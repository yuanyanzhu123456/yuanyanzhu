package com.geo.rcs.common.validator;

/**
 * @author guoyujie
 * @date 2017-12-22
 * @version 1.0
 */
public enum ResultType {

	SUCCESS, FAILD;

	private String msg;

	public String getMsg() {
		return msg;
	}

	public ResultType setMsg(String msg) {
		this.msg = msg;
		return this;
	}
}
