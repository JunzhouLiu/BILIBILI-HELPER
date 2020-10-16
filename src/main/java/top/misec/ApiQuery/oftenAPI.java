package top.misec.ApiQuery;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.API.API;
import top.misec.Login.Verify;
import top.misec.Utils.HttpUnit;

/**
 * 部分API简单封装。
 *
 * @author Junzhou Liu
 * @create 2020/10/14 14:27
 */
public class oftenAPI {
    static Logger logger = (Logger) LogManager.getLogger(oftenAPI.class.getName());

    /**
     * @return 返回主站查询到的硬币余额，查询失败返回0.0
     */
    public static Double getCoinBalance() {
        JsonObject jsonObject = HttpUnit.Get(API.getCoinBalance);
        int responseCode = jsonObject.get("code").getAsInt();
        if (responseCode == 0) {
            return jsonObject.get("data").getAsJsonObject().get("money").getAsDouble();
        } else {
            logger.debug(jsonObject);
            return 0.0;
        }
    }

    /**
     * @param type 1大会员B币券  2 大会员福利
     */
    public static void vipPrivilege(int type) {
        String requestBody = "type=" + type
                + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject jsonObject = HttpUnit.Post(API.vipPrivilegeReceive, requestBody);
        int responseCode = jsonObject.get("code").getAsInt();
        if (responseCode == 0) {
            if (type == 1) {
                logger.info("领取年度大会员每月赠送的B币券成功");
            } else if (type == 2) {
                logger.info("领取大会员福利/权益成功");
            }

        } else {
            logger.debug("领取年度大会员每月赠送的B币券/大会员福利失败，原因: " + jsonObject.get("message").getAsString());
        }
    }
}
