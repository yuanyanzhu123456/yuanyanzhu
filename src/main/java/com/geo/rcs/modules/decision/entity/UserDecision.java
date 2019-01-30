package com.geo.rcs.modules.decision.entity;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.decision.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月10日 下午6:30
 */
public class UserDecision {

    /**
     * 编号
     */
    private Long id;
    /**
     * 客户编号
     */
    private Long userId;
    /**
     * 决策编号
     */
    private Integer decisionId;
    /**
     * 日志编号
     */
    private Long logId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(Integer decisionId) {
        this.decisionId = decisionId;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    @Override
    public String toString() {
        return "UserDecision{" +
                "id=" + id +
                ", userId=" + userId +
                ", decisionId=" + decisionId +
                ", logId=" + logId +
                '}';
    }
}
