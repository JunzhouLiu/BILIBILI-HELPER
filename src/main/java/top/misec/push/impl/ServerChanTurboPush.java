package top.misec.push.impl;

import com.google.gson.JsonElement;
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
        // {"code":0,"message":"","data":{"pushid":"XXX","readkey":"XXX","error":"SUCCESS","errno":0}}
        JsonElement code = jsonObject.get("code");

        if (null == code) {
            return false;
        }

        return code.getAsInt() == 0;
    }

    @Override
    protected String generatePushBody(PushMetaInfo metaInfo, String content) {
        return "title=BILIBILI-HELPER任务简报&desp=" + content;
    }
}
