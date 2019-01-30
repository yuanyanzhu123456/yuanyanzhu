package com.geo.rcs.modules.rule.inter.entity;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.inter
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月15日 下午2:57
 */
public class EngineInter {

    //接口编号
    private Long id;
    //接口名称
    private String name;
    //描述
    private String describ;
    //激活状态（0：未激活，1：激活）
    private Integer active;
    //审核状态
    private Integer verify;
    //添加人
    private String addUser;
    //添加时间
    private Date  addTime;
    //转换类型
    private String parserType;
    //参数
    private String parameters;

    private Integer pageSize;

    private Integer pageNo;
    //接收类型
    private String requestType;
    //参数描述
    private String parametersDesc;

    private Integer interType;

    private Integer fieldCount;

    private String testParameters;

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTestParameters() {
        return testParameters;
    }

    public void setTestParameters(String testParameters) {
        this.testParameters = testParameters;
    }

    public Integer getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(Integer fieldCount) {
        this.fieldCount = fieldCount;
    }

    public Integer getInterType() {
        return interType;
    }

    public void setInterType(Integer interType) {
        this.interType = interType;
    }

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getParametersDesc() {
        return parametersDesc;
    }

    public void setParametersDesc(String parametersDesc) {
        this.parametersDesc = parametersDesc;
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

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
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

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getParserType() {
        return parserType;
    }

    public void setParserType(String parserType) {
        this.parserType = parserType;
    }
}
