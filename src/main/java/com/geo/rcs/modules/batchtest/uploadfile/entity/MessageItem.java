package com.geo.rcs.modules.batchtest.uploadfile.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月18日 上午10:25:00
 */

public class MessageItem implements Comparable<MessageItem>,Serializable{
	// 消息主键
	private Integer id;

	// API接口参数
	private String apiParmMap;

	//所属批量标识
	private Integer batchId;
	// 消息状态(0尚未开始任务,1消费中(放入分发器)2消费成功,3消费失败)
	private Integer status;
	// 任务开始开始时间
	private Long startDatetamp;
	// 消费完毕时间
	private Date endDate;
	//测试的规则集id
	private String rulesId;
	//任务优先级
	private Integer priorityLevel;



	public Integer getPriorityLevel() {
		return priorityLevel;
	}
	public void setPriorityLevel(Integer priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	//备注
	private String reMark;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getApiParmMap() {
		return apiParmMap;
	}
	public void setApiParmMap(String apiParmMap) {
		this.apiParmMap = apiParmMap;
	}
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRulesId() {
		return rulesId;
	}
	
	public Long getStartDatetamp() {
		return startDatetamp;
	}
	public void setStartDatetamp(Long startDatetamp) {
		this.startDatetamp = startDatetamp;
	}
	public void setRulesId(String rulesId) {
		this.rulesId = rulesId;
	}
	public String getReMark() {
		return reMark;
	}
	public void setReMark(String reMark) {
		this.reMark = reMark;
	}

	
	@Override
	public String toString() {
		return "MessageItem [id=" + id + ", apiParmMap=" + apiParmMap + ", batchId=" + batchId + ", status=" + status
				+ ", startDatetamp=" + startDatetamp + ", endDate=" + endDate + ", rulesId=" + rulesId
				+ ", priorityLevel=" + priorityLevel + ", reMark=" + reMark + "]";
	}
	@Override
	public int compareTo(MessageItem m) {
		Integer level = m.getPriorityLevel();
		//如果优先级相同按任务开始时间先后排序
		if (this.priorityLevel.equals(level) ) {
			return this.getStartDatetamp().compareTo(m.getStartDatetamp());
		}
		return this.priorityLevel > m.getPriorityLevel() ? -1 : 1;  
	}


	
	
}
