package com.geo.rcs.modules.decision.entity;

import com.alibaba.fastjson.JSON;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.decision.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月28日 下午12:33
 */
public class DecisionForApi {

    //决策编号
    private Integer id;
    //决策名称
    private String name;
    //决策树
    private Object decisionFlow;
    //场景编号
    private Long sceneId;
    //业务编号
    private Integer businessId;
    //审批状态
    private Integer appStatus;
    //激活状态
    private Integer activeStatus;
    //创建人
    private String creater;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;
    //进件参数
    private Object parameters;
    //用到的规则集编号
    private String usedRulesIds;
    //进件类型
    private String channel;
    //分数
    private Integer score;
    //执行流程
    private List<ExcuteFlowForApi> excuteFlow;
    //系统审核状态
    private Integer sysStatus;
    //进件编号
    private Long eventId;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(Integer sysStatus) {
        this.sysStatus = sysStatus;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDecisionFlow() {
        return decisionFlow;
    }

    public void setDecisionFlow(Object decisionFlow) {
        this.decisionFlow = decisionFlow;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Integer appStatus) {
        this.appStatus = appStatus;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public String getUsedRulesIds() {
        return usedRulesIds;
    }

    public void setUsedRulesIds(String usedRulesIds) {
        this.usedRulesIds = usedRulesIds;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<ExcuteFlowForApi> getExcuteFlow() {
        return excuteFlow;
    }

    public void setExcuteFlow(List<ExcuteFlowForApi> excuteFlow) {
        this.excuteFlow = excuteFlow;
    }
}
