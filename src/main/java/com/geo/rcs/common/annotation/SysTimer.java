package com.geo.rcs.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/19 17:24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysTimer {

	String value() default "";
}
