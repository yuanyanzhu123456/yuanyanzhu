package com.geo.rcs.common.exception;

import com.geo.rcs.common.StatusCode;
import org.apache.shiro.authc.AuthenticationException;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.exception
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月19日 下午6:18
 */
public class ShiroExceptions extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = StatusCode.ERROR.getCode();

    public ShiroExceptions(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ShiroExceptions(String msg, int code) {
        super(msg);
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
