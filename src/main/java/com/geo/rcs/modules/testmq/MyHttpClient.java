package com.geo.rcs.modules.testmq;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.testmq
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年06月06日 下午4:41
 */
public class MyHttpClient {
    /***
     * 需求：使用httpClient爬取传智播客官方网站数据
     *
     * @param args
     * @throws Exception
     * @throws
     */
    public static void main(String[] args) throws Exception {

        // 创建HttpClient对象
        HttpClient hClient = new DefaultHttpClient();

        // 设置响应时间，设置传智源码时间，设置代理服务器(不使用本机的IP爬取，以防止被服务器识别从而IP加入黑名单)
        hClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000)
                .setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000)
                .setParameter(ConnRouteParams.DEFAULT_PROXY, new HttpHost("10.111.25.35", 8020));

        // 爬虫URL大部分都是get请求，创建get请求对象
        HttpGet hget = new HttpGet("https://blog.csdn.net/axi295309066/article/details/62236178?locationNum=1&fps=1");
        // 向传智播客官方网站发送请求，获取网页源码
        HttpResponse response = hClient.execute(hget);
        // EntityUtils工具类把网页实体转换成字符串
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(content+"--------------");

    }

}
