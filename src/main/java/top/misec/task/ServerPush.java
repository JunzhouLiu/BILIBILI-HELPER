package top.misec.task;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import top.misec.apiquery.ApiList;
import top.misec.login.ServerVerify;
import top.misec.push.PushHelper;
import top.misec.push.model.PushMetaInfo;
import top.misec.utils.HttpUtil;
import top.misec.utils.LoadFileResource;

/**
 * @author @JunzhouLiu @Kurenai
 * @create 2020/10/21 17:39
 */

@Log4j2
public class ServerPush {

    private String pushToken = null;

    public void pushServerChan(String text, String desp) {
        String url;
        String pushBody;
        if (pushToken == null) {
            pushToken = ServerVerify.getFtkey();
        }

        if (pushToken.contains("SCU")) {
            url = ApiList.ServerPush + pushToken + ".send";
            pushBody = "text=" + text + "&desp=" + desp;
            log.info("采用旧版server酱推送渠道推送");
        } else {
            url = ApiList.ServerPushV2 + pushToken + ".send";
            pushBody = "title=" + text + "&desp=" + desp;
            log.info("采用Turbo版server酱推送渠道推送");
        }

        int retryTimes = 3;
        while (retryTimes > 0) {
            retryTimes--;
            try {
                JsonObject jsonObject = HttpUtil.doPost(url, pushBody);
                if (jsonObject.get("code").getAsInt() == 0 || "success".equals(jsonObject.get("errmsg").getAsString())) {
                    log.info("任务状态推送成功");
                    break;
                } else {
                    log.info("任务状态推送失败，开始第{}次重试", 3 - retryTimes);
                    log.debug(jsonObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static void doServerPush() {
        PushMetaInfo.PushMetaInfoBuilder builder = PushMetaInfo.builder().numberOfRetries(3);
        PushHelper.Target target = null;
        if (ServerVerify.getFtkey() != null && ServerVerify.getChatId() == null) {
            builder = builder.token(ServerVerify.getFtkey());
            // 临时解决方案
            if (ServerVerify.getFtkey().startsWith("https://oapi.dingtalk.com")) {
                target = PushHelper.Target.DING_TALK;
                log.info("本次执行推送日志到钉钉");
            } else {
                target = ServerVerify.getFtkey().contains("SCU") ? PushHelper.Target.SERVER_CHAN : PushHelper.Target.SERVER_CHAN_TURBO;
                log.info("本次执行推送日志到微信");
            }
            if (target == PushHelper.Target.SERVER_CHAN) {
                log.info("Server酱旧版推送渠道即将下线，请前往[sct.ftqq.com](http://sct.ftqq.com/)使用Turbo版本的推送Key");
            }

        } else if (ServerVerify.getFtkey() != null) {
            builder = builder.token(ServerVerify.getFtkey()).chatId(ServerVerify.getChatId());
            target = PushHelper.Target.TELEGRAM;
            log.info("本次执行推送日志到Telegram");
        } else {
            log.info("未配置server酱,本次执行不推送日志到微信");
            log.info("未配置Telegram,本次执行不推送日志到Telegram");
        }
        if (null != target) {
            PushHelper.push(target, builder.build(), LoadFileResource.loadFile("logs/daily.log"));
        }
    }

    private void pushTelegramMsg(String text) {
        String url = ApiList.ServerPushTelegram + ServerVerify.getFtkey() + "/sendMessage";

        String pushBody = "chat_id=" + ServerVerify.getChatId() + "&text=" + text;
        int retryTimes = 3;
        while (retryTimes > 0) {
            retryTimes--;
            try {
                JsonObject jsonObject = HttpUtil.doPost(url, pushBody);
                if (jsonObject != null && "true".equals(jsonObject.get("ok").getAsString())) {
                    log.info("任务状态推送Telegram成功");
                    break;
                } else {
                    log.info("任务状态推送Telegram失败，开始第{}次重试", 3 - retryTimes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void addOtherMsg(String msg) {
        log.info(msg);
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

}
