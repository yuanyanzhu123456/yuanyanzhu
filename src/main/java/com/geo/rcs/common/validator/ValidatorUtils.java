package com.geo.rcs.common.validator;


import com.geo.rcs.common.exception.RcsException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/19 16:57
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws RcsException  校验不通过，则报RcsException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws RcsException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
        	ConstraintViolation<Object> constraint = constraintViolations.iterator().next();
            throw new RcsException(constraint.getMessage());
        }
    }

    /**
     * 用正则表达式判断字符串是否为数字（含负数）
     * @param str
     * @return
     */
    public static boolean isNumeric(Object str) {
        if(str == null){
            return false;
        }

        String regEx = "^-?[0-9]+$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(str.toString());

        return mat.find();
    }
}
