package top.misec.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

/**
 * @author Junzhou Liu
 * @create 2020/11/21 15:31
 */
public class LogFormat {

    static Logger logger = (Logger) LogManager.getLogger(LogFormat.class.getName());

    public static void taskBegin(String taskName) {
        logger.info("-----" + taskName + "开始-----");
    }

    public static void taskEnd() {
        logger.info("-----任务结束-----\n");
    }

}
