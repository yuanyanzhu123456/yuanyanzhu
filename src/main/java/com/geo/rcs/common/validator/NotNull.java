package com.geo.rcs.common.validator;

import java.lang.annotation.*;

/**
 * @author guoyujie
 * @date 2017-12-22
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull {

	String value() default " 为空！";

	RequestType type() default RequestType.NEW_UPDATE;

	/**
	 * @author guoyujie
	 * @date 2017-12-22
	 * @version 1.0
	 */
    enum RequestType {
		NEW, UPDATE, DELETE, NEW_UPDATE, UPDATE_DELETE
    }
}
