package top.misec.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

/**
 * @author Junzhou Liu
 * @create 2020/11/21 15:22
 */

@Data
public class VersionInfo {
    static Logger logger = (Logger) LogManager.getLogger(VersionInfo.class.getName());
    private static String releaseVersion = "";
    private static String updateDate = "2021-01-13";
    private static String projectRepo = "https://github.com/JunzhouLiu/BILIBILI-HELPER";
    private static String releaseInfo = "";

    public static void initInfo() {
        String release = LoadFileResource.loadJsonFromAsset("release.json");
        JsonObject jsonObject = new JsonParser().parse(release).getAsJsonObject();
        releaseVersion = jsonObject.get("tag_main").getAsString();
        releaseInfo = LoadFileResource.loadJsonFromAsset("release.info");

    }

    public static void printVersionInfo() {
        initInfo();
        logger.info("-----版本信息-----");
        logger.info("当前版本: " + releaseVersion);
        logger.info("版本更新内容: " + releaseInfo);
        logger.info("最后更新日期: " + updateDate);
        logger.info("项目开源地址: " + projectRepo);
        logger.info("-----版本信息-----\n");
    }
}
