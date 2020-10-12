package top.misec.Task.RewardBean;

/**
 * Auto-generated: 2020-10-12 20:35:37
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private int code;
    private String message;
    private int ttl;
    private RewardData rewardData;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getTtl() {
        return ttl;
    }

    public void setData(RewardData rewardData) {
        this.rewardData = rewardData;
    }

    public RewardData getData() {
        return rewardData;
    }

}