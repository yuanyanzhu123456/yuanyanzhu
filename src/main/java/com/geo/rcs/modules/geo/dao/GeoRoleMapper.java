package com.geo.rcs.modules.geo.dao;

import com.geo.rcs.modules.geo.entity.GeoRoleMenu;
import com.geo.rcs.modules.sys.entity.*;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.geo.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月01日 上午10:53
 */
@Mapper
@Component(value = "geoRoleMapper")
public interface GeoRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> getRoleList(Long userId);

    Page<SysRole> findByPage(SysRole sysRole);

    List<SysMenu> getPermissionList();

    void updatePermission(GeoRoleMenu geoRoleMenu);

    void saveRole(SysUserRole sysUserRole);

    SysUserRole queryByRoleIdAndUid(SysUserRole sysUserRole);

    SysRolePermission queryByRoleIdAndPid(SysRolePermission sysRolePermission);

    void deleteByUserId(@Param(value = "userId") Long userId);

    void deleteByRoleId(@Param(value = "roleId") Long roleId);

    List<SysRolePermission> getRoleAndPermission(Long uId);

    List<SysMenu> getPermissionById(@Param(value = "id") Long id, @Param(value = "roleId") Long roleId);

    void updateRoleById(SysRoleMenu sysRoleMenu);

    List<SysMenu> queryByUserIdAndRoleId(@Param(value = "id") Long id, @Param(value = "geoId") Long geoId);

    void deleteByGeoIdAndRoleId(@Param(value = "geoId") Long geoId, @Param(value = "roleId") Long roleId);

}
