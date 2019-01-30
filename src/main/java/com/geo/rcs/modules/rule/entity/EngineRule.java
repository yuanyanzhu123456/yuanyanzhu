package com.geo.rcs.modules.rule.entity;

import com.geo.rcs.modules.rule.condition.entity.Conditions;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EngineRule implements Serializable{
    //规则编号
    private Long id;
    //规则名称
    private String name;
    //规则集编号
    private Long rulesId;
    //描述
    private String decision;
    //阈值
    private Integer threshold;
    //风险等级
    private Integer level;
    //条件数
    private Integer conditionNumber;
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
    //条件关联关系
    private String conditionRelationship;
    //关联客户编号
    private Long uniqueCode;

    private Integer pageSize;

    private Integer pageNo;
    //规则集名称
    private String rulesName;
    //规则编码
    private String ruleCode;

    //命中结果
    private String result;
    //规则命中数
    private String ruleHitNum;
    //规则命中率
    private String ruleHitRate;
    //分数
    private Integer score;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRuleHitNum() {
        return ruleHitNum;
    }

    public void setRuleHitNum(String ruleHitNum) {
        this.ruleHitNum = ruleHitNum;
    }

    public String getRuleHitRate() {
        return ruleHitRate;
    }

    public void setRuleHitRate(String ruleHitRate) {
        this.ruleHitRate = ruleHitRate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private List<Conditions> conditionsList;

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public List<Conditions> getConditionsList() {
        return conditionsList;
    }

    public void setConditionsList(List<Conditions> conditionsList) {
        this.conditionsList = conditionsList;
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

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision == null ? null : decision.trim();
    }



    public Integer getConditionNumber() {
        return conditionNumber;
    }

    public void setConditionNumber(Integer conditionNumber) {
        this.conditionNumber = conditionNumber;
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

    public String getConditionRelationship() {
        return conditionRelationship;
    }

    public void setConditionRelationship(String conditionRelationship) {
        this.conditionRelationship = conditionRelationship == null ? null : conditionRelationship.trim();
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    @Override
    public String toString() {
        return "EngineRule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rulesId=" + rulesId +
                ", decision='" + decision + '\'' +
                ", threshold=" + threshold +
                ", level=" + level +
                ", conditionNumber=" + conditionNumber +
                ", describ='" + describ + '\'' +
                ", active=" + active +
                ", verify=" + verify +
                ", addUser='" + addUser + '\'' +
                ", addTime=" + addTime +
                ", conditionRelationship='" + conditionRelationship + '\'' +
                ", uniqueCode=" + uniqueCode +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", rulesName='" + rulesName + '\'' +
                ", ruleCode='" + ruleCode + '\'' +
                ", result='" + result + '\'' +
                ", ruleHitNum='" + ruleHitNum + '\'' +
                ", ruleHitRate='" + ruleHitRate + '\'' +
                ", conditionsList=" + conditionsList +
                '}';
    }
}