package com.geo.rcs.modules.batchtest.batchTask.entity;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月22日 上午11:17:01 
 */
public class BatchTask {

	//主键
    @NotNull
	private Integer id;
	//测试规则集id用逗号分隔
    @NotBlank(message="规则集id上传失败,请联系管理员!", groups = {AddGroup.class})
	private String rulesId;
	//测试规则集名称,多个规则集逗号分隔
    @NotBlank(message="规则集名称上传失败,请联系管理员!", groups = {AddGroup.class})
	private String rulesName;
	//测试进件数量,一条记录测试完所有规则集算一个进件
	private Integer testCount;
	//完成数量
	private Integer  complete;
	//批次任务提交人
	private String submitUser;
	@Override
	public String toString() {
		return "BatchTask [id=" + id + ", rulesId=" + rulesId + ", rulesName=" + rulesName + ", testCount=" + testCount
				+ ", complete=" + complete + ", submitUser=" + submitUser + ", submitTimeTamp=" + submitTimeTamp
				+ ", uniqueCode=" + uniqueCode + ", timeLong=" + timeLong + ", priorityLevel=" + priorityLevel
				+ ", status=" + status + "]";
	}
	public Long getSubmitTimeTamp() {
		return submitTimeTamp;
	}
	public void setSubmitTimeTamp(Long submitTimeTamp) {
		this.submitTimeTamp = submitTimeTamp;
	}
	//批次任务提交时间
	private Long submitTimeTamp;
	//公司客户唯一标示
	private Long uniqueCode;
	//测试耗时
	private String timeLong;
	//任务优先级,默认0,越大越优先
	private Integer priorityLevel;
	//批量测试状态0,只上传了excel,1开启测试
	private Integer status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRulesId() {
		return rulesId;
	}
	public void setRulesId(String rulesId) {
		this.rulesId = rulesId;
	}
	public String getRulesName() {
		return rulesName;
	}
	public void setRulesName(String rulesName) {
		this.rulesName = rulesName;
	}
	public Integer getTestCount() {
		return testCount;
	}
	public void setTestCount(Integer testCount) {
		this.testCount = testCount;
	}
	public Integer getComplete() {
		return complete;
	}
	public void setComplete(Integer complete) {
		this.complete = complete;
	}
	public String getSubmitUser() {
		return submitUser;
	}
	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}
	
	public Long getUniqueCode() {
		return uniqueCode;
	}
	public void setUniqueCode(Long uniqueCode) {
		this.uniqueCode = uniqueCode;
	}
	public String getTimeLong() {
		return timeLong;
	}
	public void setTimeLong(String timeLong) {
		this.timeLong = timeLong;
	}
	public Integer getPriorityLevel() {
		return priorityLevel;
	}
	public void setPriorityLevel(Integer priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
