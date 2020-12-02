package top.misec.task;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.utils.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static top.misec.task.TaskInfoHolder.calculateUpgradeDays;
import static top.misec.task.TaskInfoHolder.statusCodeStr;

/**
 * @author @JunzhouLiu @Kurenai
 * @create 2020/10/11 20:44
 */
public class DailyTask {
    static Logger logger = (Logger) LogManager.getLogger(DailyTask.class.getName());

    private final List<Task> dailyTasks =
            Arrays.asList(new UserCheck(), new VideoWatch(), new MangaSign(), new CoinAdd(), new Silver2coin(), new LiveCheckin(), new ChargeMe(), new GetMangaVipReward());

    public void doDailyTask() {
        try {
            printTime();
            logger.debug("任务启动中");
            for (Task task : dailyTasks) {
                logger.info("-----{}开始-----", task.getName());
                task.run();
                logger.info("-----任务结束-----\n");
            }
            logger.info("本日任务已全部执行完毕");
            calculateUpgradeDays();
        } finally {
            ServerPush.doServerPush();
        }
    }

    /**
     * @return jsonObject 返回status对象，包含{"login":true,"watch":true,"coins":50,
     * "share":true,"email":true,"tel":true,"safe_question":true,"identify_card":false}
     * @author @srcrs
     */
    public static JsonObject getDailyTaskStatus() {
        JsonObject jsonObject = HttpUtil.doGet(ApiList.reward);
        int responseCode = jsonObject.get(statusCodeStr).getAsInt();
        if (responseCode == 0) {
            logger.info("请求本日任务完成状态成功");
            return jsonObject.get("data").getAsJsonObject();
        } else {
            logger.debug(jsonObject.get("message").getAsString());
            return HttpUtil.doGet(ApiList.reward).get("data").getAsJsonObject();
            //偶发性请求失败，再请求一次。
        }
    }

    private void printTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(d);
        logger.info(time);
    }

}

