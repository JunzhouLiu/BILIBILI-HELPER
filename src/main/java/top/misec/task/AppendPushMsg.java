package top.misec.task;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author Junzhou Liu
 * @create 2020/10/21 19:46
 */
public class AppendPushMsg {

    static AppendPushMsg appendPushMsg = new AppendPushMsg();

    static Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
    static String pushDesp = String.valueOf(cal.getTime()) + "%0D%0A%0D%0A" + "本日任务完成情况简报：" + "%0D%0A%0D%0A";

    public static AppendPushMsg getInstance() {
        return appendPushMsg;
    }

    AppendPushMsg() {
    }

    public String getPushDesp() {
        return pushDesp;
    }


    public void appendDesp(String loggerText) {
        pushDesp += loggerText + "%0D%0A%0D%0A";
    }

}
