package com.geo.rcs.modules.datapool.entity;

import java.util.Date;

/**
 * 数据池模型
 * @author yongmignz
 * @crated on 2018-07-13
 */
public class Person {

    /**
     * id
     */
    private Long id;

    /**
     * 全局ID
     */
    private String rid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String cid;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
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
}
