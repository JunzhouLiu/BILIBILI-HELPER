package top.misec.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Junzhou Liu
 * @create 2020/10/17 19:31
 */
public class LoadJsonFromResources {

    static Logger logger = (Logger) LogManager.getLogger(LoadJsonFromResources.class.getName());

    public static String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = LoadJsonFromResources.class.getClassLoader().getResourceAsStream("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e);
            return null;
        }
        return json;
    }
}
