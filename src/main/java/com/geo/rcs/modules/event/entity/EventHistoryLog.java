package com.geo.rcs.modules.event.entity;

import javax.persistence.Entity;
import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.event.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年11月09日 下午2:17
 */
@Entity
public class EventHistoryLog {

    /** 编号 **/
    private Long id;
    /** 模型编号 **/
    private Long modelId;
    /** 模型名称 **/
    private String modelName;
    /** 模型类型（0:决策；1:规则集；2:模型） **/
    private Integer modelType;
    /** 批次号 **/
    private String identify;
    /** 客户编号 **/
    private Long uniqueCode;
    /** 通过量 **/
    private Integer taskNum;
    /** 失效量 **/
    private Integer invalidNum;
    /** 通过量 **/
    private Integer passNum;
    /** 拒绝量 **/
    private Integer refuseNum;
    /** 人工审核量 **/
    private Integer artificialNum;
    /** 文件名 **/
    private String fileName;
    /** 进件时间 **/
    private Date entryTime;
    /** 任务类型（0:小批量任务；1:大批量任务） **/
    private Integer jobType;
    /** 任务进度 **/
    private Integer jobProgress;
    /** 任务状态（0:新建；1:暂停；2:终止；3:完成;4:失败） **/
    private Integer jobStatus;
    /** 页码 **/
    private Integer pageNo;
    /** 页数 **/
    private Integer pageSize;
    /** 任务数 **/
    private Integer jobCount;
    /** 成功数 **/
    private Integer successCount;
    /** 暂停数 **/
    private Integer stopCount;
    /** 终止数 **/
    private Integer abortCount;
    /** 失败数 **/
    private Integer failCount;
    /** 进行数 **/
    private Integer runningCount;
    /** 异常数 **/
    private Integer exceptCount;

    public Integer getStopCount() {
        return stopCount;
    }

    public void setStopCount(Integer stopCount) {
        this.stopCount = stopCount;
    }

    public Integer getAbortCount() {
        return abortCount;
    }

    public void setAbortCount(Integer abortCount) {
        this.abortCount = abortCount;
    }

    public Integer getJobCount() {
        return jobCount;
    }

    public void setJobCount(Integer jobCount) {
        this.jobCount = jobCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(Integer runningCount) {
        this.runningCount = runningCount;
    }

    public Integer getExceptCount() {
        return exceptCount;
    }

    public void setExceptCount(Integer exceptCount) {
        this.exceptCount = exceptCount;
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

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Integer getPassNum() {
        return passNum;
    }

    public void setPassNum(Integer passNum) {
        this.passNum = passNum;
    }

    public Integer getRefuseNum() {
        return refuseNum;
    }

    public void setRefuseNum(Integer refuseNum) {
        this.refuseNum = refuseNum;
    }

    public Integer getArtificialNum() {
        return artificialNum;
    }

    public void setArtificialNum(Integer artificialNum) {
        this.artificialNum = artificialNum;
    }

    public Integer getInvalidNum() {
        return invalidNum;
    }

    public void setInvalidNum(Integer invalidNum) {
        this.invalidNum = invalidNum;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public Integer getJobProgress() {
        return jobProgress;
    }

    public void setJobProgress(Integer jobProgress) {
        this.jobProgress = jobProgress;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    @Override
    public String toString() {
        return "EventHistoryLog{" +
                "id=" + id +
                ", modelId=" + modelId +
                ", modelName='" + modelName + '\'' +
                ", modelType=" + modelType +
                ", identify='" + identify + '\'' +
                ", uniqueCode=" + uniqueCode +
                ", taskNum=" + taskNum +
                ", invalidNum=" + invalidNum +
                ", passNum=" + passNum +
                ", refuseNum=" + refuseNum +
                ", artificialNum=" + artificialNum +
                ", fileName='" + fileName + '\'' +
                ", entryTime='" + entryTime + '\'' +
                ", jobType=" + jobType +
                ", jobProgress=" + jobProgress +
                ", jobStatus=" + jobStatus +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                '}';
    }
}
