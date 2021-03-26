package top.misec.push.model;

import lombok.Getter;

/**
 * @author itning
 * @since 2021/3/22 16:58
 */
@Getter
public class PushResult {

    private final boolean success;

    public PushResult(boolean success) {
        this.success = success;
    }

    public static PushResult success() {
        return new PushResult(true);
    }

    public static PushResult failed() {
        return new PushResult(false);
    }

    public static boolean checkSuccess(PushResult pushResult) {
        return null != pushResult && pushResult.isSuccess();
    }
}
