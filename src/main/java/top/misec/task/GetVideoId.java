package top.misec.task;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import top.misec.apiquery.ApiList;
import top.misec.login.Verify;
import top.misec.utils.HttpUtil;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author @JunzhouLiu
 * @create 2020/11/12 13:17
 */

@Log4j2
public class GetVideoId {
    private ArrayList<String> followUpVideoList;
    private ArrayList<String> rankVideoList;
    private ArrayBlockingQueue<String> followUpVideoQueue;

    public GetVideoId() {
        this.followUpVideoList = queryDynamicNew();
        this.rankVideoList = regionRanking();
        if (this.followUpVideoList.size() > 0) {
            this.followUpVideoQueue = new ArrayBlockingQueue<>(followUpVideoList.size());
            this.followUpVideoQueue.addAll(followUpVideoList);
        }
    }

    public void updateAllVideoList() {
        this.followUpVideoList = queryDynamicNew();
        this.rankVideoList = regionRanking();
        if (this.followUpVideoList.size() > 0) {
            this.followUpVideoQueue = new ArrayBlockingQueue<>(followUpVideoList.size());
            this.followUpVideoQueue.addAll(followUpVideoList);
        }
    }

    /**
     * 从动态中获取随机bv号
     */
    public String getFollowUpRandomVideoBvid() {
        if (followUpVideoList.size() == 0) {
            return getRegionRankingVideoBvid();
        }
        Random random = new Random();
        return followUpVideoList.get(random.nextInt(followUpVideoList.size()));
    }

    /**
     * 暂未启用的方法
     *
     * @return 从阻塞队列中获取bv号
     */
    @ExtensionMethod
    public String getFollowUpRecentVideoBvid() {
        return followUpVideoQueue.peek() == null ? getRegionRankingVideoBvid() : followUpVideoQueue.poll();
    }

    /**
     * 排行榜获取随机bv号
     */
    public String getRegionRankingVideoBvid() {
        Random random = new Random();
        return rankVideoList.get(random.nextInt(rankVideoList.size()));
    }

    public ArrayList<String> queryDynamicNew() {
        ArrayList<String> arrayList = new ArrayList<>();
        String urlParameter = "?uid=" + Verify.getInstance().getUserId()
                + "&type_list=8"
                + "&from="
                + "&platform=web";
        JsonObject jsonObject = HttpUtil.doGet(ApiList.queryDynamicNew + urlParameter);
        JsonArray jsonArray = jsonObject.getAsJsonObject("data").getAsJsonArray("cards");

        if (jsonArray != null) {
            for (JsonElement videoInfo : jsonArray) {
                JsonObject tempObject = videoInfo.getAsJsonObject().getAsJsonObject("desc");
                arrayList.add(tempObject.get("bvid").getAsString());
            }
        }
        return arrayList;
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
    public ArrayList<String> regionRanking() {
        int rid = randomRegion();
        int day = 3;
        return regionRanking(rid, day);
    }

    /**
     * @param rid 分区id 默认为3
     * @param day 日榜，三日榜 周榜 1，3，7
     * @return 随机返回一个aid
     */
    public ArrayList<String> regionRanking(int rid, int day) {

        ArrayList<String> videoList = new ArrayList<>();
        String urlParam = "?rid=" + rid + "&day=" + day;
        JsonObject resultJson = HttpUtil.doGet(ApiList.getRegionRanking + urlParam);

        JsonArray jsonArray = resultJson.getAsJsonArray("data");

        if (jsonArray != null) {
            for (JsonElement videoInfo : jsonArray) {
                JsonObject tempObject = videoInfo.getAsJsonObject();
                videoList.add(tempObject.get("bvid").getAsString());
            }
        }
        return videoList;
    }
}
