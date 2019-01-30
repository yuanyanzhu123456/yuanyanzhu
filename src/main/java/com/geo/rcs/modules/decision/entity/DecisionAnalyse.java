package com.geo.rcs.modules.decision.entity;

import java.text.DecimalFormat;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.decision.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月26日 下午2:34
 */
public class DecisionAnalyse {

    /** 通过事件数 */
    private Integer passEventCount;

    /** 通过事件率 */
    private Double passEventRatio;

    /** 拒绝事件数 */
    private Integer refuseEventCount;

    /** 拒绝事件率 */
    private Double refuseEventRatio;

    /** 人工审核数 */
    private Integer manualReviewCount;

    /** 人工审核率 */
    private Double manualReviewRatio;

    /** 失效事件数 */
    private Integer invalidEventCount;

    /** 失效事件率 */
    private Double invalidEventRatio;

    /** 事件总数 */
    private Integer eventTotal;

    /** 系统审核结果*/
    private Integer appStatus;

    /** 人工审核结果*/
    private Integer manStatus;

    /** 失败事件总量*/
    private Integer failEventCount;

    /** 失败事件率 */
    private Double failEventRatio;

    public Double getFailEventRatio() {
        if(getFailEventCount() == null || getFailEventCount() == 0 || getEventTotal() == null || getEventTotal() == 0){
            return 0.0;
        }else {
            this.failEventRatio = (getFailEventCount()*1.0)/(getEventTotal()*1.0);
            return Double.valueOf(new DecimalFormat("#.00").format(failEventRatio));
        }
    }

    public void setFailEventRatio(Double failEventRatio) {
        this.failEventRatio = failEventRatio == null ? 0.0 : failEventRatio;
    }

    public Integer getFailEventCount() {
        return failEventCount;
    }

    public void setFailEventCount(Integer failEventCount) {
        this.failEventCount = failEventCount;
    }

    public Integer getPassEventCount() {
        return passEventCount == null ? 0 : passEventCount;
    }

    public void setPassEventCount(Integer passEventCount) {
        this.passEventCount = passEventCount == null ? 0 : passEventCount;
    }

    public Double getPassEventRatio() {
        if(getPassEventCount() == null || getPassEventCount() == 0 || getEventTotal() == null || getEventTotal() == 0){
            return 0.0;
        }else {
            this.passEventRatio = (getPassEventCount()*1.0)/(getEventTotal()*1.0);
            return Double.valueOf(new DecimalFormat("#.00").format(passEventRatio));
        }
    }

    public void setPassEventRatio(Double passEventRatio) {
        this.passEventRatio = passEventRatio == null ? 0.0 : passEventRatio;
    }

    public Integer getRefuseEventCount() {
        return refuseEventCount == null ? 0 : refuseEventCount;
    }

    public void setRefuseEventCount(Integer refuseEventCount) {
        this.refuseEventCount = refuseEventCount == null ? 0 : refuseEventCount;
    }

    public Double getRefuseEventRatio() {
        if(getRefuseEventCount() == null || getRefuseEventCount() == 0 || getEventTotal() == null || getEventTotal() == 0){
            return 0.0;
        }else {
            this.refuseEventRatio = (getRefuseEventCount()*1.0)/(getEventTotal()*1.0);
            return Double.valueOf(new DecimalFormat("#.00").format(refuseEventRatio));
        }
    }

    public void setRefuseEventRatio(Double refuseEventRatio) {
        this.refuseEventRatio = refuseEventRatio == null ? 0.0 : refuseEventRatio;
    }

    public Integer getManualReviewCount() {
        return manualReviewCount == null ? 0 : manualReviewCount;
    }

    public void setManualReviewCount(Integer manualReviewCount) {
        this.manualReviewCount = manualReviewCount == null ? 0 : manualReviewCount;
    }

    public Double getManualReviewRatio() {
        if(getManualReviewCount() == null || getManualReviewCount() == 0 || getEventTotal() == null || getEventTotal() == 0){
            return 0.0;
        }else {
            this.manualReviewRatio = (getManualReviewCount()*1.0)/(getEventTotal()*1.0);
            return Double.valueOf(new DecimalFormat("#.00").format(manualReviewRatio));
        }
    }

    public void setManualReviewRatio(Double manualReviewRatio) {
        this.manualReviewRatio = manualReviewRatio == null ? 0.0 : manualReviewRatio;
    }

    public Integer getInvalidEventCount() {
        return invalidEventCount == null ? 0 : invalidEventCount;
    }

    public void setInvalidEventCount(Integer invalidEventCount) {
        this.invalidEventCount = invalidEventCount == null ? 0 : invalidEventCount;
    }

    public Double getInvalidEventRatio() {
        if(getInvalidEventCount() == null || getInvalidEventCount() == 0 || getEventTotal() == null || getEventTotal() == 0){
            return 0.0;
        }else {
            this.invalidEventRatio = (getInvalidEventCount()*1.0)/(getEventTotal()*1.0);
            return Double.valueOf(new DecimalFormat("#.00").format(invalidEventRatio));
        }
    }

    public void setInvalidEventRatio(Double invalidEventRatio) {
        this.invalidEventRatio = invalidEventRatio == null ? 0.0 : invalidEventRatio;
    }
    public Integer getEventTotal() {
        return eventTotal;
    }

    public void setEventTotal(Integer eventTotal) {
        this.eventTotal = eventTotal;
    }

    public Integer getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Integer appStatus) {
        this.appStatus = appStatus;
    }

    public Integer getManStatus() {
        return manStatus;
    }

    public void setManStatus(Integer manStatus) {
        this.manStatus = manStatus;
    }
}
