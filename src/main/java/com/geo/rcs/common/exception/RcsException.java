package com.geo.rcs.common.exception;

import com.geo.rcs.common.StatusCode;

/**
 * 自定义异常
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/18 10:22
 */
public class RcsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = StatusCode.ERROR.getCode();
    
    public RcsException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public RcsException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public RcsException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public RcsException(StatusCode statusCode) {
		super(statusCode.getMessage());
		this.msg = statusCode.getMessage();
		this.code = statusCode.getCode();
	}
	
	public RcsException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
