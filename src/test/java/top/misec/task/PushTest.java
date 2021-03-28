package top.misec.task;

import top.misec.push.PushHelper;
import top.misec.push.model.PushMetaInfo;

/**
 * @author itning
 * @since 2021/3/28 16:06
 */
public class PushTest {
    public static void main(String[] args) {
        PushMetaInfo metaInfo = PushMetaInfo
                .builder()
                .numberOfRetries(3)
                .token(args.length > 0 ? args[0] : null)
                .chatId(args.length > 1 ? args[1] : null)
                .build();
        PushHelper.push(PushHelper.Target.PUSH_PLUS, metaInfo, "测试内容");
    }
}
