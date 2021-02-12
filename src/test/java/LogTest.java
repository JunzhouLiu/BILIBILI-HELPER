import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LogTest {

    @Test
    // 打印各种级别的日志用于测试
    public void createLogFile() {
        log.trace("trace level log");
        log.debug("debug level log");
        log.info("info level log");
        log.error("error level log");
        log.trace("fatal level log");
    }

}
