package com.geo.rcs.modules.rabbitmq.service;

import java.util.HashMap;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rabbitmq.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月09日 下午8:48
 */
public interface RabbitMqService {

    /** 推送消息*/
    void pushQueue(HashMap<String, Object> parmMap) throws Exception;
    /** 当前队列长度*/
    long getQueueSize(String queueName) throws Exception;
    /** 管道数量*/
    int getChannelNumber() throws Exception;
    /** 队列消费者数量*/
    long getCustomerCount(String queueName) throws Exception;
    /** 删除队列*/
    void deleteQueue(String queueName) throws Exception;

}
