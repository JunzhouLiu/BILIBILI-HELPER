package top.misec.task;

import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;
import java.util.concurrent.ThreadLocalRandom;

import static top.misec.task.TaskInfoHolder.calculateUpgradeDays;

/**
 * @author @JunzhouLiu @Kurenai
 * @create 2020/10/11 20:44
 */
@Slf4j
public class DailyTask {

    public void doDailyTask() {
        try {
            log.info("本日任务执行中");
            // 通过SPI获取需要执行的任务
            ServiceLoader<Task> taskServiceLoader = ServiceLoader.load(Task.class);
            for (Task task : taskServiceLoader) {
                log.info("------{}开始------", task.getName());
                task.run();
                log.info("------{}结束------\n", task.getName());
                taskSuspend();
            }
            log.info("本日任务已全部执行完毕");
            calculateUpgradeDays();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            ServerPush.doServerPush();
        }
    }


    // 任务睡眠1500~4500 ms
    private void taskSuspend() throws InterruptedException {
        int sleepTime = ThreadLocalRandom.current().nextInt(1500, 4500);
        log.info("-----随机暂停{}ms-----", sleepTime);
        Thread.sleep(sleepTime);
    }

}

