package top.misec.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.Test;
import top.misec.login.ServerVerify;
import top.misec.login.Verify;

/**
 * @author Junzhou Liu
 * @create 2020/10/12 20:24
 */
public class DailyTaskTest {
    static Logger logger = (Logger) LogManager.getLogger(DailyTaskTest.class.getName());

    @Test
    public static void main(String[] args) {
        Verify.verifyInit(args[0], args[1], args[2]);
        ServerVerify.verifyInit(args[3]);
    }
}
