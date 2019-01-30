package com.geo.rcs.modules.abtest.entity;

import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

/**
 * Ab测试
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/10/25
 */
public class AbTest implements Serializable{

    private static final long serialVersionUID = 1L;
    /** 任务调度参数key */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";
    /** 任务编号 */
    private Long jobId;
    /** 任务名称 */
    @NotBlank(message="任务名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String jobName;
    /** A目标  */
    @NotNull
    private Integer goalA;
    /** B目标 */
    @NotNull
    private Integer goalB;
    /** 任务状态 */
    private Integer jobStatus;
    /** A占比 最小值为1 最大值100 */
    @NotNull
    @Min(value = 1, message = "最小值为1")
    @Max(value = 100, message = "最大值为100")
    private Integer scaleA;
    /** B占比 最小值为1 最大值100 */
    @NotNull
    @Min(value = 1, message = "最小值为1")
    @Max(value = 100, message = "最大值为100")
    private Integer scaleB;
    /** 进件类型 */
    private Integer eventType;
    /** 实时离线类型 */
    private Integer lineType;
    /** 规则类型 */
    private Integer ruleType;
    /** 单次、批量类型*/
    private Integer entryType;
    /** 创建时间 */
    private String   createTime;
    /** 客户编号 */
    private Long uniqueCode;
    /**  类名 */
    private String beanName;
    /** 方法名  */
    private String methodName;
    /** 表达式 */
    private String cronExpression;
    /** 进件参数 */
    private String parameters;
    /** 子任务总量 */
    private Integer taskCount;
    /** 已完成总量 */
    private Integer completedCount;

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(Integer completedCount) {
        this.completedCount = completedCount;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public static String getJobParamKey() {
        return JOB_PARAM_KEY;
    }

    public Integer getEntryType() {
        return entryType;
    }

    public void setEntryType(Integer entryType) {
        this.entryType = entryType;
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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getScaleA() {
        return scaleA;
    }

    public void setScaleA(Integer scaleA) {
        this.scaleA = scaleA;
    }

    public Integer getScaleB() {
        return scaleB;
    }

    public void setScaleB(Integer scaleB) {
        this.scaleB = scaleB;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getLineType() {
        return lineType;
    }

    public void setLineType(Integer lineType) {
        this.lineType = lineType;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    @Override
    public String toString() {
        return "AbTest{" +
                "jobId=" + jobId +
                ", jobName='" + jobName + '\'' +
                ", goalA=" + goalA +
                ", goalB=" + goalB +
                ", jobStatus=" + jobStatus +
                ", scaleA=" + scaleA +
                ", scaleB=" + scaleB +
                ", eventType=" + eventType +
                ", lineType=" + lineType +
                ", ruleType=" + ruleType +
                ", createTime=" + createTime +
                ", uniqueCode=" + uniqueCode +
                '}';
    }
}