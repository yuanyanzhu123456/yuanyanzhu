package com.geo.rcs.modules.testmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.testmq
 * @Description : 发送者
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年05月15日 下午5:47
 */
@Service
public class HelloSender2 {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(Integer i) {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context +"***********" +i);
        this.rabbitTemplate.convertAndSend("hello", context);
    }


}
