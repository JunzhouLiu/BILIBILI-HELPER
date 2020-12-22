package top.misec.pojo.rewardbean;

import lombok.Data;

@Data
public class JsonRootBean {

    private int code;
    private String message;
    private int ttl;
    private RewardData rewardData;

}