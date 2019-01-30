package com.geo.rcs.modules.event.vo;

import com.geo.rcs.modules.engine.entity.Field;
import com.geo.rcs.modules.engine.entity.Rules;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 事件报告
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2018/1/26 10:40
 */
public class EventReport implements Serializable {
    /** 个人基本信息 */
    /** 姓名 */
    private String name;
    /** 性别 */
    private Character gender;
    /** 年龄 */
    private Integer age;
    /** 职业 */
    private String occupation;
    /** 手机 */
    private String mobile;
    /** 家庭电话 */
    private String telephone;
    /** 身份证 */
    private String idCard;
    /** 工作单位 */
    private String workUnit;
    /** 单位（公司） */
    private String company;
    /** 家庭地址 */
    private String homeAddress;
    /** 单位（公司）地址 */
    private String companyAddress;

    /** 规则集信息  **/
    private Rules engineRules;

    /** 命中规则 */
    private List<HitRule> hitRuleList;

    /** 字段数据 */
    private List<Map<String, Object>> productFieldList;


    /** 进件时间 **/
    private String addUser;

    /** 进件时间 **/
    private String addTime;

    /** 手机号实名查询 */
    private String realNameQuery;
    /** 手机号姓名验证 */
    private String nameVerification;
    /** 手机号在网时长 */
    private String onlineTime;
    /** 手机号在网状态 */
    private String networkState;

    /**个人基本信息集合*/
    private Map<String,String> paramInfo;

    /**规则字段列表*/
    private List<Field> fieldList;

    /**模型评分中接口名字和中文描述*/
    private Map<String,String> fieldDesc;

    public Map<String, String> getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(Map<String, String> fieldDesc) {
        this.fieldDesc = fieldDesc;
    }



    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }



    public Map<String, String> getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(Map<String, String> paramInfo) {
        this.paramInfo = paramInfo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public List<HitRule> getHitRuleList() {
        return hitRuleList;
    }

    public void setHitRuleList(List<HitRule> hitRuleList) {
        this.hitRuleList = hitRuleList;
    }

    public List<Map<String, Object>> getProductFieldList() {
        return productFieldList;
    }

    public void setProductFieldList(List<Map<String, Object>> productFieldList) {
        this.productFieldList = productFieldList;
    }

    public String getRealNameQuery() {
        return realNameQuery;
    }

    public void setRealNameQuery(String realNameQuery) {
        this.realNameQuery = realNameQuery;
    }

    public String getNameVerification() {
        return nameVerification;
    }

    public void setNameVerification(String nameVerification) {
        this.nameVerification = nameVerification;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getNetworkState() {
        return networkState;
    }

    public void setNetworkState(String networkState) {
        this.networkState = networkState;
    }


    public Rules getEngineRules() {
        return engineRules;
    }

    public void setEngineRules(Rules engineRules) {
        this.engineRules = engineRules;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
