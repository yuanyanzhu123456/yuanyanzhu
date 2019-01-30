package com.geo.rcs.modules.monitor.entity;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月13日 下午12:18
 */
public class ScheduleTask {

    //编号
    private Long id;
    //姓名
    private String name;
    //身份证号
    private String idNumber;
    //手机号
    private String cid;
    //任务编号
    private Integer jobId;
    //创建时间
    private Date createTime;
    //消费状态
    private Integer taskStatus;
    //上次执行状态
    private Integer executeStatus;
    //分发时间
    private Date distributeTime;
    //分发状态
    private Integer distributeStatus;
    //分发次数
    private Integer distributeNum;
    //更新时间
    private Date updateTime;
    //最新结果
    private String resultMap;
    //周期
    private String cronExpression;
    //开始时间
    private Date  startTime;
    //表名
    private String tableName;
    //客户编号
    private Long userId;
    //已完成数量
    private Integer overCount;
    //异常数量
    private Integer exceptionCount;
    //失败数量
    private Integer failCount;
    //名单总数
    private Integer taskTotal;
    //监控中数量
    private Integer monitoringCount;
    //阅读状态
    private Integer readStatus;
    //目标编号
    private Integer goalId;

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    //参数json
    private String parmsJson;
    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public Integer getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Integer executeStatus) {
        this.executeStatus = executeStatus;
    }

    @Override
    public String toString() {
        return "ScheduleTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", cid='" + cid + '\'' +
                ", jobId=" + jobId +
                ", createTime=" + createTime +
                ", taskStatus=" + taskStatus +
                ", executeStatus=" + executeStatus +
                ", distributeTime=" + distributeTime +
                ", distributeStatus=" + distributeStatus +
                ", distributeNum=" + distributeNum +
                ", updateTime=" + updateTime +
                ", resultMap='" + resultMap + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", startTime=" + startTime +
                ", tableName='" + tableName + '\'' +
                ", userId=" + userId +
                ", overCount=" + overCount +
                ", exceptionCount=" + exceptionCount +
                ", failCount=" + failCount +
                ", taskTotal=" + taskTotal +
                ", monitoringCount=" + monitoringCount +
                ", readStatus=" + readStatus +
                ", parmsJson='" + parmsJson + '\'' +
                '}';
    }



    public String getParmsJson() {
        return parmsJson;
    }

    public void setParmsJson(String parmsJson) {
        this.parmsJson = parmsJson;
    }

    public Integer getOverCount() {
        return overCount;
    }

    public void setOverCount(Integer overCount) {
        this.overCount = overCount;
    }

    public Integer getExceptionCount() {
        return exceptionCount;
    }

    public void setExceptionCount(Integer exceptionCount) {
        this.exceptionCount = exceptionCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getTaskTotal() {
        return taskTotal;
    }

    public void setTaskTotal(Integer taskTotal) {
        this.taskTotal = taskTotal;
    }

    public Integer getMonitoringCount() {
        return monitoringCount;
    }

    public void setMonitoringCount(Integer monitoringCount) {
        this.monitoringCount = monitoringCount;
    }

    public Integer getDistributeNum() {
        return distributeNum;
    }

    public void setDistributeNum(Integer distributeNum) {
        this.distributeNum = distributeNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getDistributeTime() {
        return distributeTime;
    }

    public void setDistributeTime(Date distributeTime) {
        this.distributeTime = distributeTime;
    }

    public Integer getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(Integer distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
