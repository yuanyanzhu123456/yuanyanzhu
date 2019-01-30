package com.geo.rcs.modules.admin.taskmanger.entity;

import org.apache.catalina.LifecycleState;

import java.util.List;
import java.util.Map;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 10:01 2018/8/24
 */
public class QueueDetail {
    //队列数量
    private Integer queueNum;
    //单次分发限制
    private Integer eachTimeMax;
    //单队列上限
    private Integer queueCapacity;
    private List<Map<Object,Object>> queueMapList;

    public Integer getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(Integer queueNum) {
        this.queueNum = queueNum;
    }

    public Integer getEachTimeMax() {
        return eachTimeMax;
    }

    public void setEachTimeMax(Integer eachTimeMax) {
        this.eachTimeMax = eachTimeMax;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    @Override
    public String toString() {
        return "QueueDetail{" +
                "queueNum=" + queueNum +
                ", eachTimeMax=" + eachTimeMax +
                ", queueCapacity=" + queueCapacity +
                ", queueMapList=" + queueMapList +
                '}';
    }

    public List<Map<Object, Object>> getQueueMapList() {
        return queueMapList;
    }

    public void setQueueMapList(List<Map<Object, Object>> queueMapList) {
        this.queueMapList = queueMapList;
    }
}
