package com.geo.rcs.common.excel;

import java.lang.annotation.*;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {

	String value() default "";
}
