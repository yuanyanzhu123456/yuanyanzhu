package com.geo.rcs.modules.sys.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysRole;
import com.geo.rcs.modules.sys.entity.SysRoleMenu;
import com.geo.rcs.modules.sys.entity.SysUserRole;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.sys.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月15日 下午7:02
 */
public interface SysRoleService {

    Page<SysRole> findByPage(SysRole sysRole) throws ServiceException;

    void save(SysRole sysRole) throws ServiceException;

    SysRole queryRoleById(Long id);

    void updateRole(SysRole sysRole) throws ServiceException;

    List<SysRole> getRoleList(Long userId);

    List<SysMenu> getPermissionList();

    void updatePermission(SysRoleMenu sysRoleMenu) throws ServiceException;

    void saveRole(SysUserRole sysUserRole);

    List<SysMenu> getPermissionById(Long id, Long roleId)  throws ServiceException;

    List<SysMenu>  queryByUserIdAndRoleId(Long id, Long userId);

    List<SysRole> queryRoleName(String roleName);

}
