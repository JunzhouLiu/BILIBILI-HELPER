package top.misec.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Junzhou Liu
 * @create 2020/10/17 19:31
 * 工具类通过流的方式读取文件
 */
public class LoadFileResource {

    static Logger logger = (Logger) LogManager.getLogger(LoadFileResource.class.getName());

    /**
     * 读取配置文件信息
     *
     * @return configJson 返回配置文件json
     */
    public static String loadConfigJsonFromAsset() {
        String configJson = null;
        try {
            InputStream is = LoadFileResource.class.getClassLoader().getResourceAsStream("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            configJson = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e);
        }
        return configJson;
    }

    /**
     * 加载日志
     *
     * @return 返回String类型的日志信息
     */
    public static String loadLogFile() {
        String log = null;
        try {
            InputStream is = new FileInputStream("logs/daily.log");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            log = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e);
        }
        return log;
    }
}
