package top.misec.task;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.login.ServerVerify;
import top.misec.utils.HttpUtil;

/**
 * @author @JunzhouLiu @Kurenai
 * @create 2020/10/21 17:39
 */
public class ServerPush {
    static Logger logger = (Logger) LogManager.getLogger(ServerPush.class.getName());

    private String pushToken = null;

    public void pushMsg(String text, String desp) {
        if (pushToken == null) {
            pushToken = ServerVerify.getFtkey();
        }

        String url = ApiList.ServerPush + pushToken + ".send";

        String pushBody = "text=" + text + "&desp=" + desp;

        JsonObject jsonObject = HttpUtil.doPost(url, pushBody);

        if ("success".equals(jsonObject.get("errmsg").getAsString())) {
            logger.info("任务状态推送成功");
        } else {
            logger.debug(jsonObject);
        }
    }

    public void addOtherMsg(String msg) {
        logger.info(msg);
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

}
