package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.LoginLog;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 登录日志
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */
@Mapper
@Component(value = "loginLogMapper")
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    Page<LoginLog> findLoginLogByPage(LoginLog loginLog);


    //WZQ
    List<Map<String,Object>> logRankByEmp(Map<String, Object> map);
    List<Map<String,Object>> logTotalByEmp(Map<String, Object> map);


}
