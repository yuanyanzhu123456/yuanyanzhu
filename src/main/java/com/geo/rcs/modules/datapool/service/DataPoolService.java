package com.geo.rcs.modules.datapool.service;

import java.util.Map;

/**
 * 数据池服务
 * @Description 面向内部业务系统提供数据查询服务
 * @author  zhangyongming@geotmt.com
 * @created on 2018.7.19
 */
public interface DataPoolService {

    /**
     * 通过三要素查询信息接口方法
     * @param dimension  监控维度
     * @param userId  用户ID
     * @return
     */
    String getPersonByThreeFactor(String dimension, Long userId, Map<String, Object> paramaters);

}
