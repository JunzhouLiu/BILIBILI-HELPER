package top.misec.task;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.apiquery.oftenAPI;
import top.misec.config.Config;
import top.misec.login.ServerVerify;
import top.misec.login.Verify;
import top.misec.pojo.userinfobean.Data;
import top.misec.utils.HttpUtil;
import top.misec.utils.LoadFileResource;

import java.util.Calendar;
import java.util.Collections;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 20:44
 */
public class DailyTask {
    static Logger logger = (Logger) LogManager.getLogger(DailyTask.class.getName());
    private final String statusCodeStr = "code";
    Data userInfo = null;
    GetVideoId getVideoId = new GetVideoId();

    /**
     * @param bvid 要分享的视频bvid
     */
    public void dailyAvShare(String bvid) {
        String requestBody = "bvid=" + bvid + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject result = HttpUtil.doPost((ApiList.AvShare), requestBody);

        String videoTitle = oftenAPI.videoTitle(bvid);

        if (result.get(statusCodeStr).getAsInt() == 0) {
            logger.info("视频: " + videoTitle + " 分享成功");
        } else {
            logger.debug("视频分享失败，原因: " + result.get("message").getAsString());
            logger.debug("开发者提示: 如果是csrf校验失败请检查BILI_JCT参数是否正确或者失效");
            doServerPush();
        }
    }

    public void doMangaSign() {
        String platform = Config.getInstance().getDevicePlatform();
        String requestBody = "platform=" + platform;
        JsonObject result = HttpUtil.doPost(ApiList.Manga, requestBody);

        if (result == null) {
            logger.info("哔哩哔哩漫画已经签到过了");
        } else {
            logger.info("完成漫画签到");
        }
    }

    /**
     * @param bvid       av号
     * @param multiply   投币数量
     * @param selectLike 是否同时点赞 1是
     * @return 是否投币成功
     */
    public boolean coinAdd(String bvid, int multiply, int selectLike) {
        String requestBody = "bvid=" + bvid
                + "&multiply=" + multiply
                + "&select_like=" + selectLike
                + "&cross_domain=" + "true"
                + "&csrf=" + Verify.getInstance().getBiliJct();
        String videoTitle = oftenAPI.videoTitle(bvid);
        //判断曾经是否对此av投币过
        if (!isCoin(bvid)) {
            JsonObject jsonObject = HttpUtil.doPost(ApiList.CoinAdd, requestBody);
            if (jsonObject.get(statusCodeStr).getAsInt() == 0) {

                logger.info("为 " + videoTitle + " 投币成功");
                return true;
            } else {
                logger.info("投币失败" + jsonObject.get("message").getAsString());
                return false;
            }
        } else {
            logger.debug("已经为" + videoTitle + "投过币了");
            return false;
        }
    }

    /**
     * 检查是否投币
     *
     * @param bvid av号
     * @return 返回是否投过硬币了
     */
    public boolean isCoin(String bvid) {
        String urlParam = "?bvid=" + bvid;
        JsonObject result = HttpUtil.doGet(ApiList.isCoin + urlParam);

        int multiply = result.getAsJsonObject("data").get("multiply").getAsInt();
        if (multiply > 0) {
            logger.info("之前已经为av" + bvid + "投过" + multiply + "枚硬币啦");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前投币获得的经验值
     *
     * @return 本日已经投了几个币
     */
    public int expConfirm() {
        JsonObject resultJson = HttpUtil.doGet(ApiList.needCoinNew);
        int getCoinExp = resultJson.get("data").getAsInt();
        // logger.info("今日已获得投币经验: " + getCoinExp);
        return getCoinExp / 10;
    }

    public void calculateUpgradeDays() {

        int needExp = userInfo.getLevel_info().getNext_exp_asInt()
                - userInfo.getLevel_info().getCurrent_exp();
        int todayExp = 15;
        todayExp += expConfirm() * 10;
        logger.info("今日获得的总经验值为: " + todayExp);

        if (userInfo.getLevel_info().getCurrent_level() < 6) {
            logger.info("按照当前进度，升级到升级到Lv" + (userInfo.getLevel_info().getCurrent_level() + 1) + "还需要: " +
                    needExp / todayExp + "天");
        } else {
            logger.info("当前等级Lv6，经验值为：" + userInfo.getLevel_info().getCurrent_exp());
        }
    }

    /**
     * 投币操作
     */
    public void doCoinAdd() {
        //投币最多操作数 解决csrf校验失败时死循环的问题
        int addCoinOperateCount = 0;
        //安全检查，最多投币数
        final int maxNumberOfCoins = 5;
        //获取自定义配置投币数 配置写在src/main/resources/config.json中
        int setCoin = Config.getInstance().getNumberOfCoins();
        //已投的硬币
        int useCoin = expConfirm();
        //投币策略
        int coinAddPriority = Config.getInstance().getCoinAddPriority();

        if (setCoin > maxNumberOfCoins) {
            logger.info("自定义投币数为: " + setCoin + "枚," + "为保护你的资产，自定义投币数重置为: " + maxNumberOfCoins + "枚");
            setCoin = maxNumberOfCoins;
        }

        logger.info("自定义投币数为: " + setCoin + "枚," + "程序执行前已投: " + useCoin + "枚");

        //调整投币数 设置投币数-已经投过的硬币数
        int needCoins = setCoin - useCoin;

        //投币前硬币余额
        Double beforeAddCoinBalance = oftenAPI.getCoinBalance();
        int coinBalance = (int) Math.floor(beforeAddCoinBalance);

        if (needCoins <= 0) {
            logger.info("已完成设定的投币任务，今日无需再投币了");
        } else {
            logger.info("投币数调整为: " + needCoins + "枚");
            //投币数大于余额时，按余额投
            if (needCoins > coinBalance) {
                logger.info("完成今日设定投币任务还需要投: " + needCoins + "枚硬币，但是余额只有: " + beforeAddCoinBalance);
                logger.info("投币数调整为: " + coinBalance);
                needCoins = coinBalance;
            }
        }

        logger.info("投币前余额为 : " + beforeAddCoinBalance);
        /*
         * 开始投币
         * 请勿修改 max_numberOfCoins 这里多判断一次保证投币数超过5时 不执行投币操作
         * 最后一道安全判断，保证即使前面的判断逻辑错了，也不至于发生投币事故
         */
        while (needCoins > 0 && needCoins <= maxNumberOfCoins) {
            String bvid;

            if (coinAddPriority == 1 && addCoinOperateCount < 7) {
                bvid = getVideoId.getFollowUpRandomVideoBvid();
            } else {
                bvid = getVideoId.getRegionRankingVideoBvid();
            }

            addCoinOperateCount++;
            boolean flag = coinAdd(bvid, 1, Config.getInstance().getSelectLike());
            if (flag) {
                try {
                    Random random = new Random();
                    int sleepTime = random.nextInt(1000) + 2000;
                    logger.info("投币后随机暂停" + sleepTime + "毫秒");
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                needCoins--;
            }
            if (addCoinOperateCount > 15) {
                logger.info("尝试投币/投币失败次数太多");
                break;
            }
        }
        logger.info("投币任务完成后余额为: " + oftenAPI.getCoinBalance());
    }

    public void silver2coin() {
        JsonObject resultJson = HttpUtil.doGet(ApiList.silver2coin);
        int responseCode = resultJson.get(statusCodeStr).getAsInt();
        if (responseCode == 0) {
            logger.info("银瓜子兑换硬币成功");
        } else {
            logger.debug("银瓜子兑换硬币失败 原因是: " + resultJson.get("msg").getAsString());
        }

        JsonObject queryStatus = HttpUtil.doGet(ApiList.getSilver2coinStatus).get("data").getAsJsonObject();
        double silver2coinMoney = oftenAPI.getCoinBalance();
        logger.info("当前银瓜子余额: " + queryStatus.get("silver").getAsInt());
        logger.info("兑换银瓜子后硬币余额: " + silver2coinMoney);

        /*
        兑换银瓜子后，更新userInfo中的硬币值
         */
        userInfo.setMoney(silver2coinMoney);

    }

    /**
     * @return jsonObject 返回status对象，包含{"login":true,"watch":true,"coins":50,
     * "share":true,"email":true,"tel":true,"safe_question":true,"identify_card":false}
     * @author @srcrs
     */
    public JsonObject getDailyTaskStatus() {
        JsonObject jsonObject = HttpUtil.doGet(ApiList.reward);
        int responseCode = jsonObject.get(statusCodeStr).getAsInt();
        if (responseCode == 0) {
            logger.info("请求本日任务完成状态成功");
            return jsonObject.get("data").getAsJsonObject();
        } else {
            logger.debug(jsonObject.get("message").getAsString());
            return HttpUtil.doGet(ApiList.reward).get("data").getAsJsonObject();
            //偶发性请求失败，再请求一次。
        }
    }

    public void videoWatch() {
        JsonObject dailyTaskStatus = getDailyTaskStatus();
        String bvid = getVideoId.getRegionRankingVideoBvid();
        if (!dailyTaskStatus.get("watch").getAsBoolean()) {
            int playedTime = new Random().nextInt(90) + 1;
            String postBody = "bvid=" + bvid
                    + "&played_time=" + playedTime;
            JsonObject resultJson = HttpUtil.doPost(ApiList.videoHeartbeat, postBody);
            String videoTitle = oftenAPI.videoTitle(bvid);
            int responseCode = resultJson.get(statusCodeStr).getAsInt();
            if (responseCode == 0) {
                logger.info("视频: " + videoTitle + "播放成功,已观看到第" + playedTime + "秒");
            } else {
                logger.debug("视频: " + videoTitle + "播放失败,原因: " + resultJson.get("message").getAsString());
            }
        } else {
            logger.info("本日观看视频任务已经完成了，不需要再观看视频了");
        }

        if (!dailyTaskStatus.get("share").getAsBoolean()) {
            dailyAvShare(bvid);
        } else {
            logger.info("本日分享视频任务已经完成了，不需要再分享视频了");
        }
    }

    /**
     * @return 返回会员类型
     * 0:无会员（会员过期，当前不是会员）
     * 1:月会员
     * 2:年会员
     */
    public int queryVipStatusType() {
        if (userInfo.getVipStatus() == 1) {
            //只有VipStatus为1的时候获取到VipType才是有效的。
            return userInfo.getVipType();
        } else {
            return 0;
        }
    }

    /**
     * 月底自动给自己充电。仅充会到期的B币券，低于2的时候不会充
     */
    public void doCharge() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int day = cal.get(Calendar.DATE);

        //B币券余额
        int couponBalance = userInfo.getWallet().getCoupon_balance();
        //大会员类型
        int vipType = queryVipStatusType();
        //被充电用户的userID
        String userId = Verify.getInstance().getUserId();

        if (day == 1 && vipType == 2) {
            oftenAPI.vipPrivilege(1);
            oftenAPI.vipPrivilege(2);
        }

        if (vipType == 0 || vipType == 1) {
            logger.info("普通会员和月度大会员每月不赠送B币券，所以没法给自己充电哦");
            return;
        }

        /*
          判断条件 是月底&&是年大会员&&b币券余额大于2&&配置项允许自动充电
         */
        if (day == 28 && couponBalance >= 2 &&
                Config.getInstance().isMonthEndAutoCharge() &&
                vipType == 2) {
            String requestBody = "elec_num=" + couponBalance * 10
                    + "&up_mid=" + userId
                    + "&otype=up"
                    + "&oid=" + userId
                    + "&csrf=" + Verify.getInstance().getBiliJct();

            JsonObject jsonObject = HttpUtil.doPost(ApiList.autoCharge, requestBody);

            int resultCode = jsonObject.get(statusCodeStr).getAsInt();
            if (resultCode == 0) {
                JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
                int statusCode = dataJson.get("status").getAsInt();
                if (statusCode == 4) {
                    logger.info("月底了，给自己充电成功啦，送的B币券没有浪费哦");
                    logger.info("本次给自己充值了: " + couponBalance * 10 + "个电池哦");
                    //获取充电留言token
                    String orderNo = dataJson.get("order_no").getAsString();
                    chargeComments(orderNo);
                } else {
                    logger.debug("充电失败了啊 原因: " + jsonObject);
                }

            } else {
                logger.debug("充电失败了啊 原因: " + jsonObject);
            }
        } else {
            logger.info("今天是本月的第: " + day + "天，还没到给自己充电日子呢");
        }
    }

    public void chargeComments(String token) {

        String requestBody = "order_id=" + token
                + "&message=" + "BILIBILI-HELPER自动充电"
                + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject jsonObject = HttpUtil.doPost(ApiList.chargeComment, requestBody);

        if (jsonObject.get(statusCodeStr).getAsInt() == 0) {
            logger.info("充电留言成功");
        } else {
            logger.debug(jsonObject.get("message").getAsString());
        }

    }

    /**
     * 获取大会员漫画权益
     *
     * @param reasonId 权益号，由https://api.bilibili.com/x/vip/privilege/my
     *                 得到权益号数组，取值范围为数组中的整数
     *                 为方便直接取1，为领取漫读劵，暂时不取其他的值
     * @return 返回领取结果和数量
     */
    public void mangaGetVipReward(int reasonId) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int day = cal.get(Calendar.DATE);

        //根据userInfo.getVipStatus() ,如果是1 ，会员有效，0会员失效。
        //@JunzhouLiu: fixed query_vipStatusType()现在可以查询会员状态，以及会员类型了 2020-10-15
        if (day != 1 || queryVipStatusType() == 0) {
            //一个月执行一次就行，跟几号没关系，由B站策略决定(有可能改领取时间)
            return;
        }

        String requestBody = "{\"reason_id\":" + reasonId + "}";
        //注意参数构造格式为json，不知道需不需要重载下面的Post函数改请求头
        JsonObject jsonObject = HttpUtil.doPost(ApiList.mangaGetVipReward, requestBody);
        if (jsonObject.get(statusCodeStr).getAsInt() == 0) {
            //@happy888888:好像也可以getAsString或,getAsShort
            //@JunzhouLiu:Int比较好判断
            logger.info("大会员成功领取" + jsonObject.get("data").getAsJsonObject().get("amount").getAsInt() + "张漫读劵");
        } else {
            logger.info("大会员领取漫读劵失败，原因为:" + jsonObject.get("msg").getAsString());
        }
    }

    /**
     * 直播签到
     */
    public void doLiveCheckin() {
        logger.info("开始直播签到");
        JsonObject liveCheckinResponse = HttpUtil.doGet(ApiList.liveCheckin);
        int code = liveCheckinResponse.get(statusCodeStr).getAsInt();
        if (code == 0) {
            JsonObject data = liveCheckinResponse.get("data").getAsJsonObject();
            logger.info("直播签到成功，本次签到获得" + data.get("text").getAsString() + "," + data.get("specialText").getAsString());
        } else {
            String message = liveCheckinResponse.get("message").getAsString();
            logger.debug("直播签到失败: " + message);
        }
    }


    public void doServerPush() {
        if (ServerVerify.getFtkey() != null) {
            ServerPush serverPush = new ServerPush();
            serverPush.pushMsg("BILIBILIHELPER任务简报", LoadFileResource.loadLogFile());
        } else {
            logger.info("未配置server酱,本次执行不推送日志到微信");
        }
    }

    public void userCheck() {
        JsonObject userJson = HttpUtil.doGet(ApiList.LOGIN);
        //判断Cookies是否有效
        if (userJson.get(statusCodeStr).getAsInt() == 0
                && userJson.get("data").getAsJsonObject().get("isLogin").getAsBoolean()) {
            userInfo = new Gson().fromJson(userJson
                    .getAsJsonObject("data"), Data.class);
            logger.info("Cookies有效，登录成功");
        } else {
            logger.debug(userJson);
            logger.warn("Cookies可能失效了,请仔细检查Github Secrets中DEDEUSERID SESSDATA BILI_JCT三项的值是否正确、过期");
            doServerPush();
        }

        Config.getInstance().configInit();

        String uname = userInfo.getUname();
        //用户名模糊处理 @happy88888
        int s1 = uname.length() / 2, s2 = (s1 + 1) / 2;
        logger.info("用户名称: " + uname.substring(0, s2) + String.join("",
                Collections.nCopies(s1, "*")) + uname.substring(s1 + s2));
        logger.info("硬币余额: " + userInfo.getMoney());

    }

    public void doDailyTask() {
        userCheck();//检查登录是否有效
        videoWatch();//观看视频 默认会调用分享
        doMangaSign();//漫画签到
        silver2coin();//银瓜子换硬币
        doCoinAdd();//投币任务
        doLiveCheckin(); //直播签到
        doCharge();
        mangaGetVipReward(1);
        logger.info("本日任务已全部执行完毕");
        calculateUpgradeDays();
        doServerPush();
    }
}

