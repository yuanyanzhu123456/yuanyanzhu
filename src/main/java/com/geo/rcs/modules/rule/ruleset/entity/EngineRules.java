package com.geo.rcs.modules.rule.ruleset.entity;

import com.geo.rcs.modules.event.vo.EventStatEntry;
import com.geo.rcs.modules.rule.entity.EngineRule;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EngineRules implements Serializable {

    //编号
    private Long id;
    //名称
    private String name;
    //业务类型编号
    private Integer businessId;
    //场景编号
    private Long senceId;
    //匹配类型
    private Integer matchType;
    //阈值
    private Integer threshold;
    //最小阈值
    private Integer thresholdMin;
    //最大阈值
    private Integer thresholdMax;
    //描述
    private String describ;
    //审核时间
    private Integer verify;
    //激活状态（0：未激活，1：激活）
    private Integer active;
    //添加人
    private String addUser;
    //添加时间
    private Date addTime;

    private Integer pageSize;

    private Integer pageNo;
    //关联客户编号
    private Long uniqueCode;
    //参数
    private String parameters;
    //
    private List<EngineRule> ruleList;
    //类型名称
    private String typeName;
    //场景名称
    private String sceneName;
    //白名单过滤
    private String whiteFilter;
    //黑名单过滤
    private String blackFilter;
    //
    private EventStatEntry eventStatEntry;
    //命中结果
    private String result;
    //分数
    private Integer score;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public String getWhiteFilter() {
        return whiteFilter;
    }

    public void setWhiteFilter(String whiteFilter) {
        this.whiteFilter = whiteFilter;
    }

    public String getBlackFilter() {
        return blackFilter;
    }

    public void setBlackFilter(String blackFilter) {
        this.blackFilter = blackFilter;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<EngineRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<EngineRule> ruleList) {
        this.ruleList = ruleList;
    }


    @Override
    public String toString() {
        return "EngineRules{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", businessId=" + businessId +
                ", senceId=" + senceId +
                ", matchType=" + matchType +
                ", threshold=" + threshold +
                ", thresholdMin=" + thresholdMin +
                ", thresholdMax=" + thresholdMax +
                ", describ='" + describ + '\'' +
                ", verify=" + verify +
                ", active=" + active +
                ", addUser='" + addUser + '\'' +
                ", addTime=" + addTime +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", uniqueCode=" + uniqueCode +
                ", parameters='" + parameters + '\'' +
                ", ruleList=" + ruleList +
                ", typeName='" + typeName + '\'' +
                ", sceneName='" + sceneName + '\'' +
                ", whiteFilter='" + whiteFilter + '\'' +
                ", blackFilter='" + blackFilter + '\'' +
                ", eventStatEntry=" + eventStatEntry +
                ", result='" + result + '\'' +
                ", score=" + score +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Long getSenceId() {
        return senceId;
    }

    public void setSenceId(Long senceId) {
        this.senceId = senceId;
    }

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public Integer getThresholdMin() {
        return thresholdMin;
    }

    public void setThresholdMin(Integer thresholdMin) {
        this.thresholdMin = thresholdMin;
    }

    public Integer getThresholdMax() {
        return thresholdMax;
    }

    public void setThresholdMax(Integer thresholdMax) {
        this.thresholdMax = thresholdMax;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public EventStatEntry getEventStatEntry() {
        return eventStatEntry;
    }

    public void setEventStatEntry(EventStatEntry eventStatEntry) {
        this.eventStatEntry = eventStatEntry;
    }
}