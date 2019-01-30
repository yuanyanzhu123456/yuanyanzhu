package com.geo.rcs.common.jedis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 方式一配置
 */
@Configuration
public class JedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.pool.max-active}")
    private int maxTotal;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public JedisPool redisPoolFactory() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setJmxEnabled(true);

        JedisPool jedisPool;

        if(password!=null && password.length()>0){
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        }else{
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        }

        return jedisPool;
    }
}


/**
 * 方式二配置
 * 默认配置需要增加 ：spring.redis.pool.block-when-exhausted
 */
//@Configuration
//public class JedisConfig {
//
//    @Bean("jedis.config")
//    public JedisPoolConfig jedisPoolConfig(//
//                                           @Value("${spring.redis.pool.min-idle}") int minIdle, //
//                                           @Value("${spring.redis.pool.max-idle}") int maxIdle, //
//                                           @Value("${spring.redis.pool.max-wait}") int maxWaitMillis, //
//                                           @Value("${spring.redis.pool.block-when-exhausted}") boolean blockWhenExhausted, //
//                                           @Value("${spring.redis.pool.max-total}") int maxTotal) {
//
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMinIdle(minIdle);
//        config.setMaxIdle(maxIdle);
//        config.setMaxWaitMillis(maxWaitMillis);
//        config.setMaxTotal(maxTotal);
//        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
//        config.setBlockWhenExhausted(blockWhenExhausted);
//        // 是否启用pool的jmx管理功能, 默认true
//        config.setJmxEnabled(true);
//        return config;
//    }
//
//    @Bean
//    public JedisPool jedisPool(@Qualifier("jedis.config") JedisPoolConfig config, //
//                               @Value("${spring.redis.host}") String host, //
//                               @Value("${spring.redis.port}") int port) {
//        System.out.println("config：");
//        System.out.println(config);
//        System.out.println(host);
//        System.out.println(port);
//
//        JedisPool jedisPool = new JedisPool(config, host, port);
//        return jedisPool;
//    }
//
//}