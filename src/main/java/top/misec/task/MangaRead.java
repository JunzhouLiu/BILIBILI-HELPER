package top.misec.task;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.utils.HttpUtil;

import static top.misec.task.TaskInfoHolder.STATUS_CODE_STR;

/**
 * @author Junzhou Liu
 * @create 2021/1/13 17:50
 */
@Log4j2
public class MangaRead implements Task {

    @Override
    public void run() {
        String urlParam = "?device=pc&platform=web";
        String requestBody = "comic_id=27355" +
                "&ep_id=381662";
        JsonObject result = HttpUtil.doPost(ApiList.mangaRead + urlParam, requestBody);
        int code = result.get(STATUS_CODE_STR).getAsInt();
        if (code == 0) {
            log.info("本日漫画自动阅读1章节成功！，阅读漫画为：堀与宫村");
        } else {
            log.debug("阅读失败,错误信息为\n```json\n{}\n```", result);
        }

    }

    @Override
    public String getName() {
        return "每日漫画阅读";
    }
}
