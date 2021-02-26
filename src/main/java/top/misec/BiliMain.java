package top.misec;

import lombok.extern.log4j.Log4j2;
import top.misec.config.Config;
import top.misec.login.ServerVerify;
import top.misec.login.Verify;
import top.misec.task.DailyTask;
import top.misec.task.ServerPush;
import top.misec.utils.VersionInfo;


/**
 * @author Junzhou Liu
 * @create 2020/10/11 2:29
 */

@Log4j2
public class BiliMain {
    public static void main(String[] args) {

        if (args.length < 3) {
            log.info("任务启动失败");
            log.warn("Cookies参数缺失，请检查是否在Github Secrets中配置Cookies参数");
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
        if (!Config.getInstance().isSkipDailyTask()) {
            DailyTask dailyTask = new DailyTask();
            dailyTask.doDailyTask();
        } else {
            log.info("已开启了跳过本日任务，本日任务跳过（不会发起任何网络请求），如果需要取消跳过，请将skipDailyTask值改为false");
            ServerPush.doServerPush();
        }
    }

}
