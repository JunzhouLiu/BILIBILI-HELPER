package top.misec.pojo.userinfobean;

import lombok.Data;

/**
 * Auto-generated
 *
 * @author Junzhou Liu
 * @create 2020/10/11 4:21
 */

@Data
public class Level_info {

    private int current_level;
    private int current_min;
    private int current_exp;
    private String next_exp;

    public int getNext_exp_asInt() {
        if ("--".equals(next_exp)) {
            return current_exp;
        } else {
            return Integer.parseInt(next_exp);
        }
    }
}