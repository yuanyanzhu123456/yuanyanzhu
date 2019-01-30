package com.geo.rcs.modules.abtest.entity;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月13日 下午12:18
 */
public class AbScheduleTask {

    //编号
    private Long id;
    //姓名
    private String name;
    //身份证号
    private String idNumber;
    //手机号
    private String cid;
    //任务编号
    private Long jobId;
    //创建时间
    private Date createTime;
    //消费状态
    private Integer taskStatus;
    //上次执行状态
    private Integer executeStatus;
    //A上次执行状态
    private Integer executeStatusA;
    //B上次执行状态
    private Integer executeStatusB;
    //分发时间
    private Date distributeTime;
    //分发状态
    private Integer distributeStatus;
    //分发次数
    private Integer distributeNum;
    //更新时间
    private Date updateTime;
    //执行结果
    private String resultMap;
    //A执行结果
    private String resultMapA;
    //B执行结果
    private String resultMapB;
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
    //目标编号
    private Integer goalId;
    //目标编号A
    private Integer goalA;
    //目标编号B
    private Integer goalB;
    //区分Ab
    private String role;
    //参数json
    private String parmsJson;

    private String remark;

    private Long idA;

    private Long idB;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getIdA() {
        return idA;
    }

    public void setIdA(Long idA) {
        this.idA = idA;
    }

    public Long getIdB() {
        return idB;
    }

    public void setIdB(Long idB) {
        this.idB = idB;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getExecuteStatusA() {
        return executeStatusA;
    }

    public void setExecuteStatusA(Integer executeStatusA) {
        this.executeStatusA = executeStatusA;
    }

    public Integer getExecuteStatusB() {
        return executeStatusB;
    }

    public void setExecuteStatusB(Integer executeStatusB) {
        this.executeStatusB = executeStatusB;
    }

    public String getResultMapA() {
        return resultMapA;
    }

    public void setResultMapA(String resultMapA) {
        this.resultMapA = resultMapA;
    }

    public String getResultMapB() {
        return resultMapB;
    }

    public void setResultMapB(String resultMapB) {
        this.resultMapB = resultMapB;
    }

    public Integer getGoalA() {
        return goalA;
    }

    public void setGoalA(Integer goalA) {
        this.goalA = goalA;
    }

    public Integer getGoalB() {
        return goalB;
    }

    public void setGoalB(Integer goalB) {
        this.goalB = goalB;
    }

    public String getParmsJson() {
        return parmsJson;
    }

    public void setParmsJson(String parmsJson) {
        this.parmsJson = parmsJson;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Integer getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Integer executeStatus) {
        this.executeStatus = executeStatus;
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

    public Integer getDistributeNum() {
        return distributeNum;
    }

    public void setDistributeNum(Integer distributeNum) {
        this.distributeNum = distributeNum;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    @Override
    public String toString() {
        return "AbScheduleTask{" +
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
                ", tableName='" + tableName + '\'' +
                ", userId=" + userId +
                ", overCount=" + overCount +
                ", exceptionCount=" + exceptionCount +
                ", failCount=" + failCount +
                ", taskTotal=" + taskTotal +
                ", goalId=" + goalId +
                ", role='" + role + '\'' +
                ", parmsJson='" + parmsJson + '\'' +
                '}';
    }
}
