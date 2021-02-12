package top.misec.task;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import top.misec.apiquery.ApiList;
import top.misec.pojo.userinfobean.Data;
import top.misec.utils.HelpUtil;
import top.misec.utils.HttpUtil;

import static top.misec.task.TaskInfoHolder.CODE;
import static top.misec.task.TaskInfoHolder.USER_INFO;

/**
 * 登录检查
 *
 * @author @JunzhouLiu @Kurenai
 * @since 2020-11-22 4:57
 */
@Slf4j
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
            if (userJson.get(CODE).getAsInt() == 0
                    && userJson.get("data").getAsJsonObject().get("isLogin").getAsBoolean()) {
                USER_INFO = new Gson().fromJson(userJson
                        .getAsJsonObject("data"), Data.class);
                log.info("Cookies有效，登录成功");
            } else {
                log.debug(String.valueOf(userJson));
                log.warn("Cookies可能失效了,请仔细检查Github Secrets中DEDEUSERID SESSDATA BILI_JCT三项的值是否正确、过期");
            }

            log.info("用户名称: {}", HelpUtil.userNameEncode(USER_INFO.getUname()));
            log.info("硬币余额: {}", USER_INFO.getMoney());
        }

    }

    @Override
    public String getName() {
        return taskName;
    }
}
