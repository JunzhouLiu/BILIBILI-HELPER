package top.misec.task;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.apiquery.oftenAPI;
import top.misec.config.Config;
import top.misec.login.Verify;
import top.misec.utils.HttpUtil;

import java.util.Calendar;
import java.util.TimeZone;

import static top.misec.task.TaskInfoHolder.*;

/**
 * 给自己充电
 * <p/>
 * 月底自动给自己充电。仅充会到期的B币券，低于2的时候不会充
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020-11-22 5:43
 */

public class ChargeMe implements Task {

    static Logger logger = (Logger) LogManager.getLogger(HttpUtil.class.getName());

    private final String taskName = "给自己充电";

    @Override
    public void run() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int day = cal.get(Calendar.DATE);

        //B币券余额
        int couponBalance = userInfo.getWallet().getCoupon_balance();
        //大会员类型
        int vipType = queryVipStatusType();
        //被充电用户的userID
        String userId = Verify.getInstance().getUserId();

        if (day == 1 && vipType == 2) {
            oftenAPI.vipPrivilege(1);
            oftenAPI.vipPrivilege(2);
        }

        if (vipType == 0 || vipType == 1) {
            logger.info("普通会员和月度大会员每月不赠送B币券，所以没法给自己充电哦");
            return;
        }

        /*
          判断条件 是月底&&是年大会员&&b币券余额大于2&&配置项允许自动充电
         */
        if (day == 28 && couponBalance >= 2 &&
                Config.getInstance().isMonthEndAutoCharge() &&
                vipType == 2) {
            String requestBody = "elec_num=" + couponBalance * 10
                    + "&up_mid=" + userId
                    + "&otype=up"
                    + "&oid=" + userId
                    + "&csrf=" + Verify.getInstance().getBiliJct();

            JsonObject jsonObject = HttpUtil.doPost(ApiList.autoCharge, requestBody);

            int resultCode = jsonObject.get(statusCodeStr).getAsInt();
            if (resultCode == 0) {
                JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
                int statusCode = dataJson.get("status").getAsInt();
                if (statusCode == 4) {
                    logger.info("月底了，给自己充电成功啦，送的B币券没有浪费哦");
                    logger.info("本次给自己充值了: " + couponBalance * 10 + "个电池哦");
                    //获取充电留言token
                    String orderNo = dataJson.get("order_no").getAsString();
                    chargeComments(orderNo);
                } else {
                    logger.debug("充电失败了啊 原因: " + jsonObject);
                }

            } else {
                logger.debug("充电失败了啊 原因: " + jsonObject);
            }
        } else {
            logger.info("今天是本月的第: " + day + "天，还没到给自己充电日子呢");
        }
    }

    private void chargeComments(String token) {

        String requestBody = "order_id=" + token
                + "&message=" + "BILIBILI-HELPER自动充电"
                + "&csrf=" + Verify.getInstance().getBiliJct();
        JsonObject jsonObject = HttpUtil.doPost(ApiList.chargeComment, requestBody);

        if (jsonObject.get(statusCodeStr).getAsInt() == 0) {
            logger.info("充电留言成功");
        } else {
            logger.debug(jsonObject.get("message").getAsString());
        }

    }

    @Override
    public String getName() {
        return taskName;
    }
}
