package top.misec.push.model;

import lombok.Builder;
import lombok.Getter;

/**
 * 元信息
 *
 * @author itning
 * @since 2021/3/22 16:58
 */
@Getter
@Builder
public class PushMetaInfo {

    /**
     * TOKEN
     */
    private final String token;

    /**
     * Telegram Chat Id
     */
    private final String chatId;

    /**
     * 失败后重试次数
     */
    private final int numberOfRetries;

    /**
     * 失败后重试间隔
     */
    private final long retryInterval;

}
