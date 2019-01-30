package com.geo.rcs.modules.jobs.entity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 14:25 2018/8/10
 */
public abstract class JobRegister {
    //类型
    private String type;
    //ID
    private String id;
    //更新时间
    private long updateTime;
    //注册时间
    private long registTime;
    //创建时间
    private long createTime;

    private String updateTimeString;

    private String registTimeString;

    private String createTimeString;

    //每页条数
    private Integer pageSize;
    //从几开始
    private Integer pageNo;

    public void setType(String type) {
        this.type = type;
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

    private String ip;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getRegistTime() {
        return registTime;
    }

    public void setRegistTime(long registTime) {
        this.registTime = registTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTimeString() {
        return updateTimeString;
    }

    public void setUpdateTimeString(String updateTimeString) {
        this.updateTimeString = updateTimeString;
    }

    public String getRegistTimeString() {
        return registTimeString;
    }

    public void setRegistTimeString(String registTimeString) {
        this.registTimeString = registTimeString;
    }

    public String getCreateTimeString() {
        return createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void updateRegistTimeString() {
        this.registTimeString = this.time2String(this.registTime);
    }

    public void updateCreateTimeString() {
        this.createTimeString = this.time2String(this.createTime);
    }
    public void updateUpdateTimeString() {
        this.updateTimeString = this.time2String(this.updateTime);
    }


    @Override
    public String toString() {
        return "JobRegister{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", updateTime=" + updateTime +
                ", registTime=" + registTime +
                ", createTime=" + createTime +
                ", updateTimeString='" + updateTimeString + '\'' +
                ", registTimeString='" + registTimeString + '\'' +
                ", createTimeString='" + createTimeString + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }


    /**
     * 工具方法: time String formattor
     *
     * @param rawtime
     * @return timeString
     */
    public String time2String(long rawtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String timeString = sdf.format(rawtime);
        return timeString;
    }

    /**
     * 工具方法: time String formattor
     *
     * @param rawtime
     * @return timeString
     */
    public String time2String(long rawtime, Boolean dateOnly) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timeString = sdf.format(rawtime);
        return timeString;
    }

    public abstract void setWorkerName(String workerName);
    /**
     * 设置注册类型是worker还是scheduler
     *
     * @return
     */
    public abstract String setType();
    /**
     * 获取注册类型是worker还是scheduler
     *
     * @return
     */
    public abstract String getType();
    /**
     * 设设置状态
     *
     * @return
     */
    public abstract int getTaskStatus();
    /**
     * 设设置状态
     *
     * @return
     */
    public abstract int getWorkStatus();
    /**
     * 设设置状态
     *
     * @return
     */
    public abstract String getWorkerName();
    /**
     * 设设置状态
     *
     * @return
     */
    public abstract String getTaskRole();
    /**
     * 设设置状态
     *
     * @return
     */
    public abstract void setWorkStatus(int status);
    /**
     * 设设置状态
     *
     * @return
     */
    public abstract List getQueneNameList();

    /**
     * 生成注册数据
     * @param register
     * @return
     */
    public abstract String getRegisterInfo(JobRegister register);



    /**
     * 更新注册Worker数据
     *
     * @param register
     * @return
     */
    public abstract String updateRegisterInfo(JobRegister register);
}
