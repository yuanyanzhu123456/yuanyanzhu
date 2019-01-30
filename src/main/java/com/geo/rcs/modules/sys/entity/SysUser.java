package com.geo.rcs.modules.sys.entity;

import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//@RuleTemplate
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    //@Id
    //@GeneratedValue
    private Long id;
    // 用户名(登录名)
    @NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String username;
    // 密码
    @NotBlank(message="密码不能为空", groups = AddGroup.class)
    private String password;
    // 真实姓名
    @NotBlank(message = "姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    private String contact;
    // 邮箱
    @NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    private String email;
    // 手机号
    @NotBlank(message = "手机号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 11, message = "手机号长度不能超过11位")
    private String mobilephone;
    // 状态  0：禁用   1：正常
    private Integer status;

    //创建时间
    private Date createTime;
    // 创建人
    private String creater;
    // 角色ID
    @NotNull
    private Long roleId;
    // 角色ID列表
    private List<Long> roleIdList;
    // 最后登录ip
    private String loginIp;
    // 最后登陆时间
    private Date loginDate;
    //每页条数
    private Integer pageSize;

    //从几开始
    private Integer pageNo;
    //用户类型
    private String userType;
    //用户唯一标识
    private Long uniqueCode;
    //公司名称
    private String company;

    private String passwordTwo;

    public String getPasswordTwo() {
        return passwordTwo;
    }

    public void setPasswordTwo(String passwordTwo) {
        this.passwordTwo = passwordTwo;
    }

    private String salt;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定

    private String roleName;

    private Customer customer;

    //版本Id
    private Long versionId;

    private Date versionCreateTime;

    private Date versionExpireTime;

    private Date versionUpdateTime;

    public Date getVersionCreateTime() {
        return versionCreateTime;
    }

    public void setVersionCreateTime(Date versionCreateTime) {
        this.versionCreateTime = versionCreateTime;
    }

    public Date getVersionExpireTime() {
        return versionExpireTime;
    }

    public void setVersionExpireTime(Date versionExpireTime) {
        this.versionExpireTime = versionExpireTime;
    }

    public Date getVersionUpdateTime() {
        return versionUpdateTime;
    }

    public void setVersionUpdateTime(Date versionUpdateTime) {
        this.versionUpdateTime = versionUpdateTime;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCompany() {
        return company;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public List<Long> getRoleIdList() {
        return roleIdList;
    }
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }



    //@ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    //@JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    private List<SysRole> roleList;// 一个用户具有多个角色

    private List<SysPermission> permissions;

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
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


    @Override
	public String toString() {
		return "SysUser [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name
				+ ", contact=" + contact + ", email=" + email + ", mobilephone=" + mobilephone + ", status=" + status
				+ ", createTime=" + createTime + ", creater=" + creater + ", roleId=" + roleId + ", roleIdList="
				+ roleIdList + ", loginIp=" + loginIp + ", loginDate=" + loginDate + ", pageSize=" + pageSize
				+ ", pageNo=" + pageNo + ", userType=" + userType + ", uniqueCode=" + uniqueCode + ", company="
				+ company + ", passwordTwo=" + passwordTwo + ", salt=" + salt + ", roleName=" + roleName + ", customer="
				+ customer + ", roleList=" + roleList + ", permissions=" + permissions + "]";
	}

	public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }


}