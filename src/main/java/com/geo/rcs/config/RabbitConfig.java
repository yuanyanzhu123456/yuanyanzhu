/*
package com.geo.rcs.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Dictionary;
import java.util.Enumeration;

*/
/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.config
 * @Description : 队列配置
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年05月15日 下午5:43
 *//*

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE   = "spring-boot-exchange";
    public static final String ROUTINGKEY = "spring-boot-routingKey";

    @Bean

    public Queue Queue() {
        return new Queue("object");
    }

    */
/*@Bean
    public Queue Queue1() {
        return new Queue("hello1");
    }*//*


   */
/* @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("localhost:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true); //必须要设置
        return connectionFactory;
    }*//*


}*/
