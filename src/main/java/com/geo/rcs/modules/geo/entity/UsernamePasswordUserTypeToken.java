package com.geo.rcs.modules.geo.entity;


import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 拓展shiro 中的UsernamePasswordToken
 * @author liyaqiang
 *
 */

public class UsernamePasswordUserTypeToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;
    private String usertype ;

    public String getUsertype() {
        return usertype;
    }
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }



}
