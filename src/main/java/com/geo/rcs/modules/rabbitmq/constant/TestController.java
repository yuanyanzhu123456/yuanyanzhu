package com.geo.rcs.modules.rabbitmq.constant;

import com.geo.rcs.modules.rabbitmq.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rabbitmq.constant
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月10日 下午4:12
 */
@RequestMapping("/test")
@RestController
public class TestController {
    @Autowired
    private RabbitMqService rabbitMqService;

    @RequestMapping("/delete")
    public void deleteQueue(){
        try {
            System.out.printf("queyeSize");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
