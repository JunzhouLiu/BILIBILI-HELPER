package top.misec.task;

import lombok.extern.slf4j.Slf4j;
import top.misec.login.ServerVerify;
import top.misec.push.Push;
import top.misec.push.PushHelper;
import top.misec.push.impl.*;
import top.misec.push.model.PushMetaInfo;
import top.misec.utils.LoadFileResource;

/**
 * @author @JunzhouLiu @Kurenai
 * @create 2020/10/21 17:39
 */

@Slf4j
public class ServerPush {

    public static void doServerPush() {
        PushMetaInfo.PushMetaInfoBuilder builder = PushMetaInfo.builder().numberOfRetries(3);
        Push push = null;
        String ftKey = ServerVerify.getFtKey();
        String chatId = ServerVerify.getChatId();
        if (ftKey != null && chatId == null) {
            builder = builder.token(ftKey);
            // 临时解决方案
            if (ftKey.startsWith("https://oapi.dingtalk.com")) {
                push = new DingTalkPush();
                log.info("本次执行推送日志到钉钉");
            } else if (ftKey.startsWith("SCU")) {
                push = new ServerChanPush();
                log.info("本次执行推送日志到Server酱");
                log.info("Server酱旧版推送渠道即将下线，请前往[sct.ftqq.com](https://sct.ftqq.com/)使用Turbo版本的推送Key");
            } else if (ftKey.startsWith("SCT")) {
                push = new ServerChanTurboPush();
                log.info("本次执行推送日志到Server酱Turbo版本");
            } else if (ftKey.length() == PushPlusPush.PUSH_PLUS_CHANNEL_TOKEN_DEFAULT_LENGTH) {
                push = new PushPlusPush();
                log.info("本次执行推送日志到Push Plus");
            }else if (ftKey.length() == WeiXinPush.WEIXIN_CHANNEL_TOKEN_DEFAULT_LENGTH) {
                push = new WeiXinPush();
                log.info("本次执行推送日志到企业微信");
            }
        } else if (ftKey != null) {
            builder = builder.token(ftKey).chatId(chatId);
            push = new TelegramPush();
            log.info("本次执行推送日志到Telegram");
        } else {
            log.info("未配置server酱,本次执行不推送日志到微信");
            log.info("未配置Telegram,本次执行不推送日志到Telegram");
        }
        if (null != push) {
            PushHelper.push(push, builder.build(), LoadFileResource.loadFile("/tmp/daily.log"));
        }
    }
}
