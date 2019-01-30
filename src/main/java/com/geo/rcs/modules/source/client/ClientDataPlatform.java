package com.geo.rcs.modules.source.client;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.JSONUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2019/1/4  16:43
 **/

@Component
public class ClientDataPlatform {

    /**
     * 数据平台地址
     */
    @Value("${dataSourceServer.dataplatform.url}")
    private String apiUrl;

    private static Log log = LogFactory.getLog(ClientDataPlatform.class);
    private HttpURLConnection conn;

    public String invoke(Map<String,Object> params) throws IOException {

        URL url = new URL(apiUrl);
        conn = (HttpURLConnection) url.openConnection();
        //post请求
        conn.setRequestMethod("POST");
        //长连接
        conn.setRequestProperty("Connection","Keep-Alive");
        //json请求格式
        conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
        //设定连接超时时间
        conn.setConnectTimeout(10000);
        //设置读取超时时间
        conn.setReadTimeout(10000);
        //http body中的json请求参数
        String paramsJson = (String)params.get("paramsJson");
        //doOutput开启后，可在建立连接后向服务器发送数据：应用post请求发送http body
        conn.setDoOutput(true);
        //发送requestbody请求数据
        conn.getOutputStream().write(paramsJson.getBytes());
        conn.getOutputStream().flush();
        int responseCode = conn.getResponseCode();
        if (responseCode != 200){
            log.warn("【DATAPLATFORM-INFO】"+"\t the request failed.responseCode="+responseCode);
            throw new RcsException("数据平台访问异常"+responseCode);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line=bufferedReader.readLine())!=null){
            sb.append(line).append("\n");
        }
        Map<String,Object> resultTmp = JSONUtil.jsonToMap(sb.toString().trim());
        String dataJson = "";
        if (resultTmp == null){
            throw new RcsException("数据平台未返回任何信息");
        } else if (resultTmp.get("code").equals("2000")) {
            dataJson = JSONUtil.beanToJson(resultTmp.get("data"));
        }else{
            throw new RcsException("数据平台："+resultTmp.get("msg")+"ErrorCode【"+resultTmp.get("code")+"】");
        }
       return dataJson;
    }

    public static void main(String[] args){
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> map = new HashedMap();
        map.put("indexName","INDEX_BRCB_LOGIN_MACADDR_1_HOUR_DIS_CUSTACC_NUM");
        map.put("MacAddr","08:00:20:0A:8C:6D");
        Map<String,String> map2 = new HashedMap();
        map2.put("indexName","INDEX_BRCB_LOGIN_MACADDR_1_HOUR_DIS_CUSTACC_NUM");
        map2.put("MacAddr","08:00:20:0A:8C:6D");
        list.add(map);
        list.add(map2);
        String paramsJson = JSONUtil.beanToJson(list);
        Map<String,Object> params = new HashedMap();
        params.put("paramsJson",paramsJson);

        ClientDataPlatform clientDataPlatform = new ClientDataPlatform();
        try {
            String result = clientDataPlatform.invoke(params);
            List<Map<String,Object>> listt = JSONUtil.jsonToBean(result,List.class);
            for (Object obj : listt){
                String objJson = JSON.toJSONString(obj);
                Map<String,Object> mapp = JSONUtil.jsonToMap(objJson);
                for (Map.Entry<String,Object> entry : mapp.entrySet()){
                    System.out.println("key:"+entry.getKey()+"\tvalue:"+entry.getValue());
                }
                System.out.println("*****************");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
