package com.geo.rcs.modules.template.entity;

import java.util.Date;

/**
 * Project : rcs
 * Package Name : com.geo.rcs.modules.template
 * Description : TODD
 * Author guoyujie
 * email guoyujie@geotmt.com
 * Creation Date : 2018年03月20日 下午3:10
 */
public class RuleTemplate {
    //模版编号
    private Integer id;
    //规则集json
    private String rulesetJson;
    //导出量
    private Integer exportNum;
    //使用量
    private Integer userNum;
    //添加人
    private String addUser;
    //添加时间
    private Date addTime;
    //描述
    private String describ;

    private Integer pageNo;

    private Integer pageSize;

    private Integer type;

    private String version;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getExportNum() {
        return exportNum;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public void setExportNum(Integer exportNum) {

        this.exportNum = exportNum;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRulesetJson() {
        return rulesetJson;
    }

    public void setRulesetJson(String rulesetJson) {
        this.rulesetJson = rulesetJson;
    }

    @Override
    public String toString() {
        return "RuleTemplate{" +
                "id=" + id +
                ", rulesetJson='" + rulesetJson + '\'' +
                ", exportNum=" + exportNum +
                ", userNum=" + userNum +
                ", addUser='" + addUser + '\'' +
                ", addTime=" + addTime +
                ", describ='" + describ + '\'' +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", type=" + type +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
