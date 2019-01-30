package com.geo.rcs.modules.monitor.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 定时器
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
public class ScheduleJob implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// 任务调度参数key
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";
	// 任务id
	private Long jobId;
	//任务名称
	private String jobName;
	// spring bean名称
	private String beanName;
	// 方法名
	private String methodName;
	// cron表达式
	private String cronExpression;
	// 任务状态
	private Integer status;
	//优先级
	private Integer priority;
	//分组
	private String group;
	//上限
	private Integer upperLimit;
	// 备注
	private String remark;
	// 创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//客户标识
	private Long userId;
	//维度
	private Integer dimension;
	//周期数
	private Integer cycleNum;
	//监控单位
	private Integer monitorUnit;
	//已执行期数
	private Integer monitoredNum;
	//间隔
	private Integer interval;
	//命中策略
	private Integer hitPolicy;
	//报警邮箱
	private String 	alarmEmail;
	//总任务量
	private Integer jobTotal;
	//执行中任务量
	private Integer ingJobCount;
	//结束任务量
	private Integer overJobCount;
	//暂停任务量
	private Integer stopJobCount;
	//失败任务量
	private Integer failJobCount;
	//总数据量
	private Integer taskCount;
	//总报警数据量
	private Integer alarmCount;

	private Integer pageSize;

	private Integer pageNo;


	public String getAlarmEmail() {
		return alarmEmail;
	}

	public void setAlarmEmail(String alarmEmail) {
		this.alarmEmail = alarmEmail;
	}

	public Integer getHitPolicy() {
		return hitPolicy;
	}

	public void setHitPolicy(Integer hitPolicy) {
		this.hitPolicy = hitPolicy;
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

	public Integer getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(Integer taskCount) {
		this.taskCount = taskCount;
	}

	public Integer getAlarmCount() {
		return alarmCount;
	}

	public void setAlarmCount(Integer alarmCount) {
		this.alarmCount = alarmCount;
	}

	public Integer getJobTotal() {
		return jobTotal;
	}

	public void setJobTotal(Integer jobTotal) {
		this.jobTotal = jobTotal;
	}

	public Integer getIngJobCount() {
		return ingJobCount;
	}

	public void setIngJobCount(Integer ingJobCount) {
		this.ingJobCount = ingJobCount;
	}

	public Integer getOverJobCount() {
		return overJobCount;
	}

	public void setOverJobCount(Integer overJobCount) {
		this.overJobCount = overJobCount;
	}

	public Integer getStopJobCount() {
		return stopJobCount;
	}

	public void setStopJobCount(Integer stopJobCount) {
		this.stopJobCount = stopJobCount;
	}

	public Integer getFailJobCount() {
		return failJobCount;
	}

	public void setFailJobCount(Integer failJobCount) {
		this.failJobCount = failJobCount;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getMonitoredNum() {
		return monitoredNum;
	}

	public void setMonitoredNum(Integer monitoredNum) {
		this.monitoredNum = monitoredNum;
	}

	public Integer getMonitorUnit() {
		return monitorUnit;
	}

	public void setMonitorUnit(Integer monitorUnit) {
		this.monitorUnit = monitorUnit;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getDimension() {
		return dimension;
	}

	public void setDimension(Integer dimension) {
		this.dimension = dimension;
	}

	public Integer getCycleNum() {
		return cycleNum;
	}

	public void setCycleNum(Integer cycleNum) {
		this.cycleNum = cycleNum;
	}

	/**
	 * 设置：任务id
	 * @param jobId 任务id
	 */
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	/**
	 * 获取：任务id
	 * @return Long
	 */
	public Long getJobId() {
		return jobId;
	}
	
	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 设置：任务状态
	 * @param status 任务状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：任务状态
	 * @return String
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * 设置：cron表达式
	 * @param cronExpression cron表达式
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/**
	 * 获取：cron表达式
	 * @return String
	 */
	public String getCronExpression() {
		return cronExpression;
	}
	
	/**
	 * 设置：创建时间
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取：创建时间
	 * @return Date
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public static String getJobParamKey() {
		return JOB_PARAM_KEY;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Integer getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Integer upperLimit) {
		this.upperLimit = upperLimit;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
