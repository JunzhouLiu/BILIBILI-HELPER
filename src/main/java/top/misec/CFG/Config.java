package top.misec.CFG;

/**
 * Auto-generated: 2020-10-13 17:10:40
 *
 * @author Junzhou Liu
 * @create 2020/10/13 17:11
 */


public class Config {

    private int numberOfCoins;
    private int select_like;

    public static Config CONFIG = new Config();

    public static Config getInstance() {
        return CONFIG;
    }


    public void setNumberOfCoins(int numberOfCoins) {
        this.numberOfCoins = numberOfCoins;
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    public void setSelect_like(int select_like) {
        this.select_like = select_like;
    }

    public int getSelect_like() {
        return select_like;
    }

    @Override
    public String toString() {
        return "Config{" +
                "numberOfCoins=" + numberOfCoins +
                ", select_like=" + select_like +
                '}';
    }

    public String outputConfig() {
        String outputConfig = "----默认每日投币数量为 : ";
        outputConfig += numberOfCoins;

        if (select_like == 0) {
            outputConfig += " 投币时是否点赞 : 否----";
        } else {
            outputConfig += " 投币时是否点赞 : 是----";
        }
        return outputConfig;
    }
}