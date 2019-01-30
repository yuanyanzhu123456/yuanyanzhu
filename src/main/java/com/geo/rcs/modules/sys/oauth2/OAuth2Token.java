package com.geo.rcs.modules.sys.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/18 20:58
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    private String usertype ;

    private String username;

    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
    public OAuth2Token(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public OAuth2Token(String token, String usertype, String username, String password) {
        this.token = token;
        this.usertype = usertype;
        this.username = username;
        this.password = password;
    }
    public OAuth2Token(String token, String usertype) {
        this.token = token;
        this.usertype = usertype;
    }
}
