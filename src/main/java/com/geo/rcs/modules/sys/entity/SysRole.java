package com.geo.rcs.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统角色实体类;
 *
 * @version v.0.1
 */
//@RuleTemplate
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;
    //@Id
    //@GeneratedValue
    private Long id;

    private String encode;

    private String roleName;

    private Integer pid;

    private Integer status;

    private Date createTime;

    private String creater;

    private String description;

    private Integer type;

    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:

    private Integer pageSize;

    private Integer pageNo;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    //角色 -- 权限关系：多对多关系;
    //@ManyToMany(fetch=FetchType.EAGER)
    //@JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="permissionId")})
    private List<SysPermission> permissions;

    // 用户 - 角色关系定义;
    // @ManyToMany
    // @JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="uid")})
    private List<SysUser> userInfos;// 一个角色对应多个用户


    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public List<SysUser> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<SysUser> userInfos) {
        this.userInfos = userInfos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode == null ? null : encode.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "id=" + id +
                ", encode='" + encode + '\'' +
                ", roleName='" + roleName + '\'' +
                ", pid=" + pid +
                ", status=" + status +
                ", createTime=" + createTime +
                ", creater='" + creater + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", role='" + role + '\'' +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", userId=" + userId +
                ", permissions=" + permissions +
                ", userInfos=" + userInfos +
                '}';
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();


    }

}