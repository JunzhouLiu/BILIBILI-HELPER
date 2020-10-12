package top.misec.Task;

import top.misec.API.apiList;
import top.misec.Login.Verify;
import top.misec.Utils.HttpUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 20:44
 */
public class DailyTask implements ExpTask {
    static Logger logger = (Logger) LogManager.getLogger(DailyTask.class.getName());

    public boolean avShare() {
        //JsonObject jsonObject = new JsonObject();
        //jsonObject.addProperty("aid", "78667972");
        //jsonObject.addProperty("csrf", Verify.getInstance().getBiliJct());
        String requestBody = "aid=" + 78667972 + "&csrf=" + Verify.getInstance().getBiliJct();
        String result = HttpUnit.Post((apiList.AvShare), requestBody);
        logger.debug(result);
        return false;
    }

    public boolean mangaSign(String platform) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("platform", "android");
//        //System.out.println(jsonObject.toString());

        String requestBody = "platform=" + platform;

        String result = HttpUnit.Post(apiList.Manga, requestBody);

        logger.debug(result);
        return false;
    }
}
