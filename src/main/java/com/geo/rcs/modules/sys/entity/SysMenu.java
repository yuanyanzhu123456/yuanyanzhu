package com.geo.rcs.modules.sys.entity;


import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/10/18 15:32
 */
public class SysMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	// 菜单ID
	private Long menuId;
	// 父菜单ID，一级菜单为0
	private Long parentId;
	// 父菜单名称
	private String parentName;
	// 菜单名称
	private String name;
	// 菜单URL
	private String url;
	// 授权(多个用逗号分隔，如：user:list,user:create)
	private String perms;
	// 类型     0：目录   1：菜单   2：按钮
	private Integer type;
	// 排序
	private Integer orderId;
	// ztree属性
	private Boolean open;
	// 列表
	private List<?> list;

	private Long roleId;
	//图标
	private String frontIcon;

	private String backIcon;

	private Integer filterType;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getFrontIcon() {
		return frontIcon;
	}

	public void setFrontIcon(String frontIcon) {
		this.frontIcon = frontIcon;
	}

	public String getBackIcon() {
		return backIcon;
	}

	public void setBackIcon(String backIcon) {
		this.backIcon = backIcon;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFilterType() {
		return filterType;
	}

	public void setFilterType(Integer filterType) {
		this.filterType = filterType;
	}

	/**
	 * 设置：父菜单ID，一级菜单为0
	 * @param parentId 父菜单ID，一级菜单为0
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取：父菜单ID，一级菜单为0
	 * @return Long
	 */
	public Long getParentId() {
		return parentId;
	}
	
	/**
	 * 设置：菜单名称
	 * @param name 菜单名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：菜单名称
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置：菜单URL
	 * @param url 菜单URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取：菜单URL
	 * @return String
	 */
	public String getUrl() {
		return url;
	}
	
	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}
}
