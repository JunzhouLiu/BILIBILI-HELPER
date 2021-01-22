package top.misec.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Junzhou Liu
 * @create 2020/10/17 19:31
 * 工具类通过流的方式读取文件
 */
@Log4j2
public class LoadFileResource {

    /**
     * 从外部资源读取配置文件
     *
     * @return config
     */
    public static String loadConfigJsonFromFile() {
        String config = null;
        try {
            String outPath = System.getProperty("user.dir") + File.separator + "config.json" + File.separator;
            InputStream is = new FileInputStream(outPath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            config = new String(buffer, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            log.info("未扫描到外部配置文件，即将加载默认配置文件【此提示仅针自行部署的Linux用户，普通用户请忽略】");
        } catch (IOException e) {
            e.printStackTrace();
            log.debug(e);
        }
        return config;
    }


    /**
     * 从resource读取版本文件
     *
     * @param fileName 文件名
     * @return 返回读取到文件
     */
    public static String loadJsonFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = LoadFileResource.class.getClassLoader().getResourceAsStream(fileName);
            assert is != null;
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
            log.debug(e);
        }
        return json;
    }


    /**
     * @param filePath 读入的文件路径
     * @return 返回str
     */
    public static String loadFile(String filePath) {
        String logs = null;
        try {
            InputStream is = new FileInputStream(filePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            logs = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            log.debug(e);
        }
        return logs;
    }
}
