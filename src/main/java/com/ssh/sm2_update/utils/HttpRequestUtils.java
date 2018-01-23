package com.ssh.sm2_update.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ssh.sm2_update.controler.UpdateControler;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

public class HttpRequestUtils {
    private final static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

    public static JSONObject httpPost(String url, List<NameValuePair> formParam) {
        //post请求返回结果
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        JSONObject jsonResult = null;
        HttpPost request = new HttpPost(url);
        CloseableHttpResponse httpResponse = null;
        try {
            if (formParam != null) {
                UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(formParam, "UTF-8");
                //解决中文乱码问题
                request.setEntity(encodedFormEntity);
//                request.setEntity(encodedFormEntity);
            }
            httpResponse = httpClient.execute(request);
            url = URLDecoder.decode(url, "UTF-8");
            String str = "";
            try {
                /**读取服务器返回过来的json字符串数据**/
                str = EntityUtils.toString(httpResponse.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSON.parseObject(str);
            } catch (Exception e) {
                logger.error("post请求提交失败:" + url + appendParamStr(formParam), e);
            }
            /**请求发送成功，并得到响应**/
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                logger.info("post请求提交失败,错误代码（" + httpResponse.getStatusLine().getStatusCode() + "）" + url + appendParamStr(formParam));
            }
        } catch (HttpHostConnectException ex) {
            logger.error("post请求超时 " + ex.getMessage() + ":" + url + appendParamStr(formParam));
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url + appendParamStr(formParam), e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResult;
    }

    public static String appendParamStr(List<NameValuePair> formParam) {
        if (formParam == null || formParam.size() == 0) {
            return "";
        } else {
            String paramStr = "?";
            for (int i = 0; i < formParam.size(); i++) {
                if (i < formParam.size() - 1) {
                    paramStr = paramStr + formParam.get(0) + "&";
                } else {
                    paramStr = paramStr + formParam.get(i);
                }
            }
            return paramStr;
        }
    }
}
