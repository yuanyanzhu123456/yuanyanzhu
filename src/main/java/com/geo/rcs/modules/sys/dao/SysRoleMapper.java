package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.*;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "sysRoleMapper")
public interface SysRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> getRoleList(Long userId);

    Page<SysRole> findByPage(SysRole sysRole);

    List<SysMenu> getPermissionList();

    void updatePermission(SysRoleMenu sysRoleMenu);

    void saveRole(SysUserRole sysUserRole);

    SysUserRole queryByRoleIdAndUid(SysUserRole sysUserRole);

    SysRolePermission queryByRoleIdAndPid(SysRolePermission sysRolePermission);

    void deleteByRoleId(@Param(value = "roleId") Long roleId, @Param(value = "userId") Long userId);

    List<SysRolePermission> getRoleAndPermission(Long uId);

    List<SysMenu> getPermissionById(@Param(value = "id") Long id, @Param(value = "roleId") Long roleId);

    void updateRoleById(SysRoleMenu sysRoleMenu);

    List<SysMenu> queryByUserIdAndRoleId(@Param(value = "id") Long id, @Param(value = "userId") Long userId);

    void deleteByUserId(Long userId);

    List<SysRole> queryRoleName(String roleName);

    List<SysMenu> getPermissionListNew();
}