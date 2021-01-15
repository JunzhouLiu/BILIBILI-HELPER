package top.misec.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

/**
 * @author Junzhou Liu
 * @create 2020/11/21 15:22
 */
@Log4j2
@Data
public class VersionInfo {
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
        log.info("-----版本信息-----");
        log.info("当前版本: " + releaseVersion);
        log.info("版本更新内容: " + releaseInfo);
        log.info("最后更新日期: " + updateDate);
        log.info("项目开源地址: " + projectRepo);
        log.info("-----版本信息-----\n");
    }
}
