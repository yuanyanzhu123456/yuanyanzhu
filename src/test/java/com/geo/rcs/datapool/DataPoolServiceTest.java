/*
package com.geo.rcs.datapool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.GeotmtApplicationTests;
import com.geo.rcs.modules.datapool.service.DataPoolService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;


public class DataPoolServiceTest extends GeotmtApplicationTests {

//     初始化常量
    private static final String CODE = "code";
    private static final String SUCCSEE_CODE = "200";
    private static final String DATA = "data";
    private static final String ISPNUM = "ISPNUM";
    private static final String RSL = "RSL";
    private static final String ECL = "ECL";
    private static final String IFT = "IFT";
    private static final String RS = "RS";
    private static final String DESC = "desc";
    private static final String INTER = "inter";
    private static final String FIELD = "field";
    private static final String VALUE = "value";
    private static final String VALUEDESC = "valueDesc";

    @Autowired
    private DataPoolService dataPoolService;

    @Test
    public void testGetPersonByThreeFactor(){
        // String params = "{\"cid\":\"13306328903\",\"cycle\":\"720\",\"idNumber\":\"370404196212262212\",\"realName\":\"赵玉柏\"}";

        // 多头样本二：无数据
        // String  resp = dataPoolService.getPersonByThreeFactor("T40301", 1l, "13306328903","赵玉柏","370404196212262212");

        // 多头样本一: 有数据
        Map<String, String> paramaters = new HashMap<String, String>();

        long userId = 8l;

        paramaters.put("cid", "13405972289");
        paramaters.put("idNumber","433023197710040058");
        paramaters.put("realName", "许国强");
        paramaters.put("taskCycle", "1");
        paramaters.put("interval", "1");
        paramaters.put("unit", "MONTH");
        paramaters.put("updateTime", "2015-06-27 00:00:00");

        String resp = dataPoolService.getPersonByThreeFactor("MULTIPLATE", userId, paramaters);
        System.out.println(resp);

        if(resp!=null&&resp.length()>0){
            JSONObject respData = JSONObject.parseObject(resp);
            System.out.println("Test resp: \n"+ respData);
        }else{
            System.out.println("返回结果为空值");
        }

    }
}
*/
