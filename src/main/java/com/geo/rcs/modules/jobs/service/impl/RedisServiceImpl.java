package com.geo.rcs.modules.jobs.service.impl;

import com.geo.rcs.modules.jobs.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {

    // 此处直接注入即可
    @Autowired
    private JedisPool jedisPool;
    private Jedis jedis;
    @Override
    public Jedis getJedis() {
        return jedis==null?this.jedisPool.getResource():jedis;
    }

    @Override
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

    @Override
    public boolean set(String key, String val) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            return "OK".equals(jedis.set(key, val));
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }


    @Override
    public String hget(String table, String key){
        Jedis jedis = this.jedisPool.getResource();

        String res;
        try {
            res = jedis.hget(table, key);
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return res;
    }

    @Override
    public boolean hset(String table, String key, String value){
        Jedis jedis = this.jedisPool.getResource();
        try {
            return "OK".equals(jedis.hset(table, key, value));
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public boolean hdel(String table, String key){
        Jedis jedis = this.jedisPool.getResource();
        try {
            return "OK".equals(jedis.hdel(table, key));
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Map hgetAll(String table){
        Jedis jedis = this.jedisPool.getResource();
        Map res;
        try {
            res = jedis.hgetAll(table);
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return res;
    }

}