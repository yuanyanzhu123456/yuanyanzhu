package com.geo.rcs.modules.api.annotation;

import java.lang.annotation.*;

/**
 * api接口，忽略Token验证
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/28 16:17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {

}
