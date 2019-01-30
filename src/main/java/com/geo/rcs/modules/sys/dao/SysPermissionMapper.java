package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "sysPermissionMapper")
public interface SysPermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysMenu> queryList();

    List<SysMenu> queryListOutOfDate();
}