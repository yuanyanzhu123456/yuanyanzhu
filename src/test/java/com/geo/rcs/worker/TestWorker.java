/*
package com.geo.rcs.worker;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.GeotmtApplicationTests;
import com.geo.rcs.common.jedis.RabbitmqConfig;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

*/
/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 11:10 2018/7/26
 *//*

public class TestWorker extends GeotmtApplicationTests {
    private static final String TASK_QUEUE_NAME1 = "Monitor-high";

    @Autowired
    private RabbitmqConfig rabbitmqConfig;

    @Test
    public void testApiTask() throws Exception {

        Message message = null;

        Channel channel = rabbitmqConfig.getChannel();

        // 指定队列持久化
        channel.queueDeclare(TASK_QUEUE_NAME1, true, false, false, null);

        String name = "许国强";
        List<String> nameList = Arrays.asList(
                "许国强", "范德萨", "徐国强", "地方撒", "打发", "发的撒", "放到", "发的的", "理解", "快回家"
        );


        HashMap<String, String> paramaters = new HashMap<String, String>();
        for (int i = 1; i < 5; i++) {

            paramaters.put("userId", "1");
            paramaters.put("dimension", "MULTIPLATE");
            paramaters.put("cid", "13405972289");
            paramaters.put("idNumber","433023197710040058");
            paramaters.put("realName", "许国强");
            paramaters.put("taskCycle", i+"");
            paramaters.put("interval", "1");
            paramaters.put("unit", "MONTH");
            paramaters.put("updateTime", "2015-06-27 00:00:00");

            message = new Message(Long.valueOf(i), paramaters);
            String beanToJson = JSON.toJSONString(message);
            // 指定消息持久化
            channel.basicPublish("", TASK_QUEUE_NAME1, MessageProperties.PERSISTENT_TEXT_PLAIN, beanToJson.getBytes());
            System.out.println(" [x] Sent '" + beanToJson + "'");
        }
        long messageCount = channel.messageCount(TASK_QUEUE_NAME1);
        System.out.println("放入队列,队列大小:" + messageCount);
        channel.close();
    }
}
*/
