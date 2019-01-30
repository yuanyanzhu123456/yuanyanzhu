package com.geo.rcs.modules.template.entity;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.template.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月31日 下午2:59
 */
public class TemplateLog {
    //模版日志编号
    private Integer id;
    //关联用户编号
    private Long userId;
    //关联模版编号
    private Integer modelId;
    //导入时间
    private Date importTime;
    //导入状态
    private Integer importStatus;
    //耗时
    private String expendTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }

    public Integer getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(Integer importStatus) {
        this.importStatus = importStatus;
    }

    public String getExpendTime() {
        return expendTime;
    }

    public void setExpendTime(String expendTime) {
        this.expendTime = expendTime;
    }
}
