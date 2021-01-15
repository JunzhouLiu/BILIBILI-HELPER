package top.misec.task;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.apiquery.oftenAPI;
import top.misec.utils.HttpUtil;

import static top.misec.task.TaskInfoHolder.statusCodeStr;
import static top.misec.task.TaskInfoHolder.userInfo;

/**
 * 银瓜子换硬币
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020-11-22 5:25
 */

public class Silver2coin implements Task {

    static Logger logger = (Logger) LogManager.getLogger(Silver2coin.class.getName());

    private final String taskName = "银瓜子换硬币";

    @Override
    public void run() {

        JsonObject resultJson = HttpUtil.doGet(ApiList.silver2coin);
        int responseCode = resultJson.get(statusCodeStr).getAsInt();
        if (responseCode == 0) {
            logger.info("银瓜子兑换硬币成功");
        } else {
            logger.debug("银瓜子兑换硬币失败 原因是: " + resultJson.get("msg").getAsString());
        }

        JsonObject queryStatus = HttpUtil.doGet(ApiList.getSilver2coinStatus).get("data").getAsJsonObject();
        double silver2coinMoney = oftenAPI.getCoinBalance();
        logger.info("当前银瓜子余额: " + queryStatus.get("silver").getAsInt());
        logger.info("兑换银瓜子后硬币余额: " + silver2coinMoney);

        /*
        兑换银瓜子后，更新userInfo中的硬币值
         */
        if (userInfo != null) {
            userInfo.setMoney(silver2coinMoney);
        }
    }

    @Override
    public String getName() {
        return taskName;
    }
}
