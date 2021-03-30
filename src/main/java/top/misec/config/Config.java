package top.misec.config;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import top.misec.utils.HttpUtil;
import top.misec.utils.LoadFileResource;

/**
 * Auto-generated: 2020-10-13 17:10:40
 *
 * @author Junzhou Liu
 * @create 2020/10/13 17:11
 */
@Log4j2
@Data
public class Config {

    private static Config CONFIG = new Config();
    /**
     * 每日设定的投币数 [0,5]
     */
    private int numberOfCoins;
    /**
     * 投币时是否点赞 [0,1]
     */
    private int selectLike;
    /**
     * 年度大会员自动充电[false,true]
     */
    private boolean monthEndAutoCharge;
    /**
     * 自动打赏快过期礼物[false,true]
     */
    private boolean giveGift;
    /**
     * 打赏快过期礼物对象，为http://live.bilibili.com/后的数字
     * 填0表示随机打赏。
     */
    private String upLive;
    /**
     * 执行客户端操作时的平台 [ios,android]
     */
    private String devicePlatform;
    /**
     * 投币优先级 [0,1]
     * 0：优先给热榜视频投币，1：优先给关注的up投币
     */
    private int coinAddPriority;
    private String userAgent;
    private boolean skipDailyTask;
    private String chargeForLove;
    private int reserveCoins;

    private Config() {
    }

    public static Config getInstance() {
        return CONFIG;
    }


    @Override
    public String toString() {
        return "配置信息{" +
                "每日投币数为：" + numberOfCoins +
                "分享时是否点赞：" + selectLike +
                "月底是否充电：" + monthEndAutoCharge +
                "执行app客户端操作的系统是：" + devicePlatform +
                "投币策略：" + coinAddPriority + "\n" +
                "UA是：" + userAgent + "\n" +
                "是否跳过每日任务：" + skipDailyTask +
                '}';
    }


    /**
     * 优先从jar包同级目录读取
     * 读取配置文件 src/main/resources/config.json
     */
    public void configInit() {
        String configJson;
        String outConfig = LoadFileResource.loadConfigJsonFromFile();
        if (outConfig != null) {
            configJson = outConfig;
            log.info("读取外部配置文件成功");
        } else {
            String temp = LoadFileResource.loadJsonFromAsset("config.json");
            /**
             *兼容旧配置文件
             * "skipDailyTask": 0 -> "skipDailyTask": false
             * "skipDailyTask": 1 -> "skipDailyTask": true
             */
            String target0 = "\"skipDailyTask\": 0";
            String target1 = "\"skipDailyTask\": 1";
            if (temp.contains(target0)) {
                log.debug("兼容旧配置文件，skipDailyTask的值由0变更为false");
                configJson = temp.replaceAll(target0, "\"skipDailyTask\": false");
            } else if (temp.contains(target1)) {
                log.debug("兼容旧配置文件，skipDailyTask的值由1变更为true");
                configJson = temp.replaceAll(target1, "\"skipDailyTask\": true");
            } else {
                log.debug("使用的是最新格式的配置文件，无需执行兼容性转换");
                configJson = temp;
            }

            log.info("读取配置文件成功");
        }

        Config.CONFIG = new Gson().fromJson(configJson, Config.class);
        HttpUtil.setUserAgent(Config.getInstance().getUserAgent());
        log.info(Config.getInstance().toString());
    }
}
