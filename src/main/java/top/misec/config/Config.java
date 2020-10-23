package top.misec.config;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.utils.LoadJsonFromResources;

/**
 * Auto-generated: 2020-10-13 17:10:40
 *
 * @author Junzhou Liu
 * @create 2020/10/13 17:11
 */


public class Config {

    static Logger logger = (Logger) LogManager.getLogger(Config.class.getName());


    /**
     * 每日设定的投币数 [0,5]
     */
    private int numberOfCoins;

    /**
     * 投币时是否点赞 [0,1]
     */
    private int selectLike;

    /**
     * 观看时是否分享 [0,1]
     */
    private int watchAndShare;

    /**
     * 年度大会员自动充电[false,true]
     */
    private boolean monthEndAutoCharge;

    /**
     * 执行客户端操作时的平台 [ios,android]
     */
    private String devicePlatform;

    public String getDevicePlatform() {
        return devicePlatform;
    }


    private static Config CONFIG = new Config();

    public static Config getInstance() {
        return CONFIG;
    }

    public int getSelectLike() {
        return selectLike;
    }

    public int getWatchAndShare() {
        return watchAndShare;
    }

    public Config() {
    }


    public boolean isMonthEndAutoCharge() {
        return monthEndAutoCharge;
    }

    public void setNumberOfCoins(int numberOfCoins) {
        this.numberOfCoins = numberOfCoins;
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }


    @Override
    public String toString() {
        return "Config{" +
                "numberOfCoins=" + numberOfCoins +
                ", selectLike=" + selectLike +
                ", watchAndShare=" + watchAndShare +
                ", monthEndAutoCharge=" + monthEndAutoCharge +
                ", devicePlatform='" + devicePlatform + '\'' +
                '}';
    }

    public String outputConfig() {
        String outputConfig = "您设置的每日投币数量为: ";
        outputConfig += numberOfCoins;

        if (selectLike == 1) {
            outputConfig += " 投币时是否点赞: " + "是";
        } else {
            outputConfig += " 投币时是否点赞: " + "否";
        }


        return outputConfig + " 执行app客户端操作的系统是: " + devicePlatform;
    }

    /**
     * 读取配置文件 src/main/resources/config.json
     */
    public void configInit() {
        String configJson = LoadJsonFromResources.loadJSONFromAsset();
        Config.CONFIG = new Gson().fromJson(configJson, Config.class);
        logger.info("----Init ConfigFile Successful----");
        logger.info(Config.getInstance().outputConfig());
    }
}
