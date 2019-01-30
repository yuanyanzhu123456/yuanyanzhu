package com.geo.rcs.modules.admin.taskmanger.entity;

import java.util.List;
import java.util.Map;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 11:35 2018/8/22
 */
public class TaskManger {
    //队列数量
    private Integer queueCount;
    //消费者数量
    private Integer customerCount;
    //调度者数量
    private Integer scheduerCount;
    //任务分发数量
    private Long taskScheduCount;
    //任务执行数量
    private Long taskExcuteCount;
    //吞吐量
    private Long taskThroughPut;
    //true正常,false异常报警
    private boolean status;
    //执行数量较前日变化
    private Long taskExecuteDayChange;
    //执行数量7日变化
    private Long taskExecuteWeekChange;
    //分发数量较前日变化
    private Long taskScheduDayChange;
   //  分发数量7日变化
    private Long taskScheduWeekChange;
    private List<Map<Object,Object>> taskDetailMapList;

    public Integer getQueueCount() {
        return queueCount;
    }

    @Override
    public String toString() {
        return "TaskManger{" +
                "queueCount=" + queueCount +
                ", customerCount=" + customerCount +
                ", scheduerCount=" + scheduerCount +
                ", taskScheduCount=" + taskScheduCount +
                ", taskExcuteCount=" + taskExcuteCount +
                ", taskThroughPut=" + taskThroughPut +
                ", status=" + status +
                ", taskExecuteDayChange=" + taskExecuteDayChange +
                ", taskExecuteWeekChange=" + taskExecuteWeekChange +
                ", taskScheduDayChange=" + taskScheduDayChange +
                ", taskScheduWeekChange=" + taskScheduWeekChange +
                ", taskDetailMapList=" + taskDetailMapList +
                '}';
    }

    public void setQueueCount(Integer queueCount) {
        this.queueCount = queueCount;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public Integer getScheduerCount() {
        return scheduerCount;
    }

    public void setScheduerCount(Integer scheduerCount) {
        this.scheduerCount = scheduerCount;
    }

    public Long getTaskScheduCount() {
        return taskScheduCount;
    }

    public void setTaskScheduCount(Long taskScheduCount) {
        this.taskScheduCount = taskScheduCount;
    }

    public Long getTaskExcuteCount() {
        return taskExcuteCount;
    }

    public void setTaskExcuteCount(Long taskExcuteCount) {
        this.taskExcuteCount = taskExcuteCount;
    }

    public Long getTaskThroughPut() {
        return taskThroughPut;
    }

    public void setTaskThroughPut(Long taskThroughPut) {
        this.taskThroughPut = taskThroughPut;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getTaskExecuteDayChange() {
        return taskExecuteDayChange;
    }

    public void setTaskExecuteDayChange(Long taskExecuteDayChange) {
        this.taskExecuteDayChange = taskExecuteDayChange;
    }

    public Long getTaskExecuteWeekChange() {
        return taskExecuteWeekChange;
    }

    public void setTaskExecuteWeekChange(Long taskExecuteWeekChange) {
        this.taskExecuteWeekChange = taskExecuteWeekChange;
    }

    public Long getTaskScheduDayChange() {
        return taskScheduDayChange;
    }

    public void setTaskScheduDayChange(Long taskScheduDayChange) {
        this.taskScheduDayChange = taskScheduDayChange;
    }

    public Long getTaskScheduWeekChange() {
        return taskScheduWeekChange;
    }

    public void setTaskScheduWeekChange(Long taskScheduWeekChange) {
        this.taskScheduWeekChange = taskScheduWeekChange;
    }

    public List<Map<Object,Object>> getTaskDetailMapList() {
        return taskDetailMapList;
    }

    public void setTaskDetailMapList(List<Map<Object,Object>> taskDetailMapList) {
        this.taskDetailMapList = taskDetailMapList;
    }
}
