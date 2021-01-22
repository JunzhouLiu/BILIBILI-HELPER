package top.misec.task;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.login.ServerVerify;
import top.misec.utils.HttpUtil;
import top.misec.utils.LoadFileResource;

import javax.net.ssl.SSLContext;
import java.util.Arrays;

/**
 * @author @JunzhouLiu @Kurenai
 * @create 2020/10/21 17:39
 */

@Log4j2
public class ServerPush {

    private String pushToken = null;

    public void pushMsg(String text, String desp) {
        if (pushToken == null) {
            pushToken = ServerVerify.getFtkey();
        }

        String url = ApiList.ServerPush + pushToken + ".send";

        String pushBody = "text=" + text + "&desp=" + desp;

        try {
            JsonObject jsonObject = HttpUtil.doPost(url, pushBody);
            if (jsonObject != null && "success".equals(jsonObject.get("errmsg").getAsString())) {
                log.info("任务状态推送成功");
            } else {
                log.info(Arrays.toString(SSLContext.getDefault().getSupportedSSLParameters().getProtocols()));
                log.info(Arrays.toString(SSLContext.getDefault().createSSLEngine().getEnabledProtocols()));
                log.info("任务状态推送失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void doServerPush() {
        if (ServerVerify.getFtkey() != null) {
            ServerPush serverPush = new ServerPush();
            serverPush.pushMsg("BILIBILI-HELPER任务简报", LoadFileResource.loadFile("logs/daily.log"));
        } else {
            log.info("未配置server酱,本次执行不推送日志到微信");
        }
    }

    public void addOtherMsg(String msg) {
        log.info(msg);
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

}
