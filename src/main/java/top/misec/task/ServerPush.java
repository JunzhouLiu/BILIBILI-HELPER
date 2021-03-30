package top.misec.task;

import lombok.extern.log4j.Log4j2;
import top.misec.login.ServerVerify;
import top.misec.push.PushHelper;
import top.misec.push.impl.PushPlusPush;
import top.misec.push.model.PushMetaInfo;
import top.misec.utils.LoadFileResource;

/**
 * @author @JunzhouLiu @Kurenai
 * @create 2020/10/21 17:39
 */

@Log4j2
public class ServerPush {

    public static void doServerPush() {
        PushMetaInfo.PushMetaInfoBuilder builder = PushMetaInfo.builder().numberOfRetries(3);
        PushHelper.Target target = null;
        if (ServerVerify.getFtkey() != null && ServerVerify.getChatId() == null) {
            builder = builder.token(ServerVerify.getFtkey());
            // 临时解决方案
            if (ServerVerify.getFtkey().startsWith("https://oapi.dingtalk.com")) {
                target = PushHelper.Target.DING_TALK;
                log.info("本次执行推送日志到钉钉");
            } else if (ServerVerify.getFtkey().startsWith("SCU")) {
                target = PushHelper.Target.SERVER_CHAN;
                log.info("本次执行推送日志到Server酱");
                log.info("Server酱旧版推送渠道即将下线，请前往[sct.ftqq.com](http://sct.ftqq.com/)使用Turbo版本的推送Key");
            } else if (ServerVerify.getFtkey().startsWith("SCT")) {
                target = PushHelper.Target.SERVER_CHAN_TURBO;
                log.info("本次执行推送日志到Server酱Turbo版本");
            } else if (ServerVerify.getFtkey().length() == PushPlusPush.PUSH_PLUS_CHANNEL_TOKEN_DEFAULT_LENGTH) {
                target = PushHelper.Target.PUSH_PLUS;
                log.info("本次执行推送日志到Push Plus");
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
}
