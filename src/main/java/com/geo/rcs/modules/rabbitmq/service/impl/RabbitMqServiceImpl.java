package com.geo.rcs.modules.rabbitmq.service.impl;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.jedis.RabbitmqConfig;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.geo.rcs.modules.rabbitmq.service.RabbitMqService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rabbitmq.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月09日 下午8:49
 */
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    private RabbitmqConfig rabbitmqConfig;

    private static final String TASK_QUEUE_NAME = "task_queue";

    @Override
    public void pushQueue(HashMap<String, Object> parmMap) throws Exception {
        Message message;
        Long i = 0L;
        Channel channel = rabbitmqConfig.getChannel();
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        message = new Message(i, parmMap);
//			message = new Message(i, "com.geo.rcs.modules.rabbitmq.task.Task2", "syso", hashMap);
        String beanToJson = JSON.toJSONString(message);
        // 指定消息持久化
        channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, beanToJson.getBytes());
        System.out.println(" [x] Sent '" + beanToJson + "'");
    }


    @Override
    public void deleteQueue(String queueName) throws Exception {
        rabbitmqConfig.getChannel().queueDelete(queueName);
    }

    @Override
    public long getQueueSize(String queueName) throws Exception {

        return rabbitmqConfig.getChannel().messageCount(queueName);
    }

    @Override
    public int getChannelNumber() throws Exception { return rabbitmqConfig.getChannel().getChannelNumber();
    }

    @Override
    public long getCustomerCount(String queueName) throws Exception {
        return rabbitmqConfig.getChannel().consumerCount(queueName);
    }

}
