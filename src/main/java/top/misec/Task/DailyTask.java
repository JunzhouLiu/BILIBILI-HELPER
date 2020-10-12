package top.misec.Task;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.API.API;
import top.misec.Login.Verify;
import top.misec.Task.RewardBean.RewardData;
import top.misec.Task.UserInfoBean.Data;
import top.misec.Utils.HttpUnit;

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
        logger.info("----完成漫画签到----");
    }

    /**
     * @param aid         av号
     * @param multiply    投币数量
     * @param select_like 是否同时点赞 1是
     */
    public void CoinAdd(String aid, int multiply, int select_like) {
        String requestBody = "aid=" + aid
                + "&multiply=" + multiply
                + "&select_like=" + select_like
                + "&cross_domain=" + "true"
                + "&csrf=" + Verify.getInstance().getBiliJct();

        if (!isCoin(aid)) {
            JsonObject jsonObject = HttpUnit.Post(API.CoinAdd, requestBody);
            if (jsonObject.get("code").getAsInt() == 0) {
                logger.info("投币成功");
            } else {
                logger.info(jsonObject.get("message").getAsString());
            }
        } else {
            logger.debug(aid + "已经投币过了");
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
        JsonObject resultJson = HttpUnit.Get(API.getRegionRanking + urlParam);
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
     * 默认请求动画区，3日榜单
     */
    public String regionRanking() {
        int rid = 1;
        int day = 3;
        return regionRanking(rid, day);
    }

    public int expConfirm() {
        JsonObject resultJson = HttpUnit.Get(API.reward);
        RewardData rewardData = new Gson().fromJson(resultJson.getAsJsonObject("data"), RewardData.class);

        if (rewardData.getCoins() == 50) {
            logger.debug(resultJson);
            logger.info("----本日投币任务已完成，无需投币了 经验+50 ----");
            return 0;
        } else {
            logger.info("还需要再投" + (50 - rewardData.getCoins()) / 10 + "枚硬币");
            return (50 - rewardData.getCoins()) / 10;
        }
    }

    /**
     * 由于bilibili Api数据更新的问题，可能造成投币多投。
     */
    @Deprecated
    public void doCoinAdd() {
        int coinNum = expConfirm();

        if (coinNum > 0) {
            String aid = regionRanking();
            CoinAdd(aid, 1, 0);
            coinNum--;
            logger.debug("正在为av" + aid + "投币");
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
        doCoinAdd();

    }


}



