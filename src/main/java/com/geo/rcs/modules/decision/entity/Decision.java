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
public class Decision {

    //规则集编号
    private Long rulesId;
    //自定义节点
    private Long index;
    //节点位置
    private String position;
    //通过指向下标
    private Long to;
    //类型(score/status/other)
    private String type;
    //运算符
    private String operator;
    //对比值
    private Integer value;
    //决策流
    private List<Decision> flow;
    //执行流
    private List<Integer> executeFlow;
    //结果状态
    private Integer status;
    //结果分数
    private Integer score;
    //进件编号
    private Long eventId;

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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<Integer> getExecuteFlow() {
        return executeFlow;
    }

    public void setExecuteFlow(List<Integer> executeFlow) {
        this.executeFlow = executeFlow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }

    public Long getTo() {
        return to;
    }

    public List<Decision> getFlow() {
        return flow;
    }

    public void setFlow(List<Decision> flow) {
        this.flow = flow;
    }

    @Override
    public String toString() {
        return "Decision{" +
                "rulesId=" + rulesId +
                ", index=" + index +
                ", position='" + position + '\'' +
                ", to=" + to +
                ", type='" + type + '\'' +
                ", operator='" + operator + '\'' +
                ", value=" + value +
                ", flow=" + flow +
                ", executeFlow=" + executeFlow +
                ", status=" + status +
                ", score=" + score +
                '}';
    }
}
