package top.misec.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void printTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(d);
        logger.info(time);
    }
}
