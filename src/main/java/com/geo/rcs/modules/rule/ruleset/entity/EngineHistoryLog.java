package com.geo.rcs.modules.rule.ruleset.entity;

import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.group.Group;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年04月09日 下午8:07
 */
public class EngineHistoryLog {

    private Long id;

    private String name;

    private String rulesMap;

    private Long ruleSetId;

    private Date recordTime;

    private String describ;

    private Long uniqueCode;

    private Integer actionType;

    private Integer pageSize;

    private Integer pageNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRulesMap() {
        return rulesMap;
    }

    public void setRulesMap(String rulesMap) {
        this.rulesMap = rulesMap;
    }

    public Long getRuleSetId() {
        return ruleSetId;
    }

    public void setRuleSetId(Long ruleSetId) {
        this.ruleSetId = ruleSetId;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }
}
