package top.misec.task;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.apiquery.ApiList;
import top.misec.pojo.userinfobean.Data;
import top.misec.utils.HttpUtil;

import java.util.Collections;

import static top.misec.task.TaskInfoHolder.STATUS_CODE_STR;
import static top.misec.task.TaskInfoHolder.userInfo;

/**
 * 登录检查
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020-11-22 4:57
 */
@Log4j2
public class UserCheck implements Task {

    private final String taskName = "登录检查";

    @Override
    public void run() {
        String requestPram = "";
        JsonObject userJson = HttpUtil.doGet(ApiList.LOGIN + requestPram);
        if (userJson == null) {
            log.info("用户信息请求失败，如果是412错误，请在config.json中更换UA，412问题仅影响用户信息确认，不影响任务");
        } else {
            userJson = HttpUtil.doGet(ApiList.LOGIN);
            //判断Cookies是否有效
            if (userJson.get(STATUS_CODE_STR).getAsInt() == 0
                    && userJson.get("data").getAsJsonObject().get("isLogin").getAsBoolean()) {
                userInfo = new Gson().fromJson(userJson
                        .getAsJsonObject("data"), Data.class);
                log.info("Cookies有效，登录成功");
            } else {
                log.debug(String.valueOf(userJson));
                log.warn("Cookies可能失效了,请仔细检查Github Secrets中DEDEUSERID SESSDATA BILI_JCT三项的值是否正确、过期");
            }

            String uname = userInfo.getUname();
            //用户名模糊处理 @happy88888
            int s1 = uname.length() / 2, s2 = (s1 + 1) / 2;
            log.info("用户名称: " + uname.substring(0, s2) + String.join("",
                    Collections.nCopies(s1, "*")) + uname.substring(s1 + s2));
            log.info("硬币余额: " + userInfo.getMoney());
        }

    }

    @Override
    public String getName() {
        return taskName;
    }
}
