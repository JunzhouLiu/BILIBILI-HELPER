package top.misec.task;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import top.misec.config.Config;
import top.misec.login.ServerVerify;
import top.misec.login.Verify;
import top.misec.utils.VersionInfo;

/**
 * @author Junzhou Liu
 * @create 2021/1/15 23:16
 */
@Log4j2
public class UnitTest {
    @Test
    public static void main(String[] args) {
        if (args.length < 3) {
            log.info("任务启动失败");
            log.warn("Cookies参数缺失，请检查是否在Github Secrets中配置Cookies参数");
        }
        //读取环境变量
        Verify.verifyInit(args[0], args[1], args[2]);

        if (args.length > 3) {
            ServerVerify.verifyInit(args[3]);
        }

        VersionInfo.printVersionInfo();
        //每日任务65经验

        //初始化配置
        Config.getInstance().configInit();
        new MangaRead().run();

    }
}
