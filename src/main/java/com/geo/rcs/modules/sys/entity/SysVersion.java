package com.geo.rcs.modules.sys.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 版本信息
 * @author wuzuqi
 * @email wuzuqi@geotmt.com
 * @date 2018/10/31
 */
public class SysVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Double price;

    private Long day;

    //激活状态（0：未激活，1：激活）
    private Integer active;

    private Long entryNum;

    private Long eruptNum;

    private Long rulesNum;

    private Long decisionNum;

    private Long modelNum;

    private Date createTime;

    private Date updateTime;

    private String creater;

    private String updater;

    private Integer pageSize;

    private Integer pageNo;

    private CusVersion cusVersion;

    private Long userNum;

    private Integer customizable;

    private Integer abStatus;

    private Integer event;

    private String service;

    private Integer usageAmount;
    public SysVersion(){}

    public SysVersion(Long id, String name, Double price, Long day, Integer active, Long entryNum, Long eruptNum, Long rulesNum, Long decisionNum, Long modelNum, Date createTime, Date updateTime, String creater, String updater, Integer pageSize, Integer pageNo, CusVersion cusVersion, Long userNum, Integer customizable, Integer abStatus, Integer event, String service, Integer usageAmount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.day = day;
        this.active = active;
        this.entryNum = entryNum;
        this.eruptNum = eruptNum;
        this.rulesNum = rulesNum;
        this.decisionNum = decisionNum;
        this.modelNum = modelNum;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.creater = creater;
        this.updater = updater;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.cusVersion = cusVersion;
        this.userNum = userNum;
        this.customizable = customizable;
        this.abStatus = abStatus;
        this.event = event;
        this.service = service;
        this.usageAmount = usageAmount;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Long getEntryNum() {
        return entryNum;
    }

    public void setEntryNum(Long entryNum) {
        this.entryNum = entryNum;
    }

    public Long getEruptNum() {
        return eruptNum;
    }

    public void setEruptNum(Long eruptNum) {
        this.eruptNum = eruptNum;
    }

    public Long getDecisionNum() {
        return decisionNum;
    }

    public void setDecisionNum(Long decisionNum) {
        this.decisionNum = decisionNum;
    }

    public Long getModelNum() {
        return modelNum;
    }

    public void setModelNum(Long modelNum) {
        this.modelNum = modelNum;
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

    public CusVersion getCusVersion() {
        return cusVersion;
    }

    public void setCusVersion(CusVersion cusVersion) {
        this.cusVersion = cusVersion;
    }

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }

    public Integer getCustomizable() {
        return customizable;
    }

    public void setCustomizable(Integer customizable) {
        this.customizable = customizable;
    }

    public Integer getAbStatus() {
        return abStatus;
    }

    public void setAbStatus(Integer abStatus) {
        this.abStatus = abStatus;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(Integer usageAmount) {
        this.usageAmount = usageAmount;
    }

    public Long getRulesNum() {
        return rulesNum;
    }

    public void setRulesNum(Long rulesNum) {
        this.rulesNum = rulesNum;
    }

    @Override
    public String toString() {
        return "SysVersion{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", day=" + day +
                ", active=" + active +
                ", entryNum=" + entryNum +
                ", eruptNum=" + eruptNum +
                ", decisionNum=" + decisionNum +
                ", modelNum=" + modelNum +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creater='" + creater + '\'' +
                ", updater='" + updater + '\'' +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", cusVersion=" + cusVersion +
                ", userNum=" + userNum +
                ", customizable=" + customizable +
                ", abStatus=" + abStatus +
                ", event=" + event +
                ", service='" + service + '\'' +
                ", usageAmount=" + usageAmount +
                '}';
    }
}
