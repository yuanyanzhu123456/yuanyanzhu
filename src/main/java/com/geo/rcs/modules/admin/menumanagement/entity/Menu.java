package com.geo.rcs.modules.admin.menumanagement.entity;

import java.io.Serializable;
import java.util.Date;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月7日 上午10:14:41 
 */
public class Menu  implements Serializable{

	private Long id;
	private Long parentId;

	private String parentName;

	private String name;
	private String url;
	private String perms;
	// 类型 1客户端 0运营端
	private Integer type;
	// ztree属性
	private Integer open;

	private String frontIcon;
	private String backIcon;
	// 排序
	private Integer orderId;

	private Integer pageSize;

	private Integer pageNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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
		return "Menu{" +
				"id=" + id +
				", parentId=" + parentId +
				", parentName='" + parentName + '\'' +
				", name='" + name + '\'' +
				", url='" + url + '\'' +
				", perms='" + perms + '\'' +
				", type=" + type +
				", open=" + open +
				", frontIcon='" + frontIcon + '\'' +
				", backIcon='" + backIcon + '\'' +
				", orderId=" + orderId +
				", pageSize=" + pageSize +
				", pageNo=" + pageNo +
				'}';
	}
}
