package com.geo.rcs.modules.decision.entity;


/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年08月31日 下午6:19
 */
public class FlowForApi {

    //通过指向下标
    private Long to;
    //类型(score/status/other)
    private String type;
    //运算符
    private String operator;
    //对比值
    private Integer value;
    //结果状态
    private Integer status;

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
