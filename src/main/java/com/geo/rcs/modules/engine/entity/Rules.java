package com.geo.rcs.modules.engine.entity;

import java.util.List;
import java.util.Map;

public class Rules {

    private static final int AVG   = 0;

    private static final int BEST = 1;

    private static final int NO   = 0;

    private static final int YES = 1;

    private Long id;

    private String  name;

    private String  describe;

    private String  reason;

    private int matchType;

    private int  score;

    private int thresholdMin;

    private int thresholdMax;

    private int status;

    private Parameters parameters;

    private Map sourceData;

    private List<Rule> ruleList;

    public static int getAVG() {
        return AVG;
    }

    public static int getBEST() {
        return BEST;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMatchType() {
        return matchType;
    }

    public void setMatchType(int matchType) {
        this.matchType = matchType;
    }

    public int getThresholdMin() {
        return thresholdMin;
    }

    public void setThresholdMin(int thresholdMin) {
        this.thresholdMin = thresholdMin;
    }

    public int getThresholdMax() {
        return thresholdMax;
    }

    public void setThresholdMax(int thresholdMax) {
        this.thresholdMax = thresholdMax;
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    public static int getNO() {
        return NO;
    }

    public static int getYES() {
        return YES;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Map getSourceData() {
        return sourceData;
    }

    public void setSourceData(Map sourceData) {
        this.sourceData = sourceData;
    }
}
