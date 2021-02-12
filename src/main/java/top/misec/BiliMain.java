package top.misec;

import lombok.extern.slf4j.Slf4j;
import top.misec.config.Config;
import top.misec.login.ServerVerify;
import top.misec.login.Verify;
import top.misec.task.DailyTask;
import top.misec.utils.VersionInfo;


/**
 * @author Junzhou Liu
 * @create 2020/10/11 2:29
 */

@Slf4j
public class BiliMain {
    public static void main(String[] args) {

        if (args.length < 3) {
            log.error("任务启动失败: Cookies参数缺失，请检查是否在Github Secrets中配置Cookies参数");
        }
        //读取环境变量
        Verify verify = Verify.getInstance();
        verify.setUserId(args[0]);
        verify.setSessData(args[1]);
        verify.setBiliJct(args[2]);

        if (args.length > 3) {
            ServerVerify.verifyInit(args[3]);
        }

        VersionInfo.printVersionInfo();
        //每日任务65经验
        Config.init();
        if (!Config.getInstance().isSkipDailyTask()) {
            DailyTask dailyTask = new DailyTask();
            dailyTask.doDailyTask();
        } else {
            log.info("自定义配置中开启了跳过本日任务，本日任务跳过，如果需要取消跳过，请将skipDailyTask值改为false");
        }
    }

}
