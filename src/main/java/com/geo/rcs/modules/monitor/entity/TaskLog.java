package com.geo.rcs.modules.monitor.entity;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月26日 下午2:10
 */
public class TaskLog {

    //编号
    private Long id;
    //客户标识
    private Long userId;
    //名单编号
    private Long taskId;
    //姓名
    private String name;
    //身份证号
    private String idNumber;
    //手机号
    private String cid;
    //创建时间
    private Date createTime;
    //结果
    private String resultMap;
    //期数
    private Integer cycleNum;
    //页数
    private Integer pageNo;
    //每页数量
    private Integer pageSize;
    //操作人
    private String operator;
    //操作时间
    private Date operateTime;
    //描述
    private String description;
    //审核状态
    private Integer auditStatus;
    //任务编号
    private Long jobId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TaskLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", cid='" + cid + '\'' +
                ", createTime=" + createTime +
                ", resultMap='" + resultMap + '\'' +
                ", cycleNum=" + cycleNum +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", operator='" + operator + '\'' +
                ", operateTime=" + operateTime +
                ", description='" + description + '\'' +
                ", auditStatus=" + auditStatus +
                ", jobId=" + jobId +
                ", tableName='" + tableName + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                '}';
    }

    //表名
    private String tableName;
    //状态
    private Integer status;
    //备注
    private String remark;

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


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    public Integer getCycleNum() {
        return cycleNum;
    }

    public void setCycleNum(Integer cycleNum) {
        this.cycleNum = cycleNum;
    }
}
