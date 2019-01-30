package com.geo.rcs.common;

/**
 * @author guoyujie
 * @date 2017-12-15
 */
public enum ResponseStatus {

	SUCCESS(2000, "成功"), ERROR(4000, "失败"), EXPIRED(4040, "会话过期"), DENIED(4030, "权限不足"), ERRAC(4099, "激活状态"),ERRNp(4098, "激活状态");

	private Integer code;
	private String desc;

	ResponseStatus(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}