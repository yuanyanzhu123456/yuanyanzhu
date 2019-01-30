package com.geo.rcs.common.aspect;

import com.geo.rcs.common.exception.RcsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Redis切面处理类
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/19 18:46
 */
@Aspect
@Component
public class RedisAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    //是否开启redis缓存  true开启   false关闭
    @Value("${geo.redis.open}")
    private boolean open;

    @Around("execution(* com.geo.rcs.common.redis.RedisUtils.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if(open){
            try{
                result = point.proceed();
            }catch (Exception e){
                logger.error("redis error", e);
                throw new RcsException("Redis服务异常");
            }
        }
        return result;
    }
}
