package top.misec.config;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.misec.utils.HttpUtil;
import top.misec.utils.LoadFileResource;

/**
 * Auto-generated: 2020-10-13 17:10:40
 *
 * @author Junzhou Liu
 * @create 2020/10/13 17:11
 */
@Slf4j
@Data
public class Config {

    private static Config CONFIG = new Config();
    /**
     * 每日设定的投币数 [0,5]
     */
    private Integer numberOfCoins;
    /**
     * 投币时是否点赞 [0,1]
     */
    private Integer selectLike;
    /**
     * 年度大会员自动充电[false,true]
     */
    private Boolean monthEndAutoCharge;
    /**
     * 自动打赏快过期礼物[false,true]
     */
    private Boolean giveGift;
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
    private Integer coinAddPriority;
    private String userAgent;
    private Boolean skipDailyTask;
    private String chargeForLove;
    private Integer reserveCoins;

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

    public void configInit(String json) {
        Config.CONFIG = new Gson().fromJson(json, Config.class);
        HttpUtil.setUserAgent(Config.getInstance().getUserAgent());
        log.info(Config.getInstance().toString());
    }

    /**
     * 优先从jar包同级目录读取
     * 读取配置文件 src/main/resources/config.json
     */
    public void configInit() {
        String configJson = LoadFileResource.loadJsonFromAsset("config.json");
        configJson = resolveOriginConfig(configJson);
        if (configJson != null) {
            log.info("读取初始化配置文件成功");
            Config.CONFIG.merge(new Gson().fromJson(configJson, Config.class));
        }

        configJson = LoadFileResource.loadConfigJsonFromFile();
        configJson = resolveOriginConfig(configJson);
        if (configJson != null) {
            log.info("读取外部配置文件成功");
            Config.CONFIG.merge(new Gson().fromJson(configJson, Config.class));
        }

        HttpUtil.setUserAgent(Config.getInstance().getUserAgent());
        log.info(Config.getInstance().toString());
    }

    private String resolveOriginConfig(String originConfig) {
        if (originConfig == null) {
            return null;
        }
        /**
         *兼容旧配置文件
         * "skipDailyTask": 0 -> "skipDailyTask": false
         * "skipDailyTask": 1 -> "skipDailyTask": true
         */
        String target0 = "\"skipDailyTask\": 0";
        String target1 = "\"skipDailyTask\": 1";
        if (originConfig.contains(target0)) {
            log.debug("兼容旧配置文件，skipDailyTask的值由0变更为false");
            return originConfig.replaceAll(target0, "\"skipDailyTask\": false");
        } else if (originConfig.contains(target1)) {
            log.debug("兼容旧配置文件，skipDailyTask的值由1变更为true");
            return originConfig.replaceAll(target1, "\"skipDailyTask\": true");
        } else {
            log.debug("使用的是最新格式的配置文件，无需执行兼容性转换");
            return originConfig;
        }

    }

    public void merge(Config config) {
        if (config.getNumberOfCoins() != null) {
            numberOfCoins = config.getNumberOfCoins();
        }
        if (config.getSelectLike() != null) {
            selectLike = config.getSelectLike();
        }
        if (config.getMonthEndAutoCharge() != null) {
            monthEndAutoCharge = config.getMonthEndAutoCharge();
        }
        if (config.getGiveGift() != null) {
            giveGift = config.getGiveGift();
        }
        if (config.getUpLive() != null) {
            upLive = config.getUpLive();
        }
        if (config.getDevicePlatform() != null) {
            devicePlatform = config.getDevicePlatform();
        }
        if (config.getCoinAddPriority() != null) {
            coinAddPriority = config.getCoinAddPriority();
        }
        if (config.getUserAgent() != null) {
            userAgent = config.getUserAgent();
        }
        if (config.getSkipDailyTask() != null) {
            skipDailyTask = config.getSkipDailyTask();
        }
        if (config.getChargeForLove() != null) {
            chargeForLove = config.getChargeForLove();
        }
        if (config.getReserveCoins() != null) {
            reserveCoins = config.getReserveCoins();
        }
    }
}
