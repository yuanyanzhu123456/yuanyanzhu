package com.geo.rcs.modules.engine.entity;

/**
 * @author zhangyongming
 * @date 2017-12-15
 */
public enum RulesEngineCode {

	/**
	 * Token Validate
	 */
	INIT(0, "初始状态"),
	PASS(1, "通过"),
	HUMAN(2, "人工审核"),
	REJECT(3, "拒绝"),
	INVALID(4, "失效"),
	FAIL(5, "失败");

	private int code;
	private String message;

	RulesEngineCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}