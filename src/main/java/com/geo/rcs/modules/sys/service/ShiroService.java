package com.geo.rcs.modules.sys.service;

import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.geo.entity.GeoUserToken;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.entity.SysUserToken;

import java.util.Set;

/**
 * shiro相关接口
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/11/1 20:48
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId, String userType);

    SysUserToken queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUser queryUser(Long userId);

    SysUserToken queryById(Long id);


}
