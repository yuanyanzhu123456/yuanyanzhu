package com.geo.rcs.modules.geo.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.geo.entity.GeoRoleMenu;
import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysRole;
import com.geo.rcs.modules.sys.entity.SysUserRole;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.geo.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月01日 上午10:51
 */
public interface GeoRoleService {
    Page<SysRole> findByPage(SysRole sysRole) throws ServiceException;

    void save(SysRole sysRole) throws ServiceException;

    SysRole queryRoleById(Long id);

    void updateRole(SysRole sysRole) throws ServiceException;

    List<SysRole> getRoleList(Long userId);

    List<SysMenu> getPermissionList();

    void updatePermission(GeoRoleMenu geoRoleMenu) throws ServiceException;

    void saveRole(SysUserRole sysUserRole);

    List<SysMenu> getPermissionById(Long id, Long roleId)  throws ServiceException;

    List<SysMenu> queryByUserIdAndRoleId(Long id, Long geoId);
}
