package com.geo.rcs.modules.rule.test.entity;

import java.util.Date;

public class EngineTest {
    private Long id;

    private String ruleName;

    private String elapsedTime;

    private String alertRate;

    private String accuracy;

    private String recall;

    private String flscore;

    private Date testTime;

    private Long uniqueCode;

    private Integer pageSize;

    private Integer pageNo;

    private Date startTime;

    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime == null ? null : elapsedTime.trim();
    }

    public String getAlertRate() {
        return alertRate;
    }

    public void setAlertRate(String alertRate) {
        this.alertRate = alertRate == null ? null : alertRate.trim();
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String precision) {
        this.accuracy = precision == null ? null : precision.trim();
    }

    public String getRecall() {
        return recall;
    }

    public void setRecall(String recall) {
        this.recall = recall == null ? null : recall.trim();
    }

    public String getFlscore() {
        return flscore;
    }

    public void setFlscore(String flscore) {
        this.flscore = flscore == null ? null : flscore.trim();
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}