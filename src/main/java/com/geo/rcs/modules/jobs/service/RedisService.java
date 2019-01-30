package com.geo.rcs.modules.jobs.service;

import redis.clients.jedis.Jedis;

import java.util.Map;

public interface RedisService {

    String get(String key);

    boolean set(String key, String val);

    String hget(String table, String key);

    boolean hset(String table, String key, String value);

    boolean hdel(String table, String key);

    Map hgetAll(String table);

    Jedis getJedis();


}