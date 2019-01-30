package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.modules.sys.dao.CusVersionMapper;
import com.geo.rcs.modules.sys.dao.SysRoleMapper;
import com.geo.rcs.modules.sys.dao.SysUserMapper;
import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysRole;
import com.geo.rcs.modules.sys.entity.SysRoleMenu;
import com.geo.rcs.modules.sys.entity.SysUserRole;
import com.geo.rcs.modules.sys.service.SysRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.sys.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月15日 下午7:02
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private CusVersionMapper cusVersionMapper;

    @Override
    public Page<SysRole> findByPage(SysRole sysRole) {
        PageHelper.startPage(sysRole.getPageNo(), sysRole.getPageSize());
        return sysRoleMapper.findByPage(sysRole);
    }

    @Override
    public void save(SysRole sysRole) {
        sysRole.setCreateTime(new Date());
        sysRole.setDescription(sysRole.getRoleName());
        sysRole.setType(1);
        sysRoleMapper.insertSelective(sysRole);
    }

    @Override
    public SysRole queryRoleById(Long id) {
        return sysRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateRole(SysRole sysRole) {
        sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public List<SysRole> getRoleList(Long userId) {
        return sysRoleMapper.getRoleList(userId);
    }

    @Override
    public List<SysMenu> getPermissionList() {
        return sysRoleMapper.getPermissionList();
    }

    @Override
    @Transactional
    public void updatePermission(SysRoleMenu sysRoleMenu) {
        //sysRoleMapper.updateRoleById(sysRoleMenu);
        sysRoleMapper.deleteByRoleId(sysRoleMenu.getRoleId(),sysRoleMenu.getUserId());
        sysRoleMapper.updatePermission(sysRoleMenu);
    }

    @Override
    public void saveRole(SysUserRole sysUserRole) {
        sysRoleMapper.deleteByUserId(sysUserRole.getuId());
        sysRoleMapper.saveRole(sysUserRole);
    }

    @Override
    public List<SysMenu> getPermissionById(Long id,Long roleId) {
        if(roleId == 2 || roleId == 3 || roleId == 4){
            Date expireTime = cusVersionMapper.getExpireTime(id);
            if(expireTime == null){
                return sysRoleMapper.getPermissionById(0L,roleId);
            }
            //过期
            if (new Date().after(expireTime)) {
                List<SysMenu> lists = sysUserMapper.queryPermissionOutOfDate(0L,roleId);
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getFilterType() == 2){
                        lists.get(i).setPerms(lists.get(i).getPerms()+":disabled");
                    }
                }
                return lists;
            } else {
                return sysRoleMapper.getPermissionById(0L,roleId);
            }
        }
        else{
            Date expireTime = cusVersionMapper.getExpireTime(id);
            if(expireTime == null){
                return sysRoleMapper.getPermissionById(id,roleId);
            }
            //过期
            if (new Date().after(expireTime)) {
                List<SysMenu> lists = sysRoleMapper.getPermissionListNew();
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getFilterType() == 2){
                        lists.get(i).setPerms(lists.get(i).getPerms()+":disabled");
                    }
                }
                return lists;
            } else {
                return sysRoleMapper.getPermissionById(id,roleId);
            }
            }
        }


    @Override
    public List<SysMenu>  queryByUserIdAndRoleId(Long id, Long userId) {
        return sysRoleMapper.queryByUserIdAndRoleId(id,userId);
    }

    @Override
    public List<SysRole> queryRoleName(String roleName) {
        return sysRoleMapper.queryRoleName(roleName);
    }


}
