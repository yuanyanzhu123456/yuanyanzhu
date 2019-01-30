package com.geo.rcs.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by geo on 2018/11/4.
 */
public class CusVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String creater;

    private String updater;

    private Date createTime;

    private Date expireTime;

    private Date updateTime;

    private Long versionId;

    private Long customerId;

    private List<SysVersion> sysVersions;

    public CusVersion(){}

    public CusVersion(Long id, String creater, String updater, Date createTime, Date expireTime, Date updateTime, Long versionId, Long customerId, List<SysVersion> sysVersions) {
        this.id = id;
        this.creater = creater;
        this.updater = updater;
        this.createTime = createTime;
        this.expireTime = expireTime;
        this.updateTime = updateTime;
        this.versionId = versionId;
        this.customerId = customerId;
        this.sysVersions = sysVersions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<SysVersion> getSysVersions() {
        return sysVersions;
    }

    public void setSysVersions(List<SysVersion> sysVersions) {
        this.sysVersions = sysVersions;
    }
}
