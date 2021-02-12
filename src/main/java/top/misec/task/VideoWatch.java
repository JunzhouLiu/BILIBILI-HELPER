package top.misec.task;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import top.misec.apiquery.ApiList;
import top.misec.apiquery.oftenAPI;
import top.misec.login.Verify;
import top.misec.utils.HttpUtil;

import java.util.concurrent.ThreadLocalRandom;

import static top.misec.task.TaskInfoHolder.CODE;
import static top.misec.task.TaskInfoHolder.getVideoId;

/**
 * 观看分享视频
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020-11-22 5:13
 */
@Slf4j
public class VideoWatch implements Task {

    @Override
    public void run() {

        JsonObject dailyTaskStatus = getDailyTaskStatus();
        String bvid = getVideoId.getRegionRankingVideoBvid();
        if (!dailyTaskStatus.get("watch").getAsBoolean()) {
            int playedTime = ThreadLocalRandom.current().nextInt(90) + 1;
            String postBody = "bvid=" + bvid + "&played_time=" + playedTime;
            JsonObject resultJson = HttpUtil.doPost(ApiList.videoHeartbeat, postBody);
            String videoTitle = oftenAPI.videoTitle(bvid);
            int responseCode = resultJson.get(CODE).getAsInt();
            if (responseCode == 0) {
                log.info("视频: " + videoTitle + "播放成功,已观看到第" + playedTime + "秒");
            } else {
                log.error("视频: " + videoTitle + "播放失败,原因: " + resultJson.get("message").getAsString());
            }
        } else {
            log.info("本日观看视频任务已经完成了，不需要再观看视频了");
        }

        if (!dailyTaskStatus.get("share").getAsBoolean()) {
            dailyAvShare(bvid);
        } else {
            log.info("本日分享视频任务已经完成了，不需要再分享视频了");
        }
    }


    /**
     * @return jsonObject 返回status对象，包含{"login":true,"watch":true,"coins":50,
     * "share":true,"email":true,"tel":true,"safe_question":true,"identify_card":false}
     * @author @srcrs
     */
    private JsonObject getDailyTaskStatus() {
        JsonObject jsonObject = HttpUtil.doGet(ApiList.reward);
        int responseCode = jsonObject.get(CODE).getAsInt();
        if (responseCode == 0) {
            log.info("请求本日任务完成状态成功");
            return jsonObject.get("data").getAsJsonObject();
        } else {
            log.error("请求本日任务完成状态失败: {}", jsonObject.get("message").getAsString());
            return HttpUtil.doGet(ApiList.reward).get("data").getAsJsonObject();
            //偶发性请求失败，再请求一次。
        }
    }

    @Override
    public String getName() {
        return "观看分享视频";
    }

    /**
     * @param bvid 要分享的视频bvid
     */
    private void dailyAvShare(String bvid) {
        String requestBody = "bvid=" + bvid + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject result = HttpUtil.doPost(ApiList.AvShare, requestBody);
        String videoTitle = oftenAPI.videoTitle(bvid);
        if (result.get(CODE).getAsInt() == 0) {
            log.info("视频: " + videoTitle + " 分享成功");
        } else {
            log.error("视频分享失败，原因: " + result.get("message").getAsString());
            log.error("开发者提示: 如果是csrf校验失败请检查BILI_JCT参数是否正确或者失效");
        }
    }
}
