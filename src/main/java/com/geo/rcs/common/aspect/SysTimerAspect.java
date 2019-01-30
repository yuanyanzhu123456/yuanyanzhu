package com.geo.rcs.common.aspect;

import com.geo.rcs.common.exception.RcsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Redis切面处理类
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/19 18:46
 */
@Aspect
public class SysTimerAspect {

    @Around("execution(* com.geo.rcs.common.redis.RedisUtils.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if(true){
            try{
                result = point.proceed();
            }catch (Exception e){
                throw new RcsException("Redis服务异常");
            }
        }
        return result;
    }
    @Before("execution(* com.geo.rcs.common.test.TestUtil.*(..))")
    public void before(ProceedingJoinPoint point) throws Throwable {
        System.out.printf("之前");
    }
    @After("execution(* com.geo.rcs.common.test.TestUtil.*(..))")
    public void after(ProceedingJoinPoint point) throws Throwable {
        System.out.printf("之后");
    }
}
