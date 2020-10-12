package top.misec.Login;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 16:49
 */
public class Verify {

    private static String userId = "";
    private static String sessData = "";
    private static String biliJct = "";


    private final static Verify VERIFY = new Verify();

    public Verify() {

    }


    public Verify(String userId, String sessData, String biliJct) {
        Verify.userId = userId;
        Verify.sessData = sessData;
        Verify.biliJct = biliJct;
    }


    public static Verify getInstance() {
        return VERIFY;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessData() {
        return sessData;
    }

    public String getBiliJct() {
        return biliJct;
    }

    public String getVerify() {
        return "{\"cookieDatas\":[{" + "bili_jct=" + getBiliJct() + ";SESSDATA=" + getSessData() + ";DedeUserID=" + getUserId() + "}]}";
    }
}
