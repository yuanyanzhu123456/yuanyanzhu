package com.geo.rcs.modules.engine.handler;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;

import java.util.Arrays;
import java.util.List;

public class RulesValidateHandler {

    private final  static List<String> BaseOperator = Arrays.asList(
            "isnull", "notnull",
            "==", "!=", ">=","<=",">","<",
            "->","->=","-<","-<=", "-==","-!="
    );

    // todo: query from database;

    public final  static List<String> NullOperator = Arrays.asList("isnull", "notnull");

    public final static List<String> intBaseOperator = Arrays.asList("==", "!=", ">=","<=",">","<");

    public final static List<String> DateBaseOperator = Arrays.asList(
            "==", "!=", ">=","<=",">","<",
            "->","->=","-<","-<=", "-==","-!="
    );

    public final static List<String> DateOperator = Arrays.asList(
            "==", "!=", ">=","<=",">","<",
            "->","->=","-<","-<=", "-==","-!="
    );

    public final static List<String> DatetimeBaseOperator = Arrays.asList(

            "==", "!=", ">=","<=",">","<", "->",
            "->","->=","-<","-<=", "-==","-!="
    );

    public final static List<String> stringBaseOperator = Arrays.asList("==", "!=");


    /**
     * 规则集配置校验：条件关系及字段关系校验
     */
    public static String relationShipValidate(String relationShip) {

        if (relationShip != null && relationShip.length() != 0) {
            return relationShip;
        } else {
            throw new RcsException( StatusCode.RULE_OPERATE_ERROR.getMessage(), StatusCode.RULE_OPERATE_ERROR.getCode());
        }
    }

    /**
     * 规则集配置校验：规则集字段运算关系，非法运算符等
     */
    public static String fieldOperateValidate(String fieldOperate) {

        if (fieldOperate != null && fieldOperate.length() != 0 && BaseOperator.contains(fieldOperate)) {
            return fieldOperate;
        } else {
            throw new RcsException( StatusCode.RULE_OPERATE_ERROR.getMessage(), StatusCode.RULE_OPERATE_ERROR.getCode());
        }
    }

    /**
     * 规则文件生成校验：条件关系及字段关系校验
     */
    public static String relationShipContentValidate(String relationShip) {

        if (relationShip != null && relationShip.length() != 0) {
            return relationShip;
        } else {
            throw new RcsException( StatusCode.RULE_OPERATE_ERROR.getMessage(), StatusCode.RULE_OPERATE_ERROR.getCode());
        }
    }

    /**
     * 规则文件生成校验：规则集字段运算关系内容不为空
     */
    public static String fieldOperateContentValidate(String fieldOperate) {

        if (fieldOperate != null && fieldOperate.length() != 0) {
            return fieldOperate;
        } else {
            throw new RcsException( StatusCode.RULE_OPERATE_ERROR.getMessage(), StatusCode.RULE_OPERATE_ERROR.getCode());
        }
    }

}