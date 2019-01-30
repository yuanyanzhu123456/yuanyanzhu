package com.geo.rcs.modules.event.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件进件
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2018/1/15 15:25
 */
public class EventEntry implements Serializable {
    //进件编号
    private Long id;
    //访问渠道
    private String channel;
    //业务类型
    private Long businessId;
    //场景编号
    private Long senceId;
    //规则集编号
    private Long rulesId;
    //规则集名称
    private String rulesName;
    //用户编号
    private Long userId;
    //账户名称
    private String nickname;
    //真实姓名
    private String userName;
    //身份证
    private String idCard;
    //银行卡号
    private String acctNo;
    //手机号
    private String mobile;
    //审核编号
    private Long approverId;
    //审核人名称
    private String approverName;
    //系统审核时间
    private Date sysApprovalTime;
    //人工审核时间
    private Date manApprovalTime;
    //系统审核结果
    private Integer sysStatus;
    //人工审核结果
    private Integer manStatus;
    //审核状态
    private Integer status;
    //备注
    private String remark;
    //结果数据
    private String resultMap;
    //创建时间
    private Date createTime;
    //身份
    private String province;
    //城市
    private String city;
    //运营商
    private String isp;
    //类型（1：测试，0：正常）
    private Integer type;
	// 从几开始
	private Integer pageNo;
	// 每页条数
	private Integer pageSize;
	//进件耗时
    private Integer expendTime;
    //数据源耗时
    private Integer sourceTime;
    //规则引擎耗时
    private Integer engineTime;

    private Integer elseTime;

    private Integer score;

    private String startTime;

    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getEngineTime() {
        return engineTime;
    }

    public void setEngineTime(Integer engineTime) {
        this.engineTime = engineTime;
    }

    public Integer getElseTime() {
        return elseTime;
    }

    public void setElseTime(Integer elseTime) {
        this.elseTime = elseTime;
    }

    public Integer getExpendTime() {
        return expendTime;
    }

    public void setExpendTime(Integer expendTime) {
        this.expendTime = expendTime;
    }

    public Integer getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(Integer sourceTime) {
        this.sourceTime = sourceTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getSenceId() {
        return senceId;
    }

    public void setSenceId(Long senceId) {
        this.senceId = senceId;
    }

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }

    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo == null ? null : acctNo.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName == null ? null : approverName.trim();
    }

    public Date getSysApprovalTime() {
        return sysApprovalTime;
    }

    public void setSysApprovalTime(Date sysApprovalTime) {
        this.sysApprovalTime = sysApprovalTime;
    }

    public Date getManApprovalTime() {
        return manApprovalTime;
    }

    public void setManApprovalTime(Date manApprovalTime) {
        this.manApprovalTime = manApprovalTime;
    }

    public Integer getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(Integer sysStatus) {
        this.sysStatus = sysStatus;
    }

    public Integer getManStatus() {
        return manStatus;
    }

    public void setManStatus(Integer manStatus) {
        this.manStatus = manStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
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

    @Override
    public String toString() {
        return "EventEntry{" +
                "id=" + id +
                ", channel='" + channel + '\'' +
                ", businessId=" + businessId +
                ", senceId=" + senceId +
                ", rulesId=" + rulesId +
                ", rulesName='" + rulesName + '\'' +
                ", userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", userName='" + userName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", acctNo='" + acctNo + '\'' +
                ", mobile='" + mobile + '\'' +
                ", approverId=" + approverId +
                ", approverName='" + approverName + '\'' +
                ", sysApprovalTime=" + sysApprovalTime +
                ", manApprovalTime=" + manApprovalTime +
                ", sysStatus=" + sysStatus +
                ", manStatus=" + manStatus +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", resultMap='" + resultMap + '\'' +
                ", createTime=" + createTime +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", isp='" + isp + '\'' +
                ", type=" + type +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", expendTime=" + expendTime +
                ", sourceTime=" + sourceTime +
                ", engineTime=" + engineTime +
                ", elseTime=" + elseTime +
                ", score=" + score +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}