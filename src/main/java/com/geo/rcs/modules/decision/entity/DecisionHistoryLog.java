package com.geo.rcs.modules.decision.entity;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.decision.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月18日 上午11:48
 */
public class DecisionHistoryLog {

    //编号
    private Long id;
    //决策集
    private String decisionMap;
    //决策编号
    private Integer decisionId;
    //决策名称
    private String decisionName;
    //用户编号
    private Long uniqueCode;
    //记录时间
    private Date recordTime;
    //操作类型
    private Integer actionType;
    //备注
    private String remark;

    private Integer pageSize;

    private Integer pageNo;

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

    public String getDecisionMap() {
        return decisionMap;
    }

    public void setDecisionMap(String decisionMap) {
        this.decisionMap = decisionMap;
    }

    public Integer getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(Integer decisionId) {
        this.decisionId = decisionId;
    }

    public String getDecisionName() {
        return decisionName;
    }

    public void setDecisionName(String decisionName) {
        this.decisionName = decisionName;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DecisionHistoryLog{" +
                "id=" + id +
                ", decisionMap='" + decisionMap + '\'' +
                ", decisionId=" + decisionId +
                ", decisionName='" + decisionName + '\'' +
                ", uniqueCode=" + uniqueCode +
                ", recordTime=" + recordTime +
                ", actionType=" + actionType +
                ", remark='" + remark + '\'' +
                '}';
    }
}
