package top.misec.Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import top.misec.Login.Verify;

import java.io.IOException;

/**
 * @author Junzhou Liu
 * @create 2020/10/11 4:03
 */
public class HttpUnit {

    static Logger logger = (Logger) LogManager.getLogger(HttpUnit.class.getName());

    private static final String pcUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36 Edg/85.0.564.70";
    // 设置配置请求参数
    private static final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
            .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
            .setSocketTimeout(60000)// 设置读取数据连接超时时间
            .build();

    static Verify verify = Verify.getInstance();

    HttpUnit() {

    }

    public static JsonObject Post(String url, String requestBody) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpPostResponse = null;

        JsonObject resultJson = null;
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例

        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头

        // httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Referer", "https://www.bilibili.com/");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("User-Agent", pcUserAgent);
        httpPost.setHeader("Cookie", verify.getVerify());

        // 封装post请求参数

        StringEntity stringEntity = new StringEntity(requestBody, "utf-8");

        httpPost.setEntity(stringEntity);

        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpPostResponse = httpClient.execute(httpPost);

            if (httpPostResponse != null && httpPostResponse.getStatusLine().getStatusCode() == 200) {
                // 从响应对象中获取响应内容
                HttpEntity entity = httpPostResponse.getEntity();
                String result = EntityUtils.toString(entity);
                resultJson = new JsonParser().parse(result).getAsJsonObject();
            } else if (httpPostResponse != null) {
                logger.debug(httpPostResponse.getStatusLine().toString());
            }

        } catch (ClientProtocolException e) {
            logger.error(e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            httpResource(httpClient, httpPostResponse);
        }
        return resultJson;
    }

    public static JsonObject Get(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpGetResponse = null;
        JsonObject resultJson = null;
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Referer", "https://www.bilibili.com/");
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("User-Agent", pcUserAgent);
            httpGet.setHeader("Cookie", verify.getVerify());
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);

            // 执行get请求得到返回对象
            httpGetResponse = httpClient.execute(httpGet);
            if (httpGetResponse != null && httpGetResponse.getStatusLine().getStatusCode() == 200) {
                // 从响应对象中获取响应内容
                // 通过返回对象获取返回数据
                HttpEntity entity = httpGetResponse.getEntity();
                // 通过EntityUtils中的toString方法将结果转换为字符串
                String result = EntityUtils.toString(entity);
                resultJson = new JsonParser().parse(result).getAsJsonObject();
            } else if (httpGetResponse != null) {
                logger.debug(httpGetResponse.getStatusLine().toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            httpResource(httpClient, httpGetResponse);
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
}
