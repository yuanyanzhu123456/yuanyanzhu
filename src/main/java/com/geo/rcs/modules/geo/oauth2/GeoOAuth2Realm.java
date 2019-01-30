package com.geo.rcs.modules.geo.oauth2;

import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.geo.entity.GeoUserToken;
import com.geo.rcs.modules.geo.service.GeoUserService;
import com.geo.rcs.modules.sys.oauth2.OAuth2Token;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 登录认证
 * 
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/11/1 20:54
 */
@Component(value = "geoOAuth2Realm")
public class GeoOAuth2Realm extends AuthorizingRealm {
    @Autowired
    private GeoUserService geoUserService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        GeoUser geoUser = new GeoUser();
        if(principals.getPrimaryPrincipal().getClass().getName() != geoUser.getClass().getName()){
                return new SimpleAuthorizationInfo();
        }

        GeoUser user = (GeoUser)principals.getPrimaryPrincipal();

        Long geoId = user.getId();

        //用户权限列表
        Set<String> permsSet = geoUserService.getUserPermissions(geoId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息
        GeoUserToken tokenEntity = geoUserService.queryGeoUserByToken(accessToken);
        //token失效
        if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        GeoUser user = geoUserService.queryGeoUserById(tokenEntity.getGeoId());
        //账号锁定
        if(user.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }
}
