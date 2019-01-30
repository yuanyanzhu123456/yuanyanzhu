package com.geo.rcs.modules.geo.config;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ShiroExceptions;
import com.geo.rcs.modules.sys.oauth2.OAuth2Token;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.config
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年02月27日 下午4:28
 */
@Component(value = "defaultModularRealm")
public class DefaultModularRealm extends ModularRealmAuthenticator {

    private List<Realm> realms;


    public DefaultModularRealm (){
        super();
    }

    /**
     * 多个realm实现
     */
    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        return super.doMultiRealmAuthentication(realms, token);
    }
    /**
     * 调用单个realm执行操作
     */
    @Override
    protected AuthenticationInfo doSingleRealmAuthentication(Realm realm,AuthenticationToken token) {
            new ModularRealmAuthenticator().setAuthenticationStrategy(new FirstSuccessfulStrategy());
            AuthenticationInfo info = null;

            // 如果该realms不支持(不能验证)当前token
            if (!realm.supports(token)) {
                throw new ShiroExceptions(StatusCode.EXPIRED.getMessage(), StatusCode.EXPIRED.getCode());
            }
            try {
                info = realm.getAuthenticationInfo(token);
                if (info == null) {
                    throw new ShiroExceptions(StatusCode.EXPIRED.getMessage(), StatusCode.EXPIRED.getCode());
                }
            } catch (Exception e) {
                throw new ShiroExceptions(StatusCode.EXPIRED.getMessage(), StatusCode.EXPIRED.getCode());
            }
            return info;
    }


    /**
     * 判断登录类型执行操作
     */
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)throws AuthenticationException {
        new ModularRealmAuthenticator().setAuthenticationStrategy(new FirstSuccessfulStrategy());
        this.assertRealmsConfigured();
        Realm realm = null;
        OAuth2Token token = (OAuth2Token) authenticationToken;
        try{
            //多个realm重复执行，需经过判断让shiro只执行一次
            //判断是否是后台用户
            if (token.getUsertype() != null && "admin".equals(token.getUsertype())) {
                realm = (Realm) this.getRealms().get(0);
            }
            else{
                realm = (Realm) this.getRealms().get(1);
            }
            return this.doSingleRealmAuthentication(realm, token);
        }
        catch (Exception e){
            throw new ShiroExceptions(StatusCode.EXPIRED.getMessage(),StatusCode.EXPIRED.getCode());
        }
    }

    /**
     * 判断realm是否为空
     */
    @Override
    protected void assertRealmsConfigured() throws IllegalStateException {
        this.realms = this.getRealms();
        if (CollectionUtils.isEmpty(this.realms)) {
            throw new ShiroExceptions(StatusCode.EXPIRED.getMessage(),StatusCode.EXPIRED.getCode());
        }
    }

    @Override
    public List<Realm> getRealms() {
        return realms = (List<Realm>) super.getRealms();
    }

}
