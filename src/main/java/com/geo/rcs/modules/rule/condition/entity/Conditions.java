package com.geo.rcs.modules.rule.condition.entity;

import com.geo.rcs.modules.rule.field.entity.EngineField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Conditions implements Serializable {
    //编号
    private Long id;
    //名称
    private String name;
    //关联规则编号
    private Long ruleId;
    //关联规则集编号
    private Long rulesId;
    //字段关联关系
    private String fieldRelationship;
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
    //关联客户编号
    private Long uniqueCode;

    private Integer pageSize;

    private Integer pageNo;
    //规则名称
    private String ruleName;
    //规则集名称
    private String rulesName;

    private List<EngineField> fieldList;
    //命中结果
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<EngineField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<EngineField> fieldList) {
        this.fieldList = fieldList;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }

    public String getFieldRelationship() {
        return fieldRelationship;
    }

    public void setFieldRelationship(String fieldRelationship) {
        this.fieldRelationship = fieldRelationship == null ? null : fieldRelationship.trim();
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ == null ? null : describ.trim();
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    @Override
    public String toString() {
        return "Conditions{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ruleId=" + ruleId +
                ", rulesId=" + rulesId +
                ", fieldRelationship='" + fieldRelationship + '\'' +
                ", describ='" + describ + '\'' +
                ", active=" + active +
                ", verify=" + verify +
                ", addUser='" + addUser + '\'' +
                ", addTime=" + addTime +
                ", uniqueCode=" + uniqueCode +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", ruleName='" + ruleName + '\'' +
                ", rulesName='" + rulesName + '\'' +
                ", fieldList=" + fieldList +
                ", result='" + result + '\'' +
                '}';
    }
}