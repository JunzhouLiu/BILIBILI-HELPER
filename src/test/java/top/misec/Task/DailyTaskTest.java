package top.misec.task;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.Test;
import top.misec.login.Verify;
import top.misec.pojo.userinfobean.Data;

/**
 * @author Junzhou Liu
 * @create 2020/10/12 20:24
 */
public class DailyTaskTest {
    static Logger logger = (Logger) LogManager.getLogger(DailyTaskTest.class.getName());

    @Test
    public static void main(String[] args) {
        Verify.verifyInit(args[0], args[1], args[2]);

        String str = "{\"code\":0,\"message\":\"0\",\"ttl\":1,\"data\":{\"isLogin\":true,\"email_verified\":1,\"face\":\"http://i1.hdslb.com/bfs/face/9a16fb874803f26153871021392a611d457fc869.jpg\",\"level_info\":{\"current_level\":6,\"current_min\":28800,\"current_exp\":33381,\"next_exp\":\"--\"},\"mid\":1291795,\"mobile_verified\":1,\"money\":520,\"moral\":73,\"official\":{\"role\":0,\"title\":\"\",\"desc\":\"\",\"type\":-1},\"officialVerify\":{\"type\":-1,\"desc\":\"\"},\"pendant\":{\"pid\":249,\"name\":\"碧蓝之海\",\"image\":\"http://i1.hdslb.com/bfs/face/045d48038b9c0f21ba8e7417b8bb1b477cdda93c.png\",\"expire\":0,\"image_enhance\":\"http://i1.hdslb.com/bfs/face/045d48038b9c0f21ba8e7417b8bb1b477cdda93c.png\"},\"scores\":0,\"uname\":\"夜羊\",\"vipDueDate\":1679587200000,\"vipStatus\":1,\"vipType\":2,\"vip_pay_type\":1,\"vip_theme_type\":0,\"vip_label\":{\"path\":\"\",\"text\":\"年度大会员\",\"label_theme\":\"annual_vip\"},\"vip_avatar_subscript\":1,\"vip_nickname_color\":\"#FB7299\",\"wallet\":{\"mid\":1291795,\"bcoin_balance\":0,\"coupon_balance\":0,\"coupon_due_time\":0},\"has_shop\":false,\"shop_url\":\"\",\"allowance_count\":0,\"answer_status\":0}}";
        JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();

        JsonObject levelInfo = jsonObject.getAsJsonObject("data").get("level_info").getAsJsonObject();
        // levelInfo.addProperty("next_exp", 888888);
        logger.debug(levelInfo);

        Data userInfo = new Gson().fromJson(jsonObject
                .getAsJsonObject("data"), Data.class);

        logger.info(userInfo.getLevel_info().getNext_exp_asInt());
    }
}
