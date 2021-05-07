package top.misec.push.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import top.misec.apiquery.ApiList;
import top.misec.push.AbstractPush;
import top.misec.push.model.PushMetaInfo;

/**
 * @description:
 * @author: liming
 * @create: 2021-05-06 18:10
 **/
public class WeiXinPush extends AbstractPush {
    /**
     * Push ++ 默认TOKEN长度
     */
    public static final int WEIXIN_CHANNEL_TOKEN_DEFAULT_LENGTH = 36;

    @Override
    protected String generatePushUrl(PushMetaInfo metaInfo) {
        return ApiList.weixingPush + metaInfo.getToken();
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
        private final String msgtype = "markdown";
        private final Markdown markdown;

        public MessageModel(String content) {
            this.markdown = new Markdown(content);
        }
    }

    @Getter
    static class Markdown {
        private final String content;

        public Markdown(String content) {
            this.content = content.replaceAll("\r\n\r", "");
        }
    }
}
