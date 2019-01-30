package com.geo.rcs.modules.sys.dao;


import com.geo.rcs.modules.sys.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Component(value = "permissionMapper")
public interface PermissionMapper {

    List<SysPermission> findByUserId(Long roleId);

    ArrayList<SysPermission> list();

    ArrayList<SysPermission> getAppPermission();

    ArrayList<SysPermission> tree();
}
