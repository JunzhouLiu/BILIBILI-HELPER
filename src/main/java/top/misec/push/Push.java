package top.misec.push;

import top.misec.push.model.PushMetaInfo;
import top.misec.push.model.PushResult;

/**
 * 推送消息接口
 *
 * @author itning
 * @since 2021/3/22 16:36
 */
@FunctionalInterface
public interface Push {
    /**
     * 发起推送
     *
     * @param metaInfo 元信息
     * @param content  推送内容
     * @return 推送结果
     */
    PushResult doPush(PushMetaInfo metaInfo, String content);
}
