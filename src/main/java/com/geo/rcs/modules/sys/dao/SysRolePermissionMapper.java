package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
@Mapper
@Component(value = "sysRolePermissionMapper")
public interface SysRolePermissionMapper {
    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);
}