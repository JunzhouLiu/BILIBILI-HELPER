package top.misec.push;

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
     * @param push   推送实例
     * @param metaInfo 元信息
     * @param content  内容
     * @return 推送结果
     */
    public static boolean push(Push push, PushMetaInfo metaInfo, String content) {
        return push.doPush(metaInfo, content).isSuccess();
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
        PUSH_PLUS,
        /**
         * 企业微信
         */
        WEIXING
    }
}
