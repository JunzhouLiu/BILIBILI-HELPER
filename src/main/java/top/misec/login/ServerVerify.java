package top.misec.login;

/**
 * @author Junzhou Liu
 * @create 2020/10/21 19:57
 */
public class ServerVerify {

    private static String MsgPushKey = null;


    private final static ServerVerify SERVER_VERIFYVERIFY = new ServerVerify();

    public static void verifyInit(String MsgPushKey) {
        ServerVerify.MsgPushKey = MsgPushKey;
    }

    public static String getMsgPushKey() {
        return MsgPushKey;
    }

    public static ServerVerify getInstance() {
        return SERVER_VERIFYVERIFY;
    }
}
