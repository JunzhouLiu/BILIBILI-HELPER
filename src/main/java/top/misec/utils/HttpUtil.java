package top.misec.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import top.misec.login.Verify;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 4:03
 */

@Slf4j
public class HttpUtil {

    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36 Edg/85.0.564.70";

    public static void setUserAgent(String userAgent) {
        USER_AGENT = userAgent;
    }

    /**
     * 设置配置请求参数
     * 设置连接主机服务超时时间
     * 设置连接请求超时时间
     * 设置读取数据连接超时时间
     */
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .setSocketTimeout(10000)
            .build();

    public static JsonObject doPost(String url, JsonObject jsonObject) {
        return doPost(url, jsonObject.toString());
    }

    public static JsonObject doPost(String url, String requestBody) {
        JsonObject resultJson = null;
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        httpPost.setConfig(REQUEST_CONFIG);
        /*
          addHeader：添加一个新的请求头字段。（一个请求头中允许有重名字段。）
          setHeader：设置一个请求头字段，有则覆盖，无则添加。
          有什么好的方式判断key1=value和{"key1":"value"}
         */
        if (requestBody.startsWith("{")) {
            //java的正则表达式咋写......
            httpPost.setHeader("Content-Type", "application/json");
        } else {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Referer", "https://www.bilibili.com/");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("User-Agent", USER_AGENT);
        httpPost.setHeader("Cookie", Verify.getInstance().toCookieVal());

        // 封装post请求参数
        StringEntity stringEntity = new StringEntity(requestBody, "utf-8");
        httpPost.setEntity(stringEntity);
        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse httpPostResponse = httpClient.execute(httpPost)) {
            // httpClient对象执行post请求,并返回响应参数对象
            int responseStatusCode = httpPostResponse.getStatusLine().getStatusCode();
            if (responseStatusCode == 200) {
                // 从响应对象中获取响应内容
                HttpEntity entity = httpPostResponse.getEntity();
                String result = EntityUtils.toString(entity);
                resultJson = new JsonParser().parse(result).getAsJsonObject();
            }
        } catch (IOException e) {
            log.error("http post fail: ", e);
        }
        return resultJson;
    }

    public static JsonObject doGet(String url) {
        return doGet(url, new JsonObject());
    }

    private static NameValuePair getNameValuePair(Map.Entry<String, JsonElement> entry) {
        return new BasicNameValuePair(entry.getKey(), Optional.ofNullable(entry.getValue()).map(Object::toString).orElse(null));
    }

    private static NameValuePair[] getPairList(JsonObject pJson) {
        return pJson.entrySet().parallelStream().map(HttpUtil::getNameValuePair).toArray(NameValuePair[]::new);
    }

    public static JsonObject doGet(String url, JsonObject pJson) {
        JsonObject resultJson = null;
        // 创建httpGet远程连接实例
        HttpGet httpGet = new HttpGet(url);
        // 设置请求头信息，鉴权
        httpGet.setHeader("Referer", "https://www.bilibili.com/");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("User-Agent", USER_AGENT);
        httpGet.setHeader("Cookie", Verify.getInstance().toCookieVal());
        for (NameValuePair pair : getPairList(pJson)) {
            httpGet.setHeader(pair.getName(), pair.getValue());
        }
        // 为httpGet实例设置配置
        httpGet.setConfig(REQUEST_CONFIG);
        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse httpGetResponse = httpClient.execute(httpGet)) {
            int responseStatusCode = httpGetResponse.getStatusLine().getStatusCode();
            if (responseStatusCode == 200) {
                HttpEntity entity = httpGetResponse.getEntity();
                String result = EntityUtils.toString(entity);
                resultJson = new JsonParser().parse(result).getAsJsonObject();
            } else if (responseStatusCode == 412) {
                log.error("出了一些问题，请在自定义配置中更换UA");
            } else {
                log.warn(httpGetResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }
}
