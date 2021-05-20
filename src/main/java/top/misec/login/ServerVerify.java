package top.misec.login;

import lombok.Getter;

/**
 * @author Junzhou Liu
 * @create 2020/10/21 19:57
 */
@Getter
public class ServerVerify {

    private static String FT_KEY = null;
    private static String CHAT_ID = null;

    public static void verifyInit(String ftKey) {
        ServerVerify.FT_KEY = ftKey;
    }

    public static void verifyInit(String ftKey, String chatId) {
        ServerVerify.FT_KEY = ftKey;
        ServerVerify.CHAT_ID = chatId;
    }

    public static String getFtKey() {
        return FT_KEY;
    }

    public static String getChatId() {
        return CHAT_ID;
    }

}
