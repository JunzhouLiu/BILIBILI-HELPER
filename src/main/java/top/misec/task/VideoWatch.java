package top.misec.task;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import top.misec.apiquery.ApiList;
import top.misec.apiquery.oftenAPI;
import top.misec.login.Verify;
import top.misec.utils.HttpUtil;

import java.util.Random;

import static top.misec.task.DailyTask.getDailyTaskStatus;
import static top.misec.task.TaskInfoHolder.STATUS_CODE_STR;
import static top.misec.task.TaskInfoHolder.getVideoId;

/**
 * 观看分享视频
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020-11-22 5:13
 */
@Log4j2
public class VideoWatch implements Task {

    private final String taskName = "观看分享视频";

    @Override
    public void run() {

        JsonObject dailyTaskStatus = getDailyTaskStatus();
        String bvid = getVideoId.getRegionRankingVideoBvid();
        if (!dailyTaskStatus.get("watch").getAsBoolean()) {
            int playedTime = new Random().nextInt(90) + 1;
            String postBody = "bvid=" + bvid
                    + "&played_time=" + playedTime;
            JsonObject resultJson = HttpUtil.doPost(ApiList.videoHeartbeat, postBody);
            String videoTitle = oftenAPI.videoTitle(bvid);
            int responseCode = resultJson.get(STATUS_CODE_STR).getAsInt();
            if (responseCode == 0) {
                log.info("视频: " + videoTitle + "播放成功,已观看到第" + playedTime + "秒");
            } else {
                log.debug("视频: " + videoTitle + "播放失败,原因: " + resultJson.get("message").getAsString());
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

    @Override
    public String getName() {
        return taskName;
    }

    /**
     * @param bvid 要分享的视频bvid
     */
    private void dailyAvShare(String bvid) {
        String requestBody = "bvid=" + bvid + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject result = HttpUtil.doPost((ApiList.AvShare), requestBody);

        String videoTitle = oftenAPI.videoTitle(bvid);

        if (result.get(STATUS_CODE_STR).getAsInt() == 0) {
            log.info("视频: " + videoTitle + " 分享成功");
        } else {
            log.debug("视频分享失败，原因: " + result.get("message").getAsString());
            log.debug("开发者提示: 如果是csrf校验失败请检查BILI_JCT参数是否正确或者失效");
        }
    }
}
