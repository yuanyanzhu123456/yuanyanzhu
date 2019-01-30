package com.geo.rcs.modules.source.service;

import java.util.List;
import java.util.Map;

/**
 * 数据源服务接口
 * @Description 面向系统内部提供数据源服务
 * @Author zhangyongming@geotmt.com
 * @Created on : 2018.1.11
 */
public interface SourceService {

    /**
     * 调用系统内置数据源服务，补充规则引擎输入数据
     * @param rulesConfig 规则引擎前置输入数据（未补充数据源字段数据）
     * @param userId  用户ID
     * @return
     * @throws Exception
     */
    String getFieldRes(String rulesConfig, Long userId) throws Exception;

    /**
     * 调用三方数据源微服务模块，补充规则引擎输入数据
     * @param rulesConfig 规则引擎前置输入数据（未补充数据源字段数据）
     * @param userId  用户ID
     * @return
     * @throws Exception
     */
    String getFieldResByThird(String rulesConfig, Long userId) throws Exception;


    /**
     * 调用三方数据源微服务模块，请求接口数据
     * @param
     */

    String getInterResByThird(Long userId, String innerIfType, Map<String, Object> parameters);

    /**
     * 调用三方数据源微服务模块，请求接口数据
     * @param
     */

    String getInterResByThird(Long userId, List<String> innerIfType, Map<String, String> parameters) throws Exception;


}
