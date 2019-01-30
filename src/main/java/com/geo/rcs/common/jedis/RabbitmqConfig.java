package com.geo.rcs.common.jedis;

import com.geo.rcs.common.util.LogUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 方式一配置
 */
@Configuration
public class RabbitmqConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${geo.rabbitmq.open}")
    private boolean open;

    protected Channel channel;
    protected Connection connection;

    @Bean
    public ConnectionFactory getConnectionFactory(){

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setPort(port);
        return factory;

    }

    @Bean
    public Channel getChannel() throws Exception {
        if(open == true){
            Connection connection = getConnectionFactory().newConnection();
            Channel channel = connection.createChannel();
            return channel;
        }else{
            LogUtil.warn("初始化rabbitmq","rabbitmq设置open关闭","系统");
            return null;
        }

    }

    /**
     * 关闭channel和connection。并非必须，因为隐含是自动调用的。
     * @throws IOException
     */
    public void close() throws IOException {
        try{
            this.channel.close();
        } catch (TimeoutException ex){
            System.out.println("ex" + ex);
        }
        this.connection.close();
    }
}


