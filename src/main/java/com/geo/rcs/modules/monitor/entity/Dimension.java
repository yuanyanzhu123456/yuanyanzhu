package com.geo.rcs.modules.monitor.entity;

import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月23日 下午6:21
 */
public class Dimension {

    /**
     * 维度编号
     */
    private Integer id;
    /**
     * 维度编码
     */
    private String dimensionName;
    /**
     * 维度名称
     */
    @NotBlank(message = "维度名称不能为空", groups = {AddGroup.class})
    private String dimensionDesc;
    /**
     * 添加人/修改人
     */
    private String userName;
    /**
     * 所属公司唯一标识
     */
    private Long unicode;
    /**
     * geo添加公共维度,并且可使用设为0,其他情况忽略该字段
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 起始页
     */
    private Integer pageSize;
    /**
     * 行数
     */
    private Integer pageNo;
    /**
     * 接口编号
     */
    @NotNull
    private Long policyId;
    /**
     * 接口名称
     */
    private String interDesc;
    /**
     * 策略类型(0:规则集,1:决策集,2:模型,3:接口)
     */
    private Integer policyType;
    /**
     * 报警策略（复选--1:通过,2:人工审核,3:拒绝）
     */
    private String alarmPolicy;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 维度名称
     */
    private String policyName;
    /**
     * 进件参数
     */
    private String paramJson;
    /**
     * 状态（0：禁用；1：启用）
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInterDesc() {
        return interDesc;
    }

    public void setInterDesc(String interDesc) {
        this.interDesc = interDesc;
    }

    public Integer getPolicyType() {
        return policyType;
    }

    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }

    public String getAlarmPolicy() {
        return alarmPolicy;
    }

    public void setAlarmPolicy(String alarmPolicy) {
        this.alarmPolicy = alarmPolicy;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public String getDimensionDesc() {
        return dimensionDesc;
    }

    public void setDimensionDesc(String dimensionDesc) {
        this.dimensionDesc = dimensionDesc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUnicode() {
        return unicode;
    }

    public void setUnicode(Long unicode) {
        this.unicode = unicode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Override
    public String toString() {
        return "Dimension{" +
                "id=" + id +
                ", dimensionName='" + dimensionName + '\'' +
                ", dimensionDesc='" + dimensionDesc + '\'' +
                ", userName='" + userName + '\'' +
                ", unicode=" + unicode +
                ", type=" + type +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", remark='" + remark + '\'' +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", policyId=" + policyId +
                ", interDesc='" + interDesc + '\'' +
                ", policyType=" + policyType +
                ", alarmPolicy='" + alarmPolicy + '\'' +
                ", companyName='" + companyName + '\'' +
                ", policyName='" + policyName + '\'' +
                ", paramJson='" + paramJson + '\'' +
                ", status=" + status +
                '}';
    }
}
