package com.geo.rcs.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author： wuzuqi
 * @email: wuzuqi@geotmt.com
 * @Description:
 * @Date： Created in 12:32 2018/12/8
 */

public class SysVersionLog implements Serializable {
    private Long id;
    private String name;
    private Long oldVersionId;
    private Integer times;
    private Long customerId;
    private String creater;
    private Date createTime;
    private String message;
    private Integer status;
    private Integer type;
    private Double price;
    private Long newVersionId;
    public SysVersionLog(){}
    public SysVersionLog(Long id, String name, Long oldVersionId, Integer times, Long customerId, String creater, Date createTime, String message, Integer status, Integer type, Double price, Long newVersionId) {
        this.id = id;
        this.name = name;
        this.oldVersionId = oldVersionId;
        this.times = times;
        this.customerId = customerId;
        this.creater = creater;
        this.createTime = createTime;
        this.message = message;
        this.status = status;
        this.type = type;
        this.price = price;
        this.newVersionId = newVersionId;
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

    public Long getOldVersionId() {
        return oldVersionId;
    }

    public void setOldVersionId(Long oldVersionId) {
        this.oldVersionId = oldVersionId;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getNewVersionId() {
        return newVersionId;
    }

    public void setNewVersionId(Long newVersionId) {
        this.newVersionId = newVersionId;
    }
}
