package com.geo.rcs.modules.evestat.entity;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.evestat.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月09日 上午10:35
 */
public class EveStat {

    private Long id;

    //客户标识
    private Long uniqueCode;

    //客户名称
    private String userName;

    //统计事件
    private Date  statTime;

    //通过事件数
    private Integer passEvent;

    //拒绝事件数
    private Integer refuseEvent;

    //拒绝事件率
    private String rejectRate;

    //人工审核事件数
    private Integer laborEvent;

    //人工审核事件率
    private String laborRate;

    private Integer pageSize;

    private Integer pageNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }

    public Integer getPassEvent() {
        return passEvent;
    }

    public void setPassEvent(Integer passEvent) {
        this.passEvent = passEvent;
    }

    public Integer getRefuseEvent() {
        return refuseEvent;
    }

    public void setRefuseEvent(Integer refuseEvent) {
        this.refuseEvent = refuseEvent;
    }

    public String getRejectRate() {
        return rejectRate;
    }

    public void setRejectRate(String rejectRate) {
        this.rejectRate = rejectRate;
    }

    public Integer getLaborEvent() {
        return laborEvent;
    }

    @Override
	public String toString() {
		return "EveStat [id=" + id + ", uniqueCode=" + uniqueCode + ", userName=" + userName + ", statTime=" + statTime
				+ ", passEvent=" + passEvent + ", refuseEvent=" + refuseEvent + ", rejectRate=" + rejectRate
				+ ", laborEvent=" + laborEvent + ", laborRate=" + laborRate + ", pageSize=" + pageSize + ", pageNo="
				+ pageNo + "]";
	}

	public void setLaborEvent(Integer laborEvent) {
        this.laborEvent = laborEvent;
    }

    public String getLaborRate() {
        return laborRate;
    }

    public void setLaborRate(String laborRate) {
        this.laborRate = laborRate;
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

}
