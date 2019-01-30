/*
package com.geo.rcs.engine;

import com.geo.rcs.GeotmtApplicationTests;
import com.geo.rcs.common.redis.RedisKeys;
import com.geo.rcs.modules.engine.entity.RulesConfig;
import com.geo.rcs.modules.engine.service.EngineService;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RulesEngineTest extends GeotmtApplicationTests  {


    @Autowired
    private EngineService engineService;

    @Test
    public void testRuleEngine() {


        List<Long> times = new ArrayList();

        // 规则文件-测试
        String rulesConfig2 = RulesConfig.getRulesConfig();

        String start = DatetimeFormattor.now();
        System.out.println("[RCS-INFO]:START" + start);

        for(int i=0; i<3; i++) {
            try {
                long startTime =  System.currentTimeMillis();
                System.out.println("################### Start:"+ startTime);
                String Result = engineService.getRulesRes(rulesConfig2);
                System.out.println("[RCS-INFO]:RESULT" + Result);
                long endTime =  System.currentTimeMillis();
                System.out.println("################### End:"+(endTime-startTime));
                times.add((endTime-startTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 规则文件-生产
       rulesConfig2 = RulesConfig.getProdRuleConfig();

        for(int i=0; i<3; i++) {
            try {
                long startTime =  System.currentTimeMillis();
                System.out.println("################### Start:"+ startTime);
                String Result = engineService.getRulesRes(rulesConfig2);
                System.out.println("[RCS-INFO]:RESULT" + Result);
                long endTime =  System.currentTimeMillis();
                System.out.println("################### End:"+(endTime-startTime));
                times.add((endTime-startTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String end = DatetimeFormattor.now();
        System.out.println(times);
        System.out.println(String.format("[RCS-INFO]:END COST：%s -  %s",  start, end));


        int i = 1;
        for (long time : times) {
            String line = "[RuleTest No" + i+"] ";
            i++;
            for (int j = 0; j < time/20 ; j++) {
                line += "=";
            }
            line += String.valueOf(time);
            System.out.println(line);
        }

    }

}

*/
