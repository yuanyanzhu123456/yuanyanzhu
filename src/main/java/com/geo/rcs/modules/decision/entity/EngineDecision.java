package com.geo.rcs.modules.decision.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月03日 上午10:59
 */
@Entity
public class EngineDecision {

    //决策编号
    private Integer id;
    //决策名称
    @NotBlank(message="决策名称不能为空", groups = {AddGroup.class})
    private String name;
    //决策树
    @NotBlank(message="决策树不能为空", groups = {UpdateGroup.class})
    private String decisionFlow;
    //场景编号
    @NotNull(message="场景不能为空", groups = {AddGroup.class})
    private Long sceneId;
    //业务编号
    @NotNull(message="业务类型不能为空", groups = {AddGroup.class})
    private Integer businessId;
    //自身状态
    private Integer status;
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
    //客户标识
    private Long userId;
    //进件参数
    private String parameters;
    //备注
    private String remark;
    //选中的规则集编号
    private String usedRulesIds;
    //进件类型
    private String channel;
    //使用的规则集编号
    private String usageRulesIds;

    public String getUsageRulesIds() {
        return usageRulesIds;
    }

    public void setUsageRulesIds(String usageRulesIds) {
        this.usageRulesIds = usageRulesIds;
    }

    private List<ExcuteFlowForApi>  decisions;

    public List<ExcuteFlowForApi> getDecisions() {
        return decisions;
    }

    public void setDecisions(List<ExcuteFlowForApi> decisions) {
        this.decisions = decisions;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUsedRulesIds() {
        return usedRulesIds;
    }

    public void setUsedRulesIds(String usedRulesIds) {
        this.usedRulesIds = usedRulesIds;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDecisionFlow() {
        return decisionFlow;
    }

    public void setDecisionFlow(String decisionFlow) {
        this.decisionFlow = decisionFlow;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "EngineDecision{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", decisionFlow='" + decisionFlow + '\'' +
                ", sceneId=" + sceneId +
                ", businessId=" + businessId +
                ", status=" + status +
                ", appStatus=" + appStatus +
                ", activeStatus=" + activeStatus +
                ", creater='" + creater + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", userId=" + userId +
                ", parameters='" + parameters + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
