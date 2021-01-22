package top.misec.apiquery;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.config.Config;
import top.misec.login.Verify;
import top.misec.utils.HttpUtil;

import java.util.Collections;

/**
 * 部分API简单封装。
 *
 * @author Junzhou Liu
 * @create 2020/10/14 14:27
 */
@Log4j2
public class oftenAPI {

    /**
     * @return 返回主站查询到的硬币余额，查询失败返回0.0
     */
    public static Double getCoinBalance() {
        JsonObject responseJson = HttpUtil.doGet(ApiList.getCoinBalance);
        int responseCode = responseJson.get("code").getAsInt();

        JsonObject dataObject = responseJson.get("data").getAsJsonObject();

        if (responseCode == 0) {
            if (dataObject.get("money").isJsonNull()) {
                return 0.0;
            } else {
                return dataObject.get("money").getAsDouble();
            }
        } else {
            log.debug("请求硬币余额接口错误，请稍后重试。错误请求信息：" + responseJson);
            return 0.0;
        }
    }

    /**
     * @param type 1大会员B币券  2 大会员福利
     */
    public static void vipPrivilege(int type) {
        String requestBody = "type=" + type
                + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject jsonObject = HttpUtil.doPost(ApiList.vipPrivilegeReceive, requestBody);
        int responseCode = jsonObject.get("code").getAsInt();
        if (responseCode == 0) {
            if (type == 1) {
                log.info("领取年度大会员每月赠送的B币券成功");
            } else if (type == 2) {
                log.info("领取大会员福利/权益成功");
            }

        } else {
            log.debug("领取年度大会员每月赠送的B币券/大会员福利失败，原因: " + jsonObject.get("message").getAsString());
        }
    }

    /**
     * 请求视频title，未获取到时返回bvid
     *
     * @return title
     */
    public static String videoTitle(String bvid) {
        String title;
        String urlParameter = "?bvid=" + bvid;
        JsonObject jsonObject = HttpUtil.doGet(ApiList.videoView + urlParameter);

        if (jsonObject.get("code").getAsInt() == 0) {
            title = jsonObject.getAsJsonObject("data").getAsJsonObject("owner").get("name").getAsString() + ": ";
            title += jsonObject.getAsJsonObject("data").get("title").getAsString();
        } else {
            title = "未能获取标题";
            log.info(title);
            log.debug(jsonObject.get("message").getAsString());
        }

        return title.replace("&", "-");
    }

    public static String queryUserName(String uid) {
        String urlParameter = "?mid=" + uid + "&jsonp=jsonp";
        String userName = "1";
        JsonObject jsonObject = HttpUtil.doGet(ApiList.queryUserName + urlParameter);
        if (jsonObject.get("code").getAsInt() == 0) {
            userName = jsonObject.getAsJsonObject("data").get("name").getAsString();
        } else {
            log.info("查询充电对象的用户名失败,充电对象已置为你本人" + jsonObject);
        }
        return userName;
    }


}
