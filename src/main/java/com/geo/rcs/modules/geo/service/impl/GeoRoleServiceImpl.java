package com.geo.rcs.modules.geo.service.impl;

import com.geo.rcs.modules.geo.dao.GeoRoleMapper;
import com.geo.rcs.modules.geo.entity.GeoRoleMenu;
import com.geo.rcs.modules.geo.service.GeoRoleService;
import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysRole;
import com.geo.rcs.modules.sys.entity.SysUserRole;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.geo.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月01日 上午10:52
 */
@Service
public class GeoRoleServiceImpl implements GeoRoleService{

    @Autowired
    private GeoRoleMapper geoRoleMapper;

    @Override
    public Page<SysRole> findByPage(SysRole sysRole) {
        PageHelper.startPage(sysRole.getPageNo(), sysRole.getPageSize());
        return geoRoleMapper.findByPage(sysRole);
    }

    @Override
    public void save(SysRole sysRole) {
        sysRole.setCreateTime(new Date());
        sysRole.setDescription(sysRole.getRoleName());
        sysRole.setType(0);
        geoRoleMapper.insertSelective(sysRole);
    }

    @Override
    public SysRole queryRoleById(Long id) {
        return geoRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateRole(SysRole sysRole) {
        geoRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public List<SysRole> getRoleList(Long userId) {

        return geoRoleMapper.getRoleList(userId);
    }

    @Override
    public List<SysMenu> getPermissionList() {
        return geoRoleMapper.getPermissionList();
    }

    @Override
    @Transactional
    public void updatePermission(GeoRoleMenu geoRoleMenu) {
        //sysRoleMapper.updateRoleById(sysRoleMenu);
        geoRoleMapper.deleteByGeoIdAndRoleId(geoRoleMenu.getGeoId(),geoRoleMenu.getRoleId());
        geoRoleMapper.updatePermission(geoRoleMenu);
    }

    @Override
    public void saveRole(SysUserRole sysUserRole) {
        geoRoleMapper.deleteByUserId(sysUserRole.getuId());
        geoRoleMapper.saveRole(sysUserRole);
    }

    @Override
    public List<SysMenu> getPermissionById(Long id,Long roleId) {
        return geoRoleMapper.getPermissionById(id,roleId);
    }

    @Override
    public List<SysMenu> queryByUserIdAndRoleId(Long id, Long geoId) {
        return geoRoleMapper.queryByUserIdAndRoleId(id,geoId);
    }


}
