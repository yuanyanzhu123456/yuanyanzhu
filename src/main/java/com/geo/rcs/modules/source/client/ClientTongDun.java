package com.geo.rcs.modules.source.client;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClientTongDun {

    /**
     * 合作方地址
     */
	@Value("${dataSourceServer.tongdun.url}")
    private  String apiUrl;

    private static final Log log = LogFactory.getLog(ClientTongDun.class);

    /**
     * 合作方标识
     */
    @Value("${dataSourceServer.tongdun.partner_code}")
    private  String PARTNER_CODE ;

    /**
     * 合作方密钥
     */
    @Value("${dataSourceServer.tongdun.partner_key}")
    private  String PARTNER_KEY ;

    /**
     * 应用名
     */
    @Value("${dataSourceServer.tongdun.partner_app}")
    private  String PARTNER_APP;

    private HttpURLConnection conn;

    public ClientTongDunResponse invoke(Map<String, Object> params) {
                try {
                        String urlString = new StringBuilder().append(apiUrl).append("?partner_code=").append(PARTNER_CODE).append("&partner_key=").append(PARTNER_KEY).append("&app_name=").append(PARTNER_APP).toString();
                        URL url = new URL(urlString);
                        // 组织请求参数
                        StringBuilder postBody = new StringBuilder();
                        for (Map.Entry<String, Object> entry : params.entrySet()) {
                                if (entry.getValue() == null) continue;
                                postBody.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(),
                                        "utf-8")).append("&");
                        }

                        if (!params.isEmpty()) {
                                postBody.deleteCharAt(postBody.length() - 1);
                        }

                        conn = (HttpURLConnection) url.openConnection();
                        // 设置长链接
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        // 设置连接超时
                        conn.setConnectTimeout(1000);
                        // 设置读取超时
                        conn.setReadTimeout(3000);
                        // 提交参数
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.getOutputStream().write(postBody.toString().getBytes());
                        conn.getOutputStream().flush();
                        int responseCode = conn.getResponseCode();
                        if (responseCode != 200) {
                           log.warn("[BodyGuardApiInvoker] invoke failed, response status:" + responseCode);
                           return null;
                        }

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                                result.append(line).append("\n");
                        }
                        return JSON.parseObject(result.toString().trim(), ClientTongDunResponse.class);
                } catch (Exception e) {
                	if (e instanceof ConnectException ) {
        	            throw new RcsException(StatusCode.DATASOURCE_CONN_ERROR_TONGDUN.getMessage(), StatusCode.DATASOURCE_CONN_ERROR_TONGDUN.getCode());

					}
                        log.error("[BodyGuardApiInvoker] invoke throw exception, details: " + e);
                }
             return null;
        }

     public static void main(String[] args) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_name", "赵玉柏");
        params.put("account_mobile", "13306328903");
        params.put("id_number", "370404196212262212");
        
        ClientTongDunResponse bodyGuardApiResponse = new ClientTongDun().invoke(params);
        System.out.println(bodyGuardApiResponse.toString());
     }
}
