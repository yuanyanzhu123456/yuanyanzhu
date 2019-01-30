package com.geo.rcs.common.annotation;

import java.lang.annotation.*;

/**
 * 数据过滤
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/19 17:32
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {
    /**  表的别名 */
    String tableAlias() default  "";

    /**  true：没有本组织数据权限，也能查询本人数据 */
    boolean user() default true;
}
