package com.geo.rcs.modules.engine.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.modules.engine.drools.DroolsRunner;
import com.geo.rcs.modules.engine.entity.Rules;
import com.geo.rcs.modules.engine.entity.RulesConfig;
import com.geo.rcs.modules.engine.entity.RulesEngineCode;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import org.joda.time.Days;
import org.kie.api.runtime.rule.ConsequenceException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class tester {

    public static void main(String[] args) throws Exception{

        // 规则文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();
        Date start = sdf.parse("2018-01-01 00:00:00");
        Date end = sdf.parse("2017-11-13 13:43:48");


        long diff = now.getTime() - end.getTime();

        int days = (int)(diff/(1000*60*60*24));

        System.out.println(days);
//        System.out.println(Days.daysBetween(start, end).getDays());


        //直接使用double类型数据进行运算
        System.out.println(0.05+0.01);
//使用BigDecimal的double参数的构造器
        BigDecimal bd1 = new BigDecimal(0.05);
        BigDecimal bd2 = new BigDecimal(0.01);
        System.out.println(bd1.add(bd2));
//使用BigDecimal的String参数的构造器
        BigDecimal bd0 = new BigDecimal("0.00");
        BigDecimal bd3 = new BigDecimal("0.05");
        BigDecimal bd4 = new BigDecimal("0.01");
        System.out.println(bd3.subtract(bd4));


//        double a = 1.000001;
        double a = Double.parseDouble("0.99");
        double b = Double.parseDouble("0.88");
        double c = a-b;

        System.out.println("\n"+((a-b)));
        System.out.println("\n"+((a-b)==0));
        System.out.println(b<=a);

    }



}
