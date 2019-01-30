package com.geo.rcs.modules.monitor.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.modules.jobs.entity.JobRegister;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年08月14日 上午10:33
 */
public class ScheduleRegister extends JobRegister{

    private String workerName;

    private String type;

    private String id;
    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 任务状态
     * 0:等待
     * 1:繁忙
     */
    private int taskStatus;

    /**
     * worker自身状态
     * 0:注销
     * 1:开启
     * 2:暂停
     */
    private int workStatus;

    private List queneNameList;

    private long updateTime;

    private long registTime;

    private long createTime;

    private String updateTimeString;

    private String registTimeString;

    private String createTimeString;

    private String taskRole;

    private String ip;

    private String mqId;

    public ScheduleRegister (){
        this.type="SCHEDULE";

    }
    @Override
    public String getWorkerName() {
        return workerName;
    }
    @Override
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }



    @Override
    public List getQueneNameList() {
        return queneNameList;
    }
    @Override
    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }
    @Override
    public int getWorkStatus() {
        return workStatus;
    }
    @Override
    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }



    public void setQueneNameList(List queneNameList) {
        this.queneNameList = queneNameList;
    }

    @Override
    public String getTaskRole() {
        return taskRole;
    }

    public void setTaskRole(String taskRole) {
        this.taskRole = taskRole;
    }



    public String getMqId() {
        return mqId;
    }

    public void setMqId(String mqId) {
        this.mqId = mqId;
    }


    @Override
    public String toString() {
        return "JobWorker{" +
                "workerName='" + workerName + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", taskStatus=" + taskStatus +
                ", workStatus=" + workStatus +
                ", queneNameList=" + queneNameList +
                ", updateTime=" + updateTime +
                ", registTime=" + registTime +
                ", createTime=" + createTime +
                ", updateTimeString='" + updateTimeString + '\'' +
                ", registTimeString='" + registTimeString + '\'' +
                ", createTimeString='" + createTimeString + '\'' +
                ", taskRole='" + taskRole + '\'' +
                ", ip='" + ip + '\'' +
                ", mqId='" + mqId + '\'' +
                '}';
    }

    /**
     * 初始化Scheduler信息
     *
     * @param
     * @param ip
     * @param
     * @param
     * @param
     */
    public void initSchedulerInfo(String ip) {

        if (this.getId() == null) {
            this.setId(UUID.randomUUID().toString());
            /*this.setWorkerName(workerName);*/
            this.setIp(ip);
//            this.setMqId(mqid);
            this.setTaskStatus(1);
            this.setWorkStatus(1);
            this.setTaskRole("SCHEDULE");
            this.setQueneNameList(queneNameList);
            this.setCreateTime(System.currentTimeMillis());
            this.updateCreateTimeString();
            this.setWorkerName("SCHEDULE");
            this.setQueneNameList(Arrays.asList("Monitor-super", "Monitor-high"," Monitor-mid"," Monitor-low"));
        }

    }

    @Override
    public String setType() {

        return this.type;
    }

    /**
     * 生成注册Scheduler数据
     *
     * @param register
     * @return
     */
    @Override
    public String getRegisterInfo(JobRegister register) {

        if (register.getId() != null) {
            register.setUpdateTime(System.currentTimeMillis());
            register.setRegistTime(System.currentTimeMillis());
            register.updateRegistTimeString();
            register.updateUpdateTimeString();
            return JSONObject.toJSONString(register);
        } else {
            return null;
        }

    }

    /**
     * 更新注册Scheduler数据
     *
     * @param register
     * @return
     */
    @Override
    public String updateRegisterInfo(JobRegister register) {

        if (register.getId() != null) {
            register.setUpdateTime(System.currentTimeMillis());
            register.updateUpdateTimeString();

            return JSON.toJSONString(register);
        } else {
            return null;
        }

    }
}
