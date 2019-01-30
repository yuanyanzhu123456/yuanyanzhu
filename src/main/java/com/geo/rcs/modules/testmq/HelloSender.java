package com.geo.rcs.modules.testmq;

import com.geo.rcs.modules.rule.test.dao.EngineTestMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.testmq
 * @Description : 发送者
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年05月15日 下午5:47
 */
@Service
public class HelloSender  implements RabbitTemplate.ConfirmCallback{

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private EngineTestMapper engineTestMapper;

  /*  public void send(User user) {
        System.out.println("Sender object: " + user.toString());
        this.rabbitTemplate.convertAndSend("object", user);
    }*/
  public void send1() {


      String context = "hi, i am send 1";
      System.out.println("Sender : " + context);
      this.rabbitTemplate.convertAndSend("hello", context);
  }

    public void send2() {
        String context = "hi, i am send 2";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello1",  context);
    }

    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        System.out.println(" 回调id:" + correlationData);
        if (b) {
            System.out.println("消息成功消费");
        } else {
            System.out.println("消息消费失败:" + s);
        }
    }
}
