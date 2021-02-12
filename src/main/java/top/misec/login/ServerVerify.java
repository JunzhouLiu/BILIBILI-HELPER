package top.misec.login;

/**
 * @author Junzhou Liu
 * @create 2020/10/21 19:57
 */
public class ServerVerify {

    private static String FTKEY = null;

    public static void verifyInit(String ftKey) {
        ServerVerify.FTKEY = ftKey;
    }

    public static String getFtkey() {
        return FTKEY;
    }

}
