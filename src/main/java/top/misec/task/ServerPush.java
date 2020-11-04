package top.misec.task;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.login.ServerVerify;
import top.misec.login.TelegramBotVerify;
import top.misec.utils.HttpUtil;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author Junzhou Liu
 * @create 2020/10/21 17:39
 */
public class ServerPush {
    static Logger logger = (Logger) LogManager.getLogger(ServerPush.class.getName());


    public void pushMsg(String text, String desp) {

        String url = ApiList.ServerPush + ServerVerify.getFTKEY() + ".send";

        String pushBody = "text=" + text + "&desp=" + desp;

        JsonObject jsonObject = HttpUtil.doPost(url, pushBody);

        if ("success".equals(jsonObject.get("errmsg").getAsString())) {
            logger.info("任务状态推送成功");
        } else {
            logger.debug(jsonObject);
        }
    }

    public void pushMsg(String text){
        String url = null;
        try {
            url = ApiList.TelegramBotPush + "bot" + TelegramBotVerify.getBOT_TOKEN() + "/sendMessage" +
                    "?parse_mode=Markdown" +
                    "&chat_id=" + TelegramBotVerify.getUSER_ID() +
                    "&text=" + URLEncoder.encode(text.replace("%0D%0A%0D%0A","\n"), "utf-8");
            HttpUtil.doGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
