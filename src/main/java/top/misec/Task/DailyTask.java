package top.misec.Task;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.API.API;
import top.misec.CFG.Config;
import top.misec.Login.Verify;
import top.misec.Task.UserInfoBean.Data;
import top.misec.Utils.HttpUnit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 20:44
 */
public class DailyTask implements ExpTask {
    static Logger logger = (Logger) LogManager.getLogger(DailyTask.class.getName());

    public void avShare(String aid) {
        String requestBody = "aid=" + aid + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject result = HttpUnit.Post((API.AvShare), requestBody);

        if (result.get("code").getAsInt() == 0) {
            logger.info("----视频分享成功 经验+5----");
        } else {
            logger.debug("----视频分享失败----");
        }

    }

    /**
     * @param platform ios  android
     */
    public void mangaSign(String platform) {
        String requestBody = "platform=" + platform;
        JsonObject result = HttpUnit.Post(API.Manga, requestBody);
        logger.debug(result);
        logger.info("----完成漫画签到----");

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
        logger.debug("投币操作" + result);
        int multiply = result.getAsJsonObject("data").get("multiply").getAsInt();
        if (multiply > 0) {
            logger.info("-----已经为AV" + aid + "投过" + multiply + "枚硬币啦-----");
            return true;
        } else {
            logger.debug("isCoin response: " + result);
            return false;
        }
    }

    /**
     * @param rid 分区id 默认为1
     * @param day 日榜，三日榜 周榜 1，3，7
     * @return 随机返回一个aid
     */
    public String regionRanking(int rid, int day) {

        String urlParam = "?rid=" + rid + "&day=" + day;

        JsonObject resultJson;
        resultJson = HttpUnit.Get(API.getRegionRanking + urlParam);

        JsonArray jsonArray = resultJson.getAsJsonArray("data");

        Map<String, Boolean> videoMap = new HashMap<>();

        for (JsonElement videoInfo : jsonArray) {
            JsonObject TempObject = videoInfo.getAsJsonObject();
            videoMap.put(TempObject.get("aid").getAsString(), false);
        }

        logger.info("-----获取分区: " + rid + "的" + day + "日top10榜单成功-----");

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
        int[] arr = {1, 3, 4, 5, 160, 202, 119};
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
            logger.info("----开始投币，还需要投" + (50 - getCoinExp) / 10 + "枚硬币----");
            return (50 - getCoinExp) / 10;
        }
    }


    /**
     * 由于bilibili Api数据更新的问题，可能造成投币多投。
     * 偶尔会返回0,导致多投。
     */
    @Deprecated
    public void doCoinAdd() {
        int coinNum = Config.CONFIG.getNumberOfCoins();
        int needCoinNum = expConfirm(coinNum);//今日能够获取经验的硬币数量

        /*
           从src/main/resources/config.json中读取配置值，默认为投币5，不同时点赞
          如果设定的投币数大于可获得经验的投币数，只投获能得经验的币数。
          如果设定的投币数小于可获得经验的投币数，投设定的投币数。
         */
        if (coinNum >= needCoinNum) {
            coinNum = needCoinNum;
        }

        while (coinNum > 0) {
            String aid = regionRanking();
            logger.debug("正在为av" + aid + "投币");
            boolean flag = CoinAdd(aid, 1, Config.CONFIG.getSelect_like());
            if (flag) {
                coinNum--;
            }
        }
    }

    /**
     * 读取配置文件 src/main/resources/config.json
     */
    public void InitConfig() {
        try {
            FileInputStream in = new FileInputStream("src/main/resources/config.json");
            Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            Config.CONFIG = new Gson().fromJson(reader, Config.class);
            logger.info("----init config file successful----");
            logger.debug(Config.getInstance().outputConfig());
        } catch (FileNotFoundException e) {
            logger.debug(e);
            e.printStackTrace();
        }

    }

    public void doDailyTask() {
        Data userInfo = new Gson().fromJson(HttpUnit.Get(API.LOGIN)
                .getAsJsonObject("data"), Data.class);

        logger.info("----用户名称---- :" + userInfo.getUname());
        logger.info("----登录成功 经验+5----");
        logger.info("----硬币余额----  :" + userInfo.getMoney());
        logger.info("----距离升级到Lv" + (userInfo.getLevel_info().getCurrent_level() + 1) + "----: " +
                (userInfo.getLevel_info().getNext_exp() - userInfo.getLevel_info().getCurrent_exp()) / 65 + " day");
        avShare(regionRanking());
        mangaSign("ios");
        InitConfig();//初始化投币配置
        doCoinAdd();
    }

}



