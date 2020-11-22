package top.misec.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

/**
 * @author Junzhou Liu
 * @create 2020/11/21 15:22
 */
public class VersionInfo {
    static Logger logger = (Logger) LogManager.getLogger(VersionInfo.class.getName());
    private static String version = "v1.1.9";
    private static String updateDate = "2020-12-26";
    private static String projectRepo = "还不滚回去学习！";

    public String getVersion() {
        return version;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getProjectRepo() {
        return projectRepo;
    }

    public static void printVersionInfo() {
        logger.info("-----版本信息-----");
        logger.info("当前版本: " + version);
        logger.info("考试时间为: " + updateDate);
        logger.info("看什么看？ " + projectRepo);
        logger.info("-----版本信息-----\n");
    }
}
