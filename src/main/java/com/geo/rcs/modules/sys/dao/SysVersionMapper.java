package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.entity.SysVersion;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by geo on 2018/10/31.
 */
@Mapper
@Component(value = "sysVersionMapper")
public interface SysVersionMapper {


    SysVersion queryVersionName(String name);

    Page<SysVersion> getVersionInfoByPage(SysVersion sysVersion);

    void save(SysVersion sysVersion);

    int updateVersion(SysVersion sysVersion);

    int deleteVersion(Long id);

    Map<String,Object> verUserSum();

    Integer queryDayById(Long id);

    Page<SysUser> findCustomOfVersion(SysVersion sysVersion);

    //拿到客户信息通过id
    SysVersion getVerInfoById(Long id);

    SysVersion getIdByUserNum(Long userNum);


    Double getPriceById(Long customerId);

}
