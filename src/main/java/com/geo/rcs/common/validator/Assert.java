package com.geo.rcs.common.validator;

import com.geo.rcs.common.exception.RcsException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/19 17:28
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RcsException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RcsException(message);
        }
    }
}
