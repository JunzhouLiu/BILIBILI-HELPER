package top.misec.push.impl;

import com.google.gson.JsonObject;
import top.misec.apiquery.ApiList;
import top.misec.push.AbstractPush;
import top.misec.push.model.PushMetaInfo;

/**
 * TG推送
 *
 * @author itning
 * @since 2021/3/22 17:55
 */
public class TelegramPush extends AbstractPush {

    @Override
    protected String generatePushUrl(PushMetaInfo metaInfo) {
        return ApiList.ServerPushTelegram + metaInfo.getToken() + "/sendMessage";
    }

    @Override
    protected boolean checkPushStatus(JsonObject jsonObject) {
        return jsonObject != null && "true".equals(jsonObject.get("ok").getAsString());
    }

    @Override
    protected String generatePushBody(PushMetaInfo metaInfo, String content) {
        return "chat_id=" + metaInfo.getChatId() + "&text=BILIBILI-HELPER任务简报\n" + content;
    }
}
