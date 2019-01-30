package com.geo.rcs.modules.source.client;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.JSONUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.squareup.okhttp.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2019/1/4  16:43
 **/

@Component
public class ClientDataPlatform2 {

    /**
     * 数据平台地址
     */
    @Value("${dataSourceServer.dataplatform.url}")
    private String apiUrl;

    /**
     * HTTP请求数据返回码
     */
    public static final int RESPONSE_CODE = 200;

    /**
     * RCS HTTP请求返回码KEY及CODE
     */
    public static final String RCS_CODE_KEY = "code";

    public static final String RCS_CODE = "2000";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * OkHttp Post 请求
     * @param
     * @return
     * @throws IOException
     */
    public String post(Map<String,Object> params)throws IOException {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        client.getDispatcher().setMaxRequestsPerHost(100);
        client.getDispatcher().setMaxRequests(500);

        String dataJson = null;
        String paramsJson = (String)params.get("paramsJson");

        RequestBody body = RequestBody.create(JSON, paramsJson);

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        int responseCode = response.code();
        if (responseCode != RESPONSE_CODE){
            throw new RcsException("数据平台访问异常"+responseCode);
        }
        String resp = response.body().string();
        Map<String,Object> resultTmp = JSONUtil.jsonToMap(resp);

        if (resultTmp == null){
            throw new RcsException("数据平台返回异常空值");
        } else if (resultTmp.get(RCS_CODE_KEY).equals(RCS_CODE)) {
            dataJson = JSONUtil.beanToJson(resultTmp.get("data"));
        }else{
            throw new RcsException("数据平台："+resultTmp.get("msg")+"ErrorCode"+resultTmp.get("code"));
        }

        return dataJson;
    }
}
