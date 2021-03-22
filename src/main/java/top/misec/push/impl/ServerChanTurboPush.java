package top.misec.push.impl;

import com.google.gson.JsonObject;
import top.misec.apiquery.ApiList;
import top.misec.push.AbstractPush;
import top.misec.push.model.PushMetaInfo;

/**
 * Turbo版本server酱推送
 *
 * @author itning
 * @since 2021/3/22 17:14
 */
public class ServerChanTurboPush extends AbstractPush {

    @Override
    protected String generatePushUrl(PushMetaInfo metaInfo) {
        return ApiList.ServerPushV2 + metaInfo.getToken() + ".send";
    }

    @Override
    protected boolean checkPushStatus(JsonObject jsonObject) {
        if (null == jsonObject) {
            return false;
        }

        return jsonObject.get("code").getAsInt() == 0 || "success".equals(jsonObject.get("errmsg").getAsString());
    }

    @Override
    protected String generatePushBody(PushMetaInfo metaInfo, String content) {
        return "title=BILIBILI-HELPER任务简报&desp=" + content;
    }
}
