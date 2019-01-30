//package com.geo.rcs.modules.source.client;
//
//import com.squareup.okhttp.*;
//
//import java.io.IOException;
//
///**
// * 数据请求client wushujia
// */
//public class ClientDataSource {
//
//	public static final OkHttpClient client = new OkHttpClient();
//
//    /**
//     * OkHttp Post 请求
//     * @param url
//     * @param
//     * @return
//     * @throws IOException
//     */
//    public static String post(String url, String rulesConfig, String userId) throws IOException {
//
//        RequestBody formBody = new FormEncodingBuilder()
//                .add("rulesConfig", rulesConfig)
//                .add("userId", userId)
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(formBody)
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        return response.body().string();
//    }
//
//
//
//}
