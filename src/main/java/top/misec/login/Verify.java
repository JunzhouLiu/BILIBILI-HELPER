package top.misec.login;


/**
 * @author Junzhou Liu
 * @create 2020/10/11 16:49
 */
public class Verify {

    private String userId;
    private String sessData;
    private String biliJct;
    // 单例
    private volatile static Verify instance;

    private Verify() {}

    public static Verify getInstance() {
        if (instance == null) {
            synchronized (Verify.class) {
                if (instance == null) {
                    instance = new Verify();
                }
            }
        }
        return instance;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessData(String sessData) {
        this.sessData = sessData;
    }

    public void setBiliJct(String biliJct) {
        this.biliJct = biliJct;
    }

    public String toCookieVal() {
        return "bili_jct=" + getBiliJct() + ";SESSDATA=" + getSessData() + ";DedeUserID=" + getUserId() + ";";
    }
}
