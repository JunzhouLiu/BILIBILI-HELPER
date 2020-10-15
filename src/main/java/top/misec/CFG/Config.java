package top.misec.CFG;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Auto-generated: 2020-10-13 17:10:40
 *
 * @author Junzhou Liu
 * @create 2020/10/13 17:11
 */


public class Config {

    static Logger logger = (Logger) LogManager.getLogger(Config.class.getName());

    private int numberOfCoins;
    private int select_like;
    private int watch_share;
    private int month_end_auto_charge;

    public int getMonth_end_auto_charge() {
        return month_end_auto_charge;
    }

    public void setMonth_end_auto_charge(int month_end_auto_charge) {
        this.month_end_auto_charge = month_end_auto_charge;
    }

    private static Config CONFIG = new Config();

    public static Config getInstance() {
        return CONFIG;
    }

    public Config() {
    }

    public int isWatch_share() {
        return watch_share;
    }

    public void setWatch_share(int watch_share) {
        this.watch_share = watch_share;
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
                ", watch_share=" + watch_share +
                '}';
    }

    public String outputConfig() {
        String outputConfig = "----您设置的每日投币数量为: ";
        outputConfig += numberOfCoins;

        if (select_like == 0) {
            outputConfig += " 投币时不点赞 ";
        } else {
            outputConfig += " 投币时点赞 ";
        }

        if (watch_share == 1) {
            outputConfig += " 观看时分享----";
        } else {
            outputConfig += " 观看时不分享----";
        }

        return outputConfig;
    }

    /**
     * 读取配置文件 src/main/resources/config.json
     */
    public void ConfigInit() {
        try {
            FileInputStream in = new FileInputStream("src/main/resources/config.json");
            Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            Config.CONFIG = new Gson().fromJson(reader, Config.class);
            logger.info("----Init ConfigFile Successful----");
            logger.debug(Config.getInstance().outputConfig());
        } catch (FileNotFoundException e) {
            logger.debug(e);
            e.printStackTrace();
        }
    }

}