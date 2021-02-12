package top.misec.task;

import com.google.gson.JsonObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.misec.apiquery.ApiList;
import top.misec.apiquery.oftenAPI;
import top.misec.utils.DateUtil;
import top.misec.utils.HttpUtil;

import static top.misec.task.TaskInfoHolder.CODE;
import static top.misec.task.TaskInfoHolder.queryVipStatusType;

/**
 * 漫画权益领取
 *
 * @author @JunzhouLiu @Kurenai @happy888888
 * @since 2020-11-22 5:48
 */
@Slf4j
@Data
public class GetVipPrivilege implements Task {

    /**
     * 权益号，由https://api.bilibili.com/x/vip/privilege/my
     * 得到权益号数组，取值范围为数组中的整数
     * 为方便直接取1，为领取漫读劵，暂时不取其他的值
     */
    private int reasonId = 1;


    @Override
    public void run() {

        //根据userInfo.getVipStatus() ,如果是1 ，会员有效，0会员失效。
        //@JunzhouLiu: fixed query_vipStatusType()现在可以查询会员状态，以及会员类型了 2020-10-15
        int vipType = queryVipStatusType();
        if (vipType == 0) {
            log.info("非大会员，跳过领取大会员权益");
            return;
        }

        int day = DateUtil.getDayOfMonth();
        if (vipType == 1 && day == 1) {
            log.info("开始领取大会员漫画权益");
            String requestBody = "{\"reason_id\":" + reasonId + "}";
            //注意参数构造格式为json，不知道需不需要重载下面的Post函数改请求头
            JsonObject jsonObject = HttpUtil.doPost(ApiList.mangaGetVipReward, requestBody);
            if (jsonObject.get(CODE).getAsInt() == 0) {
                //@happy888888:好像也可以getAsString或,getAsShort
                //@JunzhouLiu:Int比较好判断
                log.info("大会员成功领取" + jsonObject.get("data").getAsJsonObject().get("amount").getAsInt() + "张漫读劵");
            } else {
                log.info("大会员领取漫读劵失败，原因为:" + jsonObject.get("msg").getAsString());
            }
        } else {
            log.info("本日非领取大会员漫画执行日期");
        }

        if (day == 1 || day % 7 == 0 && vipType == 2) {
            log.info("开始领取年度大会员权益");
            oftenAPI.vipPrivilege(1);
            oftenAPI.vipPrivilege(2);
        } else {
            log.warn("本日非领取年度大会员权益执行日期");
        }
    }

    @Override
    public String getName() {
        return "漫画权益领取";
    }
}
