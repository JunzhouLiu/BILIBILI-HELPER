package top.misec.push;

import top.misec.push.impl.*;
import top.misec.push.model.PushMetaInfo;

/**
 * 推送工具
 *
 * @author itning
 * @since 2021/3/22 17:51
 */
public final class PushHelper {

    /**
     * 推送
     *
     * @param target   目标
     * @param metaInfo 元信息
     * @param content  内容
     * @return 推送结果
     */
    public static boolean push(Target target, PushMetaInfo metaInfo, String content) {

        switch (target) {
            case SERVER_CHAN: {
                return new ServerChanPush().doPush(metaInfo, content).isSuccess();
            }
            case SERVER_CHAN_TURBO: {
                return new ServerChanTurboPush().doPush(metaInfo, content).isSuccess();
            }
            case TELEGRAM: {
                return new TelegramPush().doPush(metaInfo, content).isSuccess();
            }
            case DING_TALK: {
                return new DingTalkPush().doPush(metaInfo, content).isSuccess();
            }
            case PUSH_PLUS: {
                return new PushPlusPush().doPush(metaInfo, content).isSuccess();
            }
            default:
                return false;
        }
    }

    /**
     * 推送目标
     */
    public enum Target {
        /**
         * server酱
         */
        SERVER_CHAN,
        /**
         * server酱turbo版
         */
        SERVER_CHAN_TURBO,
        /**
         * TG
         */
        TELEGRAM,
        /**
         * 钉钉
         */
        DING_TALK,

        /**
         * Push Plus
         */
        PUSH_PLUS
    }
}
