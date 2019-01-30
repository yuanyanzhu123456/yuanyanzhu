package com.geo.rcs.modules.event.handler;


import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.*;

import java.io.File;
import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventTestHandler extends Thread  {

    // 通过构造方法给线程名字赋值
    public EventTestHandler(String name) {
        super(name);// 给线程名字赋值
    }

    // 构造客户端(线程安全)
    public static final okHttpClient client = new okHttpClient();

    static String _input = "/Users/mingming/svnroom/rcs/rcs2-server/src/main/java/com/geo/rcs/modules/event/handler/data/";
    static String input = _input + "yingxiao.csv";
    static String inputCode = "UTF-8";


    static String output = input.replaceFirst(".csv","-")+ "result.csv";
    static String outputCode = "UTF-8";
    static List<String[]> readContent = CsvFileHandler.read(input, inputCode);
    static List<String[]> writeContent = new ArrayList<String[]>();


//    static int max = readContent.size();
    static int max = 10;  // 单次的量
    static int all = max;
    static int j = 0;  // 本批次的起点
    static int remark = j;
    static boolean detail = true;

    // 创建一个静态钥匙
    static Object ThreadKey = "TestEventKey"; //值是任意的

    /**
     * 启动规则测试
     * @return
     */

    @Override
    public void run(){

        long start = System.currentTimeMillis();

        System.out.println("启动测试"+ start);

        // 重写run方法
        while (max > 0) {

            // 这个很重要，必须使用一个锁，
            // 进去的人会把钥匙拿在手上，出来后才把钥匙拿让出来
            if (max > 0) {

                String no = "";
                String key = "";
                String name = "";
                String cid = "";
                String idNumber = "";

                synchronized (ThreadKey) {
                    long now = System.currentTimeMillis();
                    System.out.println(getName() + "任务进度" + (j-remark+1) + "/" + all + "条任务, " + "正在启动第" + (j + 1) + "条任务, " + (now - start));
                    String[] line = readContent.get(j);

                    no = String.valueOf((j+1));
                    key = line[0];
                    name = line[0];
                    cid = line[0];
                    idNumber = line[0];
                    max--;
                    j++;
                }

                    System.out.println(no + "," +cid + "," + idNumber+ "," + name);

                    String status = "";
                    String score = "";
                    String msg = "";
                    String code = "";
                    String resp = "";

                    try{
                        resp = eventClient("76", cid, idNumber,name);
                        System.out.println("返回结果：\n"+ resp);

                        msg = JSONObject.parseObject(resp).get("msg").toString();
                        code = JSONObject.parseObject(resp).get("code").toString();
                        try{
                            status = JSONObject.parseObject(resp).getJSONObject("data").get("status").toString();
                            score = JSONObject.parseObject(resp).getJSONObject("data").get("score").toString();
                        }catch (Exception e){
                            e.printStackTrace();
                            status = "";
                            score = msg;
                        }

                    }catch (Exception e ){
                        e.printStackTrace();



                        System.out.println("请求出错");
                        resp = "";
                        msg = "请求出错";
                        code = "请求出错";
                        status = "";
                        score = "";
                    }
                    if(detail){
                        resp = resp;
                    }

                    String[] content= new String[]{no, key, name,cid,idNumber,msg, code, status, score, resp };
                    writeContent.add(content);

            }

        }

        synchronized (ThreadKey) {
            if (max == 0) {

                String[] headers = {
                        "进件序号", "原始序号", "姓名", "手机号", "身份证", "消息", "状态码", "规则结果", "规则评分", "返回结果"
                };

                File outputFile = new File(output);

                if(outputFile.exists()){
                    // 如果文件存在，则建立副本
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                    Date now = new Date();
                    String nowDatetime = sdf.format(now);
                    String outputBak = output.replaceFirst(".csv","-")+ nowDatetime+".csv";
                    CsvFileHandler.write(headers, writeContent, outputBak, outputCode);
                }else{
                    CsvFileHandler.write(headers, writeContent, output, outputCode);
                }

                System.out.println("写入成功");
            }
        }

        System.out.println("结束测试");
    }

    /**
     * 进件接口客户端
     * @param rulesId 规则集ID
     * @param @Parameters 参数集合，JSONString
     * @return Response
     */
    public static String eventClient(String rulesId, String cid, String idNumber, String realName ) throws IOException{

//        String url ="http://10.111.32.144:9004/v2/api/eventin/entry";
        String url ="http://radar.geotmt.com/v2/api/eventin/entry";

        Map<String, Object> postForm = new HashMap<String, Object>();

        /* 测试样例
        postForm.put("rulesId", "10001");
        postForm.put("cid", "13306328903");
        postForm.put("idNumber", "370404196212262212");
        postForm.put("realName", "赵玉柏"); */

        postForm.put("type", 0);
        postForm.put("rulesId", rulesId);
        postForm.put("cid", cid);
        postForm.put("idNumber", idNumber);
        postForm.put("realName", realName);

        // 自定义周期，手动填入Token（安全）
        postForm.put("cycle", 720);
        String  token = "d9d24312e10c38e1c3a7a4fa47996477";

        String resp = okHttpClient.post(url, token, JSONObject.toJSONString(postForm));

        return resp;

    }


}
