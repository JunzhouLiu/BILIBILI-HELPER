package top.misec.pojo.userinfobean;

/**
 * Auto-generated
 *
 * @author Junzhou Liu
 * @create 2020/10/11 4:21
 */
public class Level_info {

    private int current_level;
    private int current_min;
    private int current_exp;
    private String next_exp;

    public void setCurrent_level(int current_level) {
        this.current_level = current_level;
    }

    public int getCurrent_level() {
        return current_level;
    }

    public void setCurrent_min(int current_min) {
        this.current_min = current_min;
    }

    public int getCurrent_min() {
        return current_min;
    }

    public void setCurrent_exp(int current_exp) {
        this.current_exp = current_exp;
    }

    public int getCurrent_exp() {
        return current_exp;
    }

    public void setNext_exp(String next_exp) {
        this.next_exp = next_exp;
    }

    public String getNext_exp() {
        return next_exp;
    }

    public int getNext_exp_asInt() {
        if ("--".equals(next_exp)) {
            return current_exp;
        } else {
            return Integer.parseInt(next_exp);
        }
    }
}