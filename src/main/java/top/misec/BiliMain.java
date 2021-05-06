package top.misec;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import top.misec.config.Config;
import top.misec.login.ServerVerify;
import top.misec.login.Verify;
import top.misec.task.DailyTask;
import top.misec.task.ServerPush;
import top.misec.utils.VersionInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;


/**
 * @author Junzhou Liu
 * @create 2020/10/11 2:29
 */

public class BiliMain {
    private static final Logger log;

    static {
        // 如果此标记为true，则为腾讯云函数，使用JUL作为日志输出。
        boolean scfFlag = Boolean.getBoolean("scfFlag");
        StaticLoggerBinder.LOG_IMPL = scfFlag ? StaticLoggerBinder.LogImpl.JUL : StaticLoggerBinder.LogImpl.LOG4J2;
        log = LoggerFactory.getLogger(BiliMain.class);
        InputStream inputStream = BiliMain.class.getResourceAsStream("/logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            java.util.logging.Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            java.util.logging.Logger.getAnonymousLogger().severe(e.getMessage());
        }
    }

    public static void main(String[] args) {

        if (args.length < 3) {
            log.info("任务启动失败");
            log.warn("Cookies参数缺失，请检查是否在Github Secrets中配置Cookies参数");
            return;
        }
        //读取环境变量
        Verify.verifyInit(args[0], args[1], args[2]);

        if (args.length > 4) {
            ServerVerify.verifyInit(args[3], args[4]);
        } else if (args.length > 3) {
            ServerVerify.verifyInit(args[3]);
        }


        VersionInfo.printVersionInfo();
        //每日任务65经验
        Config.getInstance().configInit();
        if (!Boolean.TRUE.equals(Config.getInstance().getSkipDailyTask())) {
            DailyTask dailyTask = new DailyTask();
            dailyTask.doDailyTask();
        } else {
            log.info("已开启了跳过本日任务，本日任务跳过（不会发起任何网络请求），如果需要取消跳过，请将skipDailyTask值改为false");
            ServerPush.doServerPush();
        }
    }

    /**
     * 用于腾讯云函数触发
     */
    public static void mainHandler(KeyValueClass ignored) {
        StaticLoggerBinder.LOG_IMPL = StaticLoggerBinder.LogImpl.JUL;
        String config = System.getProperty("config");
        if (null == config) {
            System.out.println("取config配置为空！！！");
            return;
        }
        KeyValueClass kv = new Gson().fromJson(config, KeyValueClass.class);
        System.out.println("环境信息：");
        System.out.println(kv);
        //读取环境变量
        Verify.verifyInit(kv.getDedeuserid(), kv.getSessdata(), kv.getBiliJct());

        if (null != kv.getTelegrambottoken() && null != kv.getTelegramchatid()) {
            ServerVerify.verifyInit(kv.getTelegrambottoken(), kv.getTelegramchatid());
        } else if (null != kv.getServerpushkey()) {
            ServerVerify.verifyInit(kv.getServerpushkey());
        }


        VersionInfo.printVersionInfo();
        //每日任务65经验
        Config.getInstance().configInit(new Gson().toJson(kv));
        if (!Boolean.TRUE.equals(Config.getInstance().getSkipDailyTask())) {
            DailyTask dailyTask = new DailyTask();
            dailyTask.doDailyTask();
        } else {
            log.info("已开启了跳过本日任务，本日任务跳过（不会发起任何网络请求），如果需要取消跳过，请将skipDailyTask值改为false");
            ServerPush.doServerPush();
        }
    }

}
