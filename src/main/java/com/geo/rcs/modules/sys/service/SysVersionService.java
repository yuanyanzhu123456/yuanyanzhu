package com.geo.rcs.modules.sys.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.entity.SysVersion;
import com.github.pagehelper.Page;

import java.util.Map;

/**
 * 版本
 *
 * @author wuzuqi
 * @email wuzuqi@geotmt.com
 * @date 2018-10-31
 */
public interface SysVersionService {


    SysVersion queryVersionName(String name) throws ServiceException;

    /**
     * 拿到客户信息by id
     * @param id
     * @return
     */
    SysVersion getVerInfoById(Long id) ;

    void save(SysVersion sysVersion) throws ServiceException;

    Page<SysVersion> getVersionInfoByPage(SysVersion sysVersion) throws ServiceException;

    void updateVersion(SysVersion sysVersion) throws Exception;

    void deleteVersion(Long id);

    Map<String,Object> verUserSum() throws ServiceException;


    SysVersion getIdByUserNum(Long userNum) throws ServiceException;


    /**
     * 根据用户id查询unique_code
     */

    Integer queryDayById(Long id);

    /**
     * 查询使用该版本的客户列表
     */
    Page<SysUser> findCustomOfVersion(SysVersion sysVersion);


    /**
     * 获取客户版本的价格
     */
    Double getPriceById(Long customerId);

}
