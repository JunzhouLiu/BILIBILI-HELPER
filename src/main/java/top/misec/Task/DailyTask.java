package top.misec.Task;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.API.API;
import top.misec.ApiQuery.oftenAPI;
import top.misec.CFG.Config;
import top.misec.Login.Verify;
import top.misec.Task.UserInfoBean.Data;
import top.misec.Utils.HttpUnit;

import java.util.*;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 20:44
 */
public class DailyTask implements ExpTask {
    static Logger logger = (Logger) LogManager.getLogger(DailyTask.class.getName());

    Data userInfo = null;

    public void avShare(String aid) {
        String requestBody = "aid=" + aid + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject result = HttpUnit.Post((API.AvShare), requestBody);

        if (result.get("code").getAsInt() == 0) {
            logger.info("----视频: av" + aid + "分享成功----");
        } else {
            logger.debug("----视频分享失败，原因: " + result);
        }

    }

    /**
     * @param platform "ios" or "android"
     */
    public void mangaSign(String platform) {
        String requestBody = "platform=" + platform;
        JsonObject result = HttpUnit.Post(API.Manga, requestBody);

        if (result == null) {
            logger.info("----哔哩哔哩漫画已经签到过了----");
        } else {
            logger.info("----完成漫画签到----");
        }
//        logger.debug(result);
    }

    /**
     * @param aid         av号
     * @param multiply    投币数量
     * @param select_like 是否同时点赞 1是
     * @return 是否投币成功
     */
    public boolean CoinAdd(String aid, int multiply, int select_like) {
        String requestBody = "aid=" + aid
                + "&multiply=" + multiply
                + "&select_like=" + select_like
                + "&cross_domain=" + "true"
                + "&csrf=" + Verify.getInstance().getBiliJct();

        if (!isCoin(aid)) {//判断曾经是否对此av投币过
            JsonObject jsonObject = HttpUnit.Post(API.CoinAdd, requestBody);
            if (jsonObject.get("code").getAsInt() == 0) {
                logger.info("-----投币成功-----");
                return true;
            } else {
                logger.info("-----投币失败" + jsonObject.get("message").getAsString());
                return false;
            }
        } else {
            logger.debug(aid + "已经投币过了");
            return false;
        }
    }

    /**
     * 检查是否投币
     *
     * @param aid av号
     * @return 返回是否投过硬币了
     */
    public boolean isCoin(String aid) {
        String urlParam = "?aid=" + aid;
        JsonObject result = HttpUnit.Get(API.isCoin + urlParam);

        int multiply = result.getAsJsonObject("data").get("multiply").getAsInt();
        if (multiply > 0) {
            logger.info("-----已经为Av" + aid + "投过" + multiply + "枚硬币啦-----");
            return true;
        } else {
            logger.info("-----还没有为Av" + aid + " 投过硬币，开始投币-----");
            return false;
        }
    }

    /**
     * @param rid 分区id 默认为3
     * @param day 日榜，三日榜 周榜 1，3，7
     * @return 随机返回一个aid
     */
    public String regionRanking(int rid, int day) {
        Map<String, Boolean> videoMap = new HashMap<>();

        String urlParam = "?rid=" + rid + "&day=" + day;
        JsonObject resultJson = HttpUnit.Get(API.getRegionRanking + urlParam);

        logger.info("----获取分区: " + rid + "的" + day + "日top10榜单成功----");

        JsonArray jsonArray = null;
        try {
            jsonArray = resultJson.getAsJsonArray("data");
            //极低的概率会抛异常，无法获取到jsonArray 可能是API返回的数据有问题。
            //初步判断是部分分区不参与排行榜，导致没请求到数据。
        } catch (Exception e) {
            logger.debug("如果出现了这个异常，麻烦提个Issues告诉下我: " + e);
            logger.debug("----提Issues时请附上这条信息-请求参数：" + API.getRegionRanking + urlParam);
            logger.debug("----提Issues时请附上这条信息-返回结果：" + resultJson);
        }

        if (jsonArray != null) {
            for (JsonElement videoInfo : jsonArray) {
                JsonObject TempObject = videoInfo.getAsJsonObject();
                videoMap.put(TempObject.get("aid").getAsString(), false);
            }
        }


        String[] keys = videoMap.keySet().toArray(new String[0]);
        Random random = new Random();

        return keys[random.nextInt(keys.length)];
    }

    /**
     * 从有限分区中随机返回一个分区rid
     * 后续会更新请求分区
     *
     * @return regionId 分区id
     */
    public int randomRegion() {
        int[] arr = {1, 3, 4, 5, 160, 22, 119};
        return arr[(int) (Math.random() * arr.length)];
    }

    /**
     * 默认请求动画区，3日榜单
     */
    public String regionRanking() {
        int rid = randomRegion();
        int day = 3;
        return regionRanking(rid, day);
    }

    /**
     * 获取当前投币获得的经验值
     *
     * @return 还需要投几个币  (50-已获得的经验值)/10
     */
    public int expConfirm(int coinExp) {
        JsonObject resultJson = HttpUnit.Get(API.needCoin);
        int getCoinExp = resultJson.get("number").getAsInt();
        if (getCoinExp == coinExp * 10) {
            logger.info("----本日投币任务已完成，无需投币了 ----");
            return 0;
        } else {
            logger.info("----如果需要获得本日全部经验，还需要投" + (50 - getCoinExp) / 10 + "枚硬币----");
            return (50 - getCoinExp) / 10;
        }
    }

    /**
     * 由于bilibili Api数据更新的问题，可能造成投币多投。
     * 更换API后 已修复
     */
    @Deprecated
    public void doCoinAdd() {
        final int max_numberOfCoins = 5;//安全检查，最多最多只能投5个。
        int numberOfCoins = Config.getInstance().getNumberOfCoins();
        //获取自定义配置投币数 配置写在src/main/resources/config.json中
        int exp_coin = expConfirm(numberOfCoins);//今日能够获取经验的硬币数量

        Double beforeAddCoinBalance = oftenAPI.getCoinBalance();

        logger.debug("投币前余额为 ： " + beforeAddCoinBalance);

        int coinBalance = (int) Math.floor(beforeAddCoinBalance);
        /*
          如果设定的投币数小于可获得经验的投币数，按设定的投币数执行。
          如果设定的投币数大于可获得经验的投币数，只投获能得经验的币数。
         */
        if (numberOfCoins >= exp_coin) {
            numberOfCoins = exp_coin;
        }

        /*
           这里惨痛的教训，写反了判断符号，自己测试的时候，硬币损失惨重(损失了41个硬币)
           不给我star的都是坏人！ 哼(￢︿̫̿￢☆)
           如果用户硬币余额小于以上判断后的投币数，则按用户的硬币余额数量投币。
         */
        if (coinBalance < numberOfCoins) {
            numberOfCoins = coinBalance;
        }

        /*
         * 设定的硬币数小等于已获得经验的投币数时，不再投币
         */
        if (numberOfCoins <= 5 - exp_coin) {
            numberOfCoins = 0;
        }

        /**
         * 开发时进行测试时。
         * 请勿修改 max_numberOfCoins 这里多判断一次保证投币数超过5时 不执行投币操作
         * 最后一道安全判断，保证即使前面的判断逻辑错了，也不至于发生投币事故
         */
        while (numberOfCoins > 0 && numberOfCoins <= max_numberOfCoins) {
            String aid = regionRanking();
            logger.debug("正在为av" + aid + "投币");
            boolean flag = CoinAdd(aid, 1, Config.getInstance().getSelect_like());
            if (flag) {
                numberOfCoins--;
            }
        }
        logger.debug("投币任务完成后余额为:  " + oftenAPI.getCoinBalance());
    }

    public void silver2coin() {
        JsonObject resultJson = HttpUnit.Get(API.silver2coin);
        int responseCode = resultJson.get("code").getAsInt();
        if (responseCode == 0) {
            logger.info("银瓜子兑换硬币成功");
        } else {
            logger.debug("银瓜子兑换硬币失败 原因是： " + resultJson.get("msg").getAsString());
        }

        JsonObject queryStatus = HttpUnit.Get(API.getSilver2coinStatus).get("data").getAsJsonObject();
        double silver2coinMoney = oftenAPI.getCoinBalance();
        logger.info("当前银瓜子余额 ：" + queryStatus.get("silver").getAsInt());
        logger.info("兑换银瓜子后硬币余额 ：" + silver2coinMoney);

        /*
        兑换银瓜子后，更新userInfo中的硬币值
         */
        userInfo.setMoney(silver2coinMoney);

    }


    public void videoWatch() {
        String aid = regionRanking();
        int played_time = (int) (Math.random() * 60) + 1;
        String postBody = "aid=" + aid
                + "&played_time" + played_time;
        JsonObject resultJson = HttpUnit.Post(API.videoHeartbeat, postBody);
        int responseCode = resultJson.get("code").getAsInt();

        if (responseCode == 0) {
            logger.info("av" + aid + "播放成功,已观看到第" + played_time + "秒");
            if (Config.getInstance().isWatch_share() == 1) {
                avShare(aid);//观看完开始分享视频
            }
        } else {
            logger.debug("av" + aid + "播放失败,原因: " + resultJson.get("message").getAsString());
        }

    }

    /**
     * @return 返回会员类型
     * 0：无会员（会员过期，当前不是会员）
     * 1：月会员
     * 2：年会员
     */
    public int query_vipStatusType() {
        if (userInfo.getVipStatus() == 1) {
            return userInfo.getVipType();
        } else {
            return 0;
        }

    }


    /*
     * 月底自动给自己充电。//仅充会到期的B币券，低于2的时候不会充
     */
    public void charge() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int day = cal.get(Calendar.DATE);
        int coupon_balance = userInfo.getWallet().getCoupon_balance();
        int vip_type = query_vipStatusType();

        if (day == 1 && vip_type == 2) {
            oftenAPI.vipPrivilege(1);
            oftenAPI.vipPrivilege(2);
        }
        String userId = Verify.getInstance().getUserId();//被充电用户的userID
        /*
               月底，要是年大会员，并且b币券余额大于2，配置项允许自动充电
         */
        if (day == 28 && coupon_balance >= 2 &&
                Config.getInstance().getMonth_end_auto_charge() == 1 &&
                vip_type == 2) {
            String requestBody = "elec_num=" + coupon_balance * 10
                    + "&up_mid=" + userId
                    + "&otype=up"
                    + "&oid=" + userId
                    + "&csrf=" + Verify.getInstance().getBiliJct();

            JsonObject jsonObject = HttpUnit.Post(API.autoCharge, requestBody);

            int resultCode = jsonObject.get("code").getAsInt();
            if (resultCode == 0) {
                JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
                logger.debug(dataJson);
                int statusCode = dataJson.get("status").getAsInt();
                if (statusCode == 4) {
                    logger.info("----月底了，给自己充电成功啦，送的B币券没有浪费哦----");
                    logger.info("本次给自己充值了: " + coupon_balance * 10 + "个电池哦");
                    String order_no = dataJson.get("order_no").getAsString();//充电留言token
                    chargeComments(order_no);
                } else {
                    logger.debug("----充电失败了啊 原因: " + jsonObject);
                }

            } else {
                logger.debug("----充电失败了啊 原因: " + jsonObject);
            }
        } else {
            logger.debug("今天是本月的第: " + day + "天，还没到给自己充电日子呢");
        }
    }

    public void chargeComments(String token) {

        String requestBody = "order_id=" + token
                + "&&message=" + "BILIBILI-HELPER自动充电"
                + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject jsonObject = HttpUnit.Post(API.chargeComment, requestBody);
        logger.debug(jsonObject);

    }

    /**
     * 获取大会员漫画权益
     *
     * @param reason_id 权益号，由https://api.bilibili.com/x/vip/privilege/my
     *                  得到权益号数组，取值范围为数组中的整数
     *                  为方便直接取1，为领取漫读劵，暂时不取其他的值
     * @return 返回领取结果和数量
     */
    public void mangaGetVipReward(int reason_id) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int day = cal.get(Calendar.DATE);

        //@happy888888: query_isVip貌似不能判断会员类型，上上面的charge函数应该做userInfo.getVipType()的判断，这里不需要
        //@JunzhouLiu: query_isVip是可以做是否VIP判断的 根据userInfo.getVipStatus() ,如果是1 ，会员有效，0会员失效。
        //@JunzhouLiu: fixed query_vipStatusType()现在可以查询会员状态，以及会员类型了 2020-10-15
        if (day != 1 || query_vipStatusType() != 0) {
            //是会员就可以领取
            //一个月执行一次就行，跟几号没关系，由B站策略决定(有可能改领取时间)
            return;
        }

        String requestBody = "{\"reason_id\":" + reason_id + "}";
        //注意参数构造格式为json，不知道需不需要重载下面的Post函数改请求头
        JsonObject jsonObject = HttpUnit.Post(API.mangaGetVipReward, requestBody);
        if (jsonObject.get("code").getAsInt() == 0) {
            //@happy888888:好像也可以getAsString或,getAsShort
            //@JunzhouLiu:Int比较好判断
            logger.info("大会员成功领取" + jsonObject.get("data").getAsJsonObject().get("amount").getAsInt() + "张漫读劵");
        } else {
            logger.info("大会员领取漫读劵失败，原因为：" + jsonObject.get("msg").getAsString());
        }
        logger.debug(jsonObject);
    }

    public void doDailyTask() {

        JsonObject userJson = HttpUnit.Get(API.LOGIN);

        if (userJson == null) {
            logger.info("-----Cookies可能失效了-----");
            //@happy88888: 失效上面好像就抛出JsonParseException异常了，这里执行得到吗.......
            //@JunzhouLiu: fixed 2020-10-15
        } else {
            userInfo = new Gson().fromJson(userJson
                    .getAsJsonObject("data"), Data.class);
        }

        String uname = userInfo.getUname();
        //用户名模糊处理 @happy88888
        int s1 = uname.length() / 2, s2 = (s1 + 1) / 2;
        logger.info("----用户名称: " + uname.substring(0, s2) + String.join("", Collections.nCopies(s1, "*")) + uname.substring(s1 + s2));
        logger.info("----登录成功 经验+5----");
        logger.info("----硬币余额: " + userInfo.getMoney());
        logger.info("----距离升级到Lv" + (userInfo.getLevel_info().getCurrent_level() + 1) + "还有: " +
                (userInfo.getLevel_info().getNext_exp() - userInfo.getLevel_info().getCurrent_exp()) / 65 + "天");

        Config.getInstance().ConfigInit();
        videoWatch();//观看视频 默认会调用分享
        mangaSign("ios");
        silver2coin();//银瓜子换硬币
        doCoinAdd();//投币任务
        charge();
        mangaGetVipReward(1);
    }
}

