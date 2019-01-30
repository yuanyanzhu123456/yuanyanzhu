package com.geo.rcs.modules.admin.adminIndex.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;

import java.util.Map;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/10/23  10:56
 **/
public interface StatisticInfoService {
    /**
     * 统计客户的规则集数、决策集数、用户数等
     * @param id
     * @return
     */
    Map<Object,Object> getSingleCustomInfo(Long id);

    /**
     * 查询客户下的用户列表
     * @param sysUser
     * @return
     */
    Page<SysUser> findEmployerListByPage(SysUser sysUser) throws ServiceException;

    /**
     * 返回概览：客户数、用户数、活跃用户数
     * @return
     */
    Map<Object,Object> getOverviewInfo();

    /**
     * 统计用户活跃度趋势图
     * @param map
     * @return
     */
    Map<String,Object> getActivityGraph(Map<Object,Object> map);

    /**
     * 指定时间内的新增用户趋势图
     * @param map
     * @return
     */
    Map<String,Object> getNewCustomTrend(Map<Object,Object> map);

    /**
     * 客户活跃度排行
     * @param dayNum
     * @return
     */
    Map<String,Object> userActivityRankList(Integer dayNum);

}
