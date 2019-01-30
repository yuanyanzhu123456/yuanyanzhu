package com.geo.rcs.common.okhttp;

import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class okHttpClient {

    /**
     * OkHttp客户端请求设置
     */
    public  final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * OkHttp Post 请求
     * @param url
     * @param json 使用JSON数据请求
     * @return
     * @throws IOException
     */
    public  String postByJSON(String url, String json, String headers) throws IOException {
        
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        client.setRetryOnConnectionFailure(false);
        client.getDispatcher().setMaxRequestsPerHost(100);
        client.getDispatcher().setMaxRequests(500);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                //todo: 增加header参数的设置项目
//                .header("key", "value") // 手动填入
                .post(body)
                .build();


        Response response = client.newCall(request).execute();

        return response.body().string();
    }


    /**
     * OkHttp Post 请求
     * @param url
     * @param formBodyMap 使用FormBody数据请求
     * @return
     * @throws IOException
     */
    public  String postByForm(String url, Map<String, Object> formBodyMap, String headers) throws IOException {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = makeBuilderFromMap(formBodyMap).build();
        Request request = new Request.Builder()
                .url(url)
                //todo: 增加header参数的设置项目
//                .header("key", "value") // 手动填入
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }


    /**
     * Child Method: Map2FormBody
     * @param map
     * @return formBody
     */
    private   FormEncodingBuilder makeBuilderFromMap(final Map<String, Object> map) {
        FormEncodingBuilder formBody = new FormEncodingBuilder();
        for (final Map.Entry<String, Object> entrySet : map.entrySet()) {
            formBody.add(entrySet.getKey(), entrySet.getValue().toString());
        }
        return formBody;
    }


}
