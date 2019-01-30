package com.geo.rcs.modules.event.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * 事件统计 视图对象 用于展示层
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2018/1/23 17:17
 */
public class EventStatEntry implements Serializable {

    private Long id;

    /** 事件统计id */
    private Integer eventId;
    /** 场景编号 */
    private Integer senceId;

    /** 用户id */
    private Long userId;

    /** 用户name */
    private String userName;

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

    /** 失败事件数 */
    private Integer failEventCount;

    /** 失败事件率 */
    private Double failEventRatio;

    /** 事件总数 */
    private Integer eventTotal;

    /** 进件时间 */
    private Date eventTime;

    /** 省份 */
    private String province;

    /** 地市 */
    private String city;

    /** 运营商 */
    private String isp;

    /** 规则集ID */
    private Long rulesId;

    /** 结果集*/
    private String resultMap;

    /** 系统审核结果*/
    private Integer sysStatus;

    /** 人工审核结果*/
    private Integer manStatus;

    /** 进件账号*/
    private String nickName;

    public Integer getSenceId() {
        return senceId;
    }

    public void setSenceId(Integer senceId) {
        this.senceId = senceId;
    }

    public Integer getFailEventCount() {
        return failEventCount;
    }

    public void setFailEventCount(Integer failEventCount) {
        this.failEventCount = failEventCount;
    }

    public Double getFailEventRatio() {
        if(getFailEventCount() == null || getFailEventCount() == 0 || getEventTotal() == null || getEventTotal() == 0){
            return 0.0;
        }else {
            this.failEventRatio = (getFailEventCount()*1.0)/(getFailEventCount()*1.0);
            return Double.valueOf(new DecimalFormat("#.00").format(failEventRatio));
        }
    }

    public void setFailEventRatio(Double failEventRatio) {
        this.failEventRatio = failEventRatio == null ? 0.0 : failEventRatio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getManStatus() {
        return manStatus;
    }

    public void setManStatus(Integer manStatus) {
        this.manStatus = manStatus;
    }

    public Integer getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(Integer sysStatus) {
        this.sysStatus = sysStatus;
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    private List<EventStatEntry> eventStatEntryList;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return eventTotal == null ? 0 :eventTotal;
    }

    public void setEventTotal(Integer eventTotal) {
        this.eventTotal = eventTotal == null ? 0 : eventTotal;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public List<EventStatEntry> getEventStatEntryList() {
        return eventStatEntryList;
    }

    public void setEventStatEntryList(List<EventStatEntry> eventStatEntryList) {
        this.eventStatEntryList = eventStatEntryList;
    }

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }
}
