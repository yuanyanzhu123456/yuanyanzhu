package com.geo.rcs.modules.datapool.entity;

import java.util.Date;

/**
 * 数据池-用户详情表
 * @author yongmignz
 * @crated on 2018-07-13
 */
public class PersonDetail {

    /**
     * id
     */
    private Long id;

    /**
     * 全局ID
     */
    private String rid;

    /**
     * 维度
     */
    private String dimension;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date  updateTime;

    /**
     * 查询次数
     */
    private Integer queryTimes;

    /**
     * 更新次数
     */
    private Integer updateTimes;

    /**
     * 查询状态
     */
    private Integer queryStatus;

    /**
     * 更新状态
     */
    private Integer updateStatus;

    /**
     * 更新最大间隔
     */
    private Integer maxInterval;

    /**
     * 更新最小间隔
     */
    private Integer minInterval;

    /**
     * 最近数据
     */
    private String recentData;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getQueryTimes() {
        return queryTimes;
    }

    public void setQueryTimes(Integer queryTimes) {
        this.queryTimes = queryTimes;
    }

    public Integer getUpdateTimes() {
        return updateTimes;
    }

    public void setUpdateTimes(Integer updateTimes) {
        this.updateTimes = updateTimes;
    }

    public Integer getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(Integer queryStatus) {
        this.queryStatus = queryStatus;
    }

    public Integer getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(Integer updateStatus) {
        this.updateStatus = updateStatus;
    }

    public Integer getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval(Integer maxInterval) {
        this.maxInterval = maxInterval;
    }

    public Integer getMinInterval() {
        return minInterval;
    }

    public void setMinInterval(Integer minInterval) {
        this.minInterval = minInterval;
    }

    public String getRecentData() {
        return recentData;
    }

    public void setRecentData(String recentData) {
        this.recentData = recentData;
    }
}
