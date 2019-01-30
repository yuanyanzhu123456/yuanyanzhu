package com.geo.rcs.modules.sys.service;


import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.sys.entity.LoginLog;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * 登录日志
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/21
 */
public interface LoginLogService {

    LoginLog queryObject(Long id);

    List<LoginLog> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(LoginLog loginLog);

    void update(LoginLog loginLog);

    void delete(Long id);

    void deleteBatch(Long[] ids);

    Page<LoginLog> findLoginLogByPage(LoginLog loginLog) throws ServiceException;

    Map<String,Object> logCountByEmp(Map<String, Object> map);

    List<LoginLog> getLogsByMacIddr(String macIddr);
}
