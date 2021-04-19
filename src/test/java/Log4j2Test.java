import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.Test;
import top.misec.task.ServerPush;

public class Log4j2Test {

    static Logger LOG = (Logger) LogManager.getLogger(Log4j2Test.class.getName());

    public static void main(String[] args) throws Exception {
        System.out.println();
        ServerPush serverPush = new ServerPush();
        //  serverPush.setPushToken(args[0]);
    }

    // 打印各种级别的日志用于测试
    @Test
    public void logAll() throws Exception {
        LOG.trace("trace level log");
        LOG.debug("debug level log");
        LOG.info("info level log");
        LOG.error("error level log");
        LOG.fatal("fatal level log");
        // 设置休眠时间(单位ms)，控制日志打印速度
        Thread.sleep(3);
    }
}
