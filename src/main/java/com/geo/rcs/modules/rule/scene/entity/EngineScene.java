package com.geo.rcs.modules.rule.scene.entity;

import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class EngineScene {
    //场景编号
    private Long id;
    //业务类型编号
    private Integer businessId;
    //名称
    @NotBlank(message = "场景名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50, message = "场景名称长度不能超过50位",groups = {AddGroup.class,UpdateGroup.class})
    private String name;
    //描述
    private String describ;
    //审核状态
    private Integer verify;
    //激活状态（0：未激活，1：激活）
    private Integer active;
    //添加人
    private String addUser;
    //添加时间
    private Date addTime;

    private Integer pageSize;

    private Integer pageNo;
    //关联客户编号
    private Long uniqueCode;

    //业务类型名称
    private String typeName;

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
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

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescribe() {
        return describ;
    }

    public void setDescribe(String describe) {
        this.describ = describe == null ? null : describe.trim();
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser == null ? null : addUser.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

}