package com.geo.rcs.modules.approval.entity;

import com.geo.rcs.modules.decision.entity.EngineDecision;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;

import java.util.Date;

public class Approval {
    //审批编号
    private Long id;
    //操作类型
    private Integer actionType;
    //类型名称
    @Override
	public String toString() {
		return "Approval [id=" + id + ", actionType=" + actionType + ", actionName=" + actionName + ", businessId="
				+ businessId + ", submitter=" + submitter + ", subTime=" + subTime + ", objId=" + objId + ", objName="
				+ objName + ", appStatus=" + appStatus + ", recordId=" + recordId + ", threshold=" + threshold
				+ ", description=" + description + ", scene=" + scene + ", activate=" + activate + ", remarks="
				+ remarks + ", uniqueCode=" + uniqueCode + ", pageSize=" + pageSize + ", pageNo=" + pageNo
				+ ", typeName=" + typeName + ", appPerson=" + appPerson + ", appTime=" + appTime + ", onlyId=" + onlyId
				+ ", thresholdMin=" + thresholdMin + ", thresholdMax=" + thresholdMax + ", ruleSetMap=" + ruleSetMap
				+ ", ruleSetName=" + ruleSetName + "]";
	}
    //业务类型编号
	private String actionName;

    private Integer businessId;
    //添加人
    private String submitter;
    //添加时间
    private Date subTime;
    //对象编号
    private Integer objId;
    //对象名称
    private String objName;
    //审批状态
    private Integer appStatus;
    //这里用作记录历史表的编号
    private Long recordId;
    //阈值
    private Integer threshold;
    //描述
    private String description;
    //场景名称
    private String scene;
    //激活状态（0：未激活，1：激活）
    private Integer activate;
    //备注
    private String remarks;
    //关联客户编号
    private Long uniqueCode;

    private Integer pageSize;

    private Integer pageNo;
    //类型名称
    private String typeName;
    //审批人
    private String appPerson;
    //审批时间
    private Date appTime;
    //审批来源
    private Long onlyId;
    //最小阈值
    private Integer thresholdMin;
    //最大阈值
    private Integer thresholdMax;

    private EngineRules ruleSetMap;
    //规则集名称
    private String ruleSetName;
    //审批类型(0:规则集；1：决策引擎)
    private Integer type;

    private EngineDecision engineDecision;

    public EngineDecision getEngineDecision() {
        return engineDecision;
    }

    public void setEngineDecision(EngineDecision engineDecision) {
        this.engineDecision = engineDecision;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRuleSetName() {
        return ruleSetName;
    }

    public void setRuleSetName(String ruleSetName) {
        this.ruleSetName = ruleSetName;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public EngineRules getRuleSetMap() {
        return ruleSetMap;
    }

    public void setRuleSetMap(EngineRules ruleSetMap) {
        this.ruleSetMap = ruleSetMap;
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

    public String getAppPerson() {
        return appPerson;
    }

    public void setAppPerson(String appPerson) {
        this.appPerson = appPerson;
    }

    public Date getAppTime() {
        return appTime;
    }

    public void setAppTime(Date appTime) {
        this.appTime = appTime;
    }

    public Integer getObjId() {
        return objId;
    }

    public void setObjId(Integer objId) {
        this.objId = objId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getOnlyId() {
        return onlyId;
    }

    public void setOnlyId(Long onlyId) {
        this.onlyId = onlyId;
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

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName == null ? null : actionName.trim();
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter == null ? null : submitter.trim();
    }

    public Date getSubTime() {
        return subTime;
    }

    public void setSubTime(Date subTime) {
        this.subTime = subTime;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName == null ? null : objName.trim();
    }

    public Integer getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Integer appStatus) {
        this.appStatus = appStatus;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene == null ? null : scene.trim();
    }

    public Integer getActivate() {
        return activate;
    }

    public void setActivate(Integer activate) {
        this.activate = activate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}