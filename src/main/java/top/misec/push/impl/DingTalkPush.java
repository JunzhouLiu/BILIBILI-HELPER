package top.misec.push.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import top.misec.push.AbstractPush;
import top.misec.push.model.PushMetaInfo;

/**
 * 钉钉机器人
 *
 * @author itning
 * @since 2021/3/22 19:15
 */
public class DingTalkPush extends AbstractPush {

    @Override
    protected String generatePushUrl(PushMetaInfo metaInfo) {
        return metaInfo.getToken();
    }

    @Override
    protected boolean checkPushStatus(JsonObject jsonObject) {
        if (jsonObject == null) {
            return false;
        }
        JsonElement errcode = jsonObject.get("errcode");
        JsonElement errmsg = jsonObject.get("errmsg");
        if (null == errcode || null == errmsg) {
            return false;
        }
        return errcode.getAsInt() == 0 && "ok".equals(errmsg.getAsString());
    }

    @Override
    protected String generatePushBody(PushMetaInfo metaInfo, String content) {
        return new Gson().toJson(new MessageModel(content));
    }

    @Getter
    static class MessageModel {
        private final String msgtype = "text";
        private final String title = "BILIBILI-HELPER任务简报";
        private final Text text;

        public MessageModel(String content) {
            this.text = new Text(content);
        }
    }

    @Getter
    static class Text {
        private final String content;

        public Text(String content) {
            this.content = content.replaceAll("\r\n\r", "");
        }
    }
}
