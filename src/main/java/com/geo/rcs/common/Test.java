package com.geo.rcs.common;

import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月05日 下午5:49
 */
public class Test {
    @Resource
    private static RedisTemplate redisTemplate;

    public static void main(String[] args) {

        Date date = new Date();
        String s = DatetimeFormattor.formatDateTime(date);

        System.out.println("date"+date);
        System.out.println("dateString"+date.toString());
        System.out.println("s"+s);

    }
}
