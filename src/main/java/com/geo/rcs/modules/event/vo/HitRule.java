package com.geo.rcs.modules.event.vo;

import java.io.Serializable;

/**
 * 命中规则
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2018/1/26 10:49
 */
public class HitRule implements Serializable {
    /** 场景 */
    private String scene;
    /** 所属规则集 */
    private String ruleSet;
    /** 规则 */
    private String rule;
    /** 条件 */
    private String condition;
    /** 规则模式 */
    private String rulePattern;
    /** 规则权重 */
    private String ruleWeight;
    /** 记录时间 */
    private String recordTime;

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(String ruleSet) {
        this.ruleSet = ruleSet;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getRulePattern() {
        return rulePattern;
    }

    public void setRulePattern(String rulePattern) {
        this.rulePattern = rulePattern;
    }

    public String getRuleWeight() {
        return ruleWeight;
    }

    public void setRuleWeight(String ruleWeight) {
        this.ruleWeight = ruleWeight;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}
