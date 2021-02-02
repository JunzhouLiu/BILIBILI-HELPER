package top.misec.task;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import top.misec.config.Config;
import top.misec.login.Verify;
import top.misec.utils.HttpUtil;

/**
 * B站直播送出即将过期的礼物
 *
 * @author srcrs
 * @Time 2020-10-13
 */

@Log4j2
public class GiveGift implements Task {

    private final String taskName = "B站直播送出即将过期的礼物";
    /**
     * 获取日志记录器对象
     */
    Config config = Config.getInstance();

    @Override
    public void run() {
        try {
            /* 从配置类中读取是否需要执行赠送礼物 */
            if (!config.isGiveGift()) {
                log.info("未开启自动送出即将过期礼物功能");
                return;
            }
            /* 直播间 id */
            String roomId = "";
            /* 直播间 uid 即 up 的 id*/
            String uid = "";
            /* B站后台时间戳为10位 */
            long nowTime = System.currentTimeMillis() / 1000;
            /* 获得礼物列表 */
            JsonArray jsonArray = xliveGiftBagList();
            /* 判断是否有过期礼物出现 */
            boolean flag = true;
            for (Object object : jsonArray) {
                JsonObject json = (JsonObject) object;
                long expireAt = Long.parseLong(json.get("expire_at").getAsString());
                /* 礼物还剩 1 天送出 */
                /* 永久礼物到期时间为 0 */
                if ((expireAt - nowTime) < 60 * 60 * 25 * 1 && expireAt != 0) {
                    /* 如果有未送出的礼物，则获取一个直播间 */
                    if ("".equals(roomId)) {
                        JsonObject uidAndRid = getuidAndRid();
                        uid = uidAndRid.get("uid").getAsString();
                        roomId = uidAndRid.get("roomid").getAsString();
                    }

                    String requestBody = "biz_id=" + roomId +
                            "&ruid=" + uid +
                            "&bag_id=" + json.get("bag_id") +
                            "&gift_id=" + json.get("gift_id") +
                            "&gift_num=" + json.get("gift_num");
                    JsonObject jsonObject3 = xliveBagSend(requestBody);
                    if ("0".equals(jsonObject3.get("code").getAsString())) {
                        /* 礼物的名字 */
                        String giftName = jsonObject3.get("data").getAsJsonObject().get("gift_name").getAsString();
                        /* 礼物的数量 */
                        String giftNum = jsonObject3.get("data").getAsJsonObject().get("gift_num").getAsString();
                        log.info("给直播间 - {} - {} - 数量: {}✔", roomId, giftName, giftNum);
                        flag = false;
                    } else {
                        log.debug("送礼失败, 原因 : {}❌", jsonObject3);
                    }
                }
            }
            if (flag) {
                log.info("当前无即将过期礼物❌");
            }
        } catch (Exception e) {
            log.error("💔赠送礼物异常 : ", e);
        }
    }

    /**
     * 获取一个直播间的room_id
     *
     * @return String
     * @author srcrs
     * @Time 2020-10-13
     */
    public String xliveGetRecommend() {
        return HttpUtil.doGet("https://api.live.bilibili.com/relation/v1/AppWeb/getRecommendList")
                .get("data").getAsJsonObject()
                .get("list").getAsJsonArray()
                .get(6).getAsJsonObject()
                .get("roomid").getAsString();
    }

    /**
     * B站获取直播间的uid
     *
     * @param roomId up 主的 uid
     * @return String
     * @author srcrs
     * @Time 2020-10-13
     */
    public String xliveGetRoomUid(String roomId) {
        JsonObject pJson = new JsonObject();
        pJson.addProperty("room_id", roomId);
        String urlPram = "?room_id=" + roomId;
        return HttpUtil.doGet("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom" + urlPram)
                .get("data").getAsJsonObject()
                .get("room_info").getAsJsonObject()
                .get("uid").getAsString();
    }

    /**
     * 根据 uid 获取其 roomid
     *
     * @param mid 即 uid
     * @return String 返回一个直播间id
     * @author srcrs
     * @Time 2020-11-20
     */
    public String getRoomInfoOld(String mid) {
        JsonObject pJson = new JsonObject();
        pJson.addProperty("mid", Integer.parseInt(mid));
        String urlPram = "?mid=" + mid;
        return HttpUtil.doGet("http://api.live.bilibili.com/room/v1/Room/getRoomInfoOld" + urlPram)
                .get("data").getAsJsonObject()
                .get("roomid").getAsString();
    }

    /**
     * B站直播获取背包礼物
     *
     * @return JsonArray
     * @author srcrs
     * @Time 2020-10-13
     */
    public JsonArray xliveGiftBagList() {
        return HttpUtil.doGet("https://api.live.bilibili.com/xlive/web-room/v1/gift/bag_list")
                .get("data").getAsJsonObject()
                .get("list").getAsJsonArray();
    }

    /**
     * B站直播送出背包的礼物
     *
     * @param requestBody
     * @return JsonObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JsonObject xliveBagSend(String requestBody) {

        requestBody += "&uid=" + Verify.getInstance().getUserId() +
                "&csrf=" + Verify.getInstance().getBiliJct() +
                "&send_ruid=" + "0" +
                "&storm_beat_id=" + "0" +
                "&price=" + "0" +
                "&platform=" + "pc" +
                "&biz_code=" + "live";

        return HttpUtil.doPost("https://api.live.bilibili.com/gift/v2/live/bag_send", requestBody);
    }

    /**
     * 获取一个包含 uid 和 RooId 的 json 对象
     *
     * @return JsonObject 返回一个包含 uid 和 RooId 的 json 对象
     * @author srcrs
     * @Time 2020-11-20
     */
    public JsonObject getuidAndRid() {
        /* 直播间 id */
        String roomId;
        /* 直播间 uid 即 up 的 id*/
        String uid;
        if (!config.getUpLive().equals("0")) {
            /* 获取指定up的id */
            uid = config.getUpLive();
            roomId = getRoomInfoOld(uid);
            String status = "0";
            if (status.equals(roomId)) {
                log.info("自定义up {} 无直播间", uid);
                /* 随机获取一个直播间 */
                roomId = xliveGetRecommend();
                uid = xliveGetRoomUid(roomId);
                log.info("随机直播间");
            } else {
                log.info("自定义up {} 的直播间", uid);
            }

        } else {
            /* 随机获取一个直播间 */
            roomId = xliveGetRecommend();
            uid = xliveGetRoomUid(roomId);
            log.info("随机直播间");
        }
        JsonObject json = new JsonObject();
        json.addProperty("uid", uid);
        json.addProperty("roomid", roomId);
        return json;
    }

    @Override
    public String getName() {
        return taskName;
    }
}
