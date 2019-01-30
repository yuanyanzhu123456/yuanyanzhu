
package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.modules.sys.dao.*;
import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.entity.SysUserToken;
import com.geo.rcs.modules.sys.service.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserTokenMapper sysUserTokenMapper;
    @Autowired
    private CusVersionMapper cusVersionMapper;

    @Override
    public Set<String> getUserPermissions(long userId, String userType) {
        List<String> permsList;
        Long uniqueCode = sysUserMapper.getUniqueCodeById(userId);
        Date expireTime = cusVersionMapper.getExpireTime(uniqueCode);
        //已过期，限制权限
        if (new Date().after(expireTime)){
            //系统管理员，拥有最高权限
            if(Constant.SUPER_ADMIN == userId){
                List<SysMenu> sysMenuList = sysPermissionMapper.queryListOutOfDate();
                permsList = new ArrayList<>(sysMenuList.size());
                for (SysMenu sysMenu : sysMenuList) {
                    permsList.add(sysMenu.getPerms());
                }
            }else{
                permsList = sysUserMapper.queryAllPermsOutOfDate(userId);
            }
        }else{
            //系统管理员，拥有最高权限
            if(Constant.SUPER_ADMIN == userId){
                List<SysMenu> sysMenuList = sysPermissionMapper.queryList();
                permsList = new ArrayList<>(sysMenuList.size());
                for (SysMenu sysMenu : sysMenuList) {
                    permsList.add(sysMenu.getPerms());
                }
            }else{
                permsList = sysUserMapper.queryAllPerms(userId);
            }
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }
    @Override
    public SysUserToken queryByToken(String token) {
        return sysUserTokenMapper.queryByToken(token);
    }

    @Override
    public SysUser queryUser(Long userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public SysUserToken queryById(Long id) {
        return sysUserTokenMapper.queryByUserId(id);
    }

}


