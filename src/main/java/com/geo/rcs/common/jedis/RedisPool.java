package com.geo.rcs.common.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;



/**
 * RedisPool By Jedis
 */

@Configuration
public class RedisPool {

    @Autowired
    private JedisPool jedisPool;

    public String get(String key) {
        Jedis jedis = this.jedisPool.getResource();
        String ret;
        try {
            ret = jedis.get(key);
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return ret;
    }

    public  boolean set(String key, String val) {

        Jedis jedis = this.jedisPool.getResource();
        try {
            return "OK".equals(jedis.set(key, val));
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }


}