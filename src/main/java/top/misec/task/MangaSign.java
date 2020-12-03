package top.misec.task;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.config.Config;
import top.misec.utils.HttpUtil;

/**
 * 漫画签到
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020-11-22 5:22
 */

public class MangaSign implements Task {

    static Logger logger = (Logger) LogManager.getLogger(MangaSign.class.getName());

    private final String taskName = "漫画签到";

    @Override
    public void run() {

        String platform = Config.getInstance().getDevicePlatform();
        String requestBody = "platform=" + platform;
        JsonObject result = HttpUtil.doPost(ApiList.Manga, requestBody);

        if (result == null) {
            logger.info("哔哩哔哩漫画已经签到过了");
        } else {
            logger.info("完成漫画签到");
        }
    }

    @Override
    public String getName() {
        return taskName;
    }
}
