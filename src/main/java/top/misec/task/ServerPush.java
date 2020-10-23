package top.misec.task;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.login.ServerVerify;
import top.misec.utils.HttpUnit;

/**
 * @author Junzhou Liu
 * @create 2020/10/21 17:39
 */
public class ServerPush {
    static Logger logger = (Logger) LogManager.getLogger(ServerPush.class.getName());


    public void pushMsg(String text, String desp) {

        String url = ApiList.ServerPush + ServerVerify.getMsgPushKey() + ".send";

        String pushBody = "text=" + text + "&desp=" + desp;

        JsonObject jsonObject = HttpUnit.doPost(url, pushBody);

        if (jsonObject.get("errmsg").getAsString().equals("success")) {
            logger.info("任务状态推送成功");
        } else {
            logger.debug(jsonObject);
        }
    }

}
