package top.misec.ApiQuery;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.API.API;
import top.misec.Utils.HttpUnit;

/**
 * 频繁使用的API数据简单封装。
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
            double coin_balance = jsonObject.get("data").getAsJsonObject().get("money").getAsDouble();
            return coin_balance;
        } else {
            logger.debug(jsonObject);
            return 0.0;
        }
    }
}
