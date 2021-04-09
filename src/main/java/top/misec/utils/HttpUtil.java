package top.misec.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Data
public class HttpUtil {
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
    static Verify verify = Verify.getInstance();
    private static String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_3) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54";
    private static CloseableHttpClient httpClient = null;
    private static CloseableHttpResponse httpResponse = null;

    public static JsonObject doPost(String url, JsonObject jsonObject) {
        return doPost(url, jsonObject.toString());
    }

    public static JsonObject doPost(String url, String requestBody) {
        return doPost(url, requestBody, null);
    }

    public static JsonObject doPost(String url, String requestBody, Map<String, String> headers) {
        httpClient = HttpClients.createDefault();
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
        //httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("User-Agent", userAgent);
        httpPost.setHeader("Cookie", verify.getVerify());

        if (null != headers && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.get(key));
            }
        } else {
            httpPost.setHeader("Referer", "https://www.bilibili.com/");
        }
        // 封装post请求参数

        StringEntity stringEntity = new StringEntity(requestBody, "utf-8");

        httpPost.setEntity(stringEntity);

        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            resultJson = processResult(httpResponse);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        } finally {
            httpResource(httpClient, httpResponse);
        }
        return resultJson;
    }

    public static JsonObject doGet(String url) {
        return doGet(url, new JsonObject());
    }

    private static NameValuePair getNameValuePair(Map.Entry<String, JsonElement> entry) {
        return new BasicNameValuePair(entry.getKey(), Optional.ofNullable(entry.getValue()).map(Object::toString).orElse(null));
    }

    public static NameValuePair[] getPairList(JsonObject pJson) {
        return pJson.entrySet().parallelStream().map(HttpUtil::getNameValuePair).toArray(NameValuePair[]::new);
    }

    public static JsonObject doGet(String url, JsonObject pJson) {
        // 通过址默认配置创建一个httpClient实例
        httpClient = HttpClients.createDefault();
        JsonObject resultJson = null;
        try {
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("User-Agent", userAgent);
            httpGet.setHeader("Cookie", verify.getVerify());
            for (NameValuePair pair : getPairList(pJson)) {
                httpGet.setHeader(pair.getName(), pair.getValue());
            }
            // 为httpGet实例设置配置
            httpGet.setConfig(REQUEST_CONFIG);
            // 执行get请求得到返回对象
            httpResponse = httpClient.execute(httpGet);
            resultJson = processResult(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            httpResource(httpClient, httpResponse);
        }
        return resultJson;

    }

    public static JsonObject processResult(CloseableHttpResponse httpResponse) throws IOException {
        JsonObject resultJson = null;
        if (httpResponse != null) {
            int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
            // 从响应对象中获取响应内容
            // 通过返回对象获取返回数据
            HttpEntity entity = httpResponse.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            String result = EntityUtils.toString(entity);
            resultJson = new JsonParser().parse(result).getAsJsonObject();
            switch (responseStatusCode) {
                case 200:
                    break;
                case 412:
                    log.debug(httpResponse.getStatusLine());
                    break;
                default:
            }
        }
        return resultJson;
    }


    private static void httpResource(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        if (null != response) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setUserAgent(String userAgent) {
        HttpUtil.userAgent = userAgent;
    }
}
