package com.geo.rcs.modules.event.handler;


import com.squareup.okhttp.*;

import java.io.IOException;

public class okHttpClientCustom {

    /**
     * OkHttp客户端请求设置
     */
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * OkHttp Post 请求
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String post(String url, String token, String json) throws IOException {
        
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("token", token) // 手动填入
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }


}
