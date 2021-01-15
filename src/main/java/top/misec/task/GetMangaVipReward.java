package top.misec.task;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.utils.HttpUtil;

import java.util.Calendar;
import java.util.TimeZone;

import static top.misec.task.TaskInfoHolder.queryVipStatusType;
import static top.misec.task.TaskInfoHolder.STATUS_CODE_STR;

/**
 * 漫画权益领取
 *
 * @author @JunzhouLiu @Kurenai @happy888888
 * @since 2020-11-22 5:48
 */
@Log4j2
public class GetMangaVipReward implements Task {

    private final String taskName = "漫画权益领取";
    /**
     * 权益号，由https://api.bilibili.com/x/vip/privilege/my
     * 得到权益号数组，取值范围为数组中的整数
     * 为方便直接取1，为领取漫读劵，暂时不取其他的值
     */
    private int reasonId = 1;

    public GetMangaVipReward() {
    }

    public GetMangaVipReward(int reasonId) {
        this.reasonId = reasonId;
    }

    @Override
    public void run() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int day = cal.get(Calendar.DATE);

        //根据userInfo.getVipStatus() ,如果是1 ，会员有效，0会员失效。
        //@JunzhouLiu: fixed query_vipStatusType()现在可以查询会员状态，以及会员类型了 2020-10-15
        if (day != 1 || queryVipStatusType() == 0) {
            //一个月执行一次就行，跟几号没关系，由B站策略决定(有可能改领取时间)
            log.info("本日非执行日期");
            return;
        }

        String requestBody = "{\"reason_id\":" + reasonId + "}";
        //注意参数构造格式为json，不知道需不需要重载下面的Post函数改请求头
        JsonObject jsonObject = HttpUtil.doPost(ApiList.mangaGetVipReward, requestBody);
        if (jsonObject.get(STATUS_CODE_STR).getAsInt() == 0) {
            //@happy888888:好像也可以getAsString或,getAsShort
            //@JunzhouLiu:Int比较好判断
            log.info("大会员成功领取" + jsonObject.get("data").getAsJsonObject().get("amount").getAsInt() + "张漫读劵");
        } else {
            log.info("大会员领取漫读劵失败，原因为:" + jsonObject.get("msg").getAsString());
        }
    }

    @Override
    public String getName() {
        return taskName;
    }
}
