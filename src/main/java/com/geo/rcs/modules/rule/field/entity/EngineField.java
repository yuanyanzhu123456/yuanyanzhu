package com.geo.rcs.modules.rule.field.entity;

import java.io.Serializable;
import java.util.Date;

public class EngineField implements Serializable {
    //编号
    private Long id;
    //展示名
    private String showName;
    //数据源字段编号
    private Integer fieldId;
    //字段名称
    private String fieldName;
    //规则集编号
    private Long rulesId;
    //关联条件编号
    private Long conditionId;
    //运算关系符
    private String operator;
    //描述
    private String describ;
    //激活状态（0：未激活，1：激活）
    private Integer active;
    //审核状态
    private Integer verify;
    //添加人
    private String addUser;
    //添加时间
    private Date addTime;
    //结果
    private String value;
    //结果值描述
    private String valueDesc;
    //关联客户编号
    private Long uniqueCode;
    //字段类型名称
    private String fieldType;
    //参数
    private String parameter;
    //字段类型
    private Integer fieldTypeId;

    private Integer pageSize;

    private Integer pageNo;
    //数据源字段名称
    private String fieldRawName;
    //命中结果
    private String result;
    //字段命中数
    private String fieldHitNum;
    //字段命中率
    private String fieldHitRate;

    public String getFieldHitNum() {
        return fieldHitNum;
    }

    public void setFieldHitNum(String fieldHitNum) {
        this.fieldHitNum = fieldHitNum;
    }

    public String getFieldHitRate() {
        return fieldHitRate;
    }

    public void setFieldHitRate(String fieldHitRate) {
        this.fieldHitRate = fieldHitRate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFieldRawName() {
        return fieldRawName;
    }

    public void setFieldRawName(String fieldRawName) {
        this.fieldRawName = fieldRawName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }

    public Long getConditionId() {
        return conditionId;
    }

    public void setConditionId(Long conditionId) {
        this.conditionId = conditionId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ == null ? null : describ.trim();
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser == null ? null : addUser.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getValueDesc() {
        return valueDesc;
    }

    public void setValueDesc(String valueDesc) {
        this.valueDesc = valueDesc;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(Integer fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

}