package com.geo.rcs.common.redis;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/20 17:32
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;
    private final static Gson gson = new Gson();

    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void deletes(String pattern) {
        Set keys = new HashSet();
        if (pattern!=null){
            keys = redisTemplate.keys(pattern);
        }
        redisTemplate.delete(keys);
    }

    public Boolean hashCheckHxists(String mapName, String field) {
        return hashOperations.hasKey(mapName, field);
    }

    public Object hashGet(String tableName, String hashKey) {
        return hashGet(tableName, hashKey, NOT_EXPIRE);
    }

    public Object hashGet(String tableName, String hashKey, long expire) {
        Object value = hashOperations.get(tableName, hashKey);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(tableName, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public Map<String, Object> hashGetAll(String key) {
        return  hashOperations.entries(key);
    }

    public Long hashIncrementLongOfHashMap(String hKey, String hashKey, Long delta) {
        return hashOperations.increment(hKey, hashKey, delta);
    }

    public Double hashIncrementDoubleOfHashMap(String hKey, String hashKey, Double delta) {
        return hashOperations.increment(hKey, hashKey, delta);
    }

    public void hashPushHashMap(String key, String hashKey, Object value) {
        hashPushHashMap(key, hashKey, value, DEFAULT_EXPIRE);
    }

    public void hashPushHashMap(String key, String hashKey, Object value, long expire) {
        hashOperations.put(key, hashKey, value);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void hashPushAllHashMap(String key, Map<String, Object> map) {
        hashPushAllHashMap(key, map, DEFAULT_EXPIRE);
    }

    public void hashPushAllHashMap(String key, Map<String, Object> map, long expire) {
        hashOperations.putAll(key, map);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public Set<String> hashGetAllHashKey(String key) {
        return hashOperations.keys(key);
    }

    public Long hashGetHashMapSize(String key) {
        return hashOperations.size(key);
    }

    public List<Object> hashGetHashAllValues(String key) {
        return hashOperations.values(key);
    }

    public Long hashDeleteHashKey(String key, Object... hashKeys) {
        return hashOperations.delete(key, hashKeys);
    }

    public void listLeftPushList(String key, String value) {
        listOperations.leftPush(key, value);
    }

    public Object listLeftPopList(String key) {
        return listOperations.leftPop(key);
    }

    public Long listSize(String key) {
        return listOperations.size(key);
    }

    public List<Object> listRangeList(String key, Long start, Long end) {
        return listOperations.range(key, start, end);
    }

    public Long listRemoveFromList(String key, long i, Object value) {
        return listOperations.remove(key, i, value);
    }

    public Object listIndexFromList(String key, long index) {
        return listOperations.index(key, index);
    }

    public void listSetValueToList(String key, long index, String value) {
        listSetValueToList(key, index, value, DEFAULT_EXPIRE);
    }

    public void listSetValueToList(String key, long index, String value, long expire) {
        listOperations.set(key, index, value);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void listTrimByRange(String key, Long start, Long end) {
        listOperations.trim(key, start, end);
    }

    public void listRightPushList(String key, String value) {
        listOperations.rightPush(key, value);
    }

    public Object listRightPopList(String key) {
        return listOperations.rightPop(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return gson.fromJson(json, clazz);
    }
}
