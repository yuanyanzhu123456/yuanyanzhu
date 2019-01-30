package com.geo.rcs.modules.decision.entity;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年08月31日 下午6:19
 */
public class ExcuteFlowForApi {

    //规则集编号
    private Long rulesId;
    //自定义节点
    private Long index;
    //节点位置
    private String position;
    //决策流
    private List<FlowForApi> flow;
    //结果状态
    private Integer status;
    //进件编号
    private Long eventId;
    //分数
    private Integer score;
    private Long to;

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public List<FlowForApi> getFlow() {
        return flow;
    }

    public void setFlow(List<FlowForApi> flow) {
        this.flow = flow;
    }
}
