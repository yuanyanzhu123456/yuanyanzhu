package com.geo.rcs.common.test;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.test
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年08月31日 上午11:57
 */
@Component
public class TestUtil {

    @Pointcut
    public  void test(String test){
        System.out.printf("测试");
    }
}
