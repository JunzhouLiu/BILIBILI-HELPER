package top.misec.login;

public class TelegramBotVerify {

    private static String BOT_TOKEN = null;
    private static String USER_ID = null;

    private final static TelegramBotVerify TELEGRAM_BOT_VERIFYVERIFY = new TelegramBotVerify();

    public static void verifyInit(String token, String id) {
        TelegramBotVerify.BOT_TOKEN = token;
        TelegramBotVerify.USER_ID = id;
    }

    public static String getBOT_TOKEN() {
        return BOT_TOKEN;
    }

    public static String getUSER_ID () {
        return USER_ID;
    }

    public static TelegramBotVerify getInstance() {
        return TELEGRAM_BOT_VERIFYVERIFY;
    }
}
