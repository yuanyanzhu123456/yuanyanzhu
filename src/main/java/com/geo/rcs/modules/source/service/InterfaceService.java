package com.geo.rcs.modules.source.service;

import com.geo.rcs.modules.rule.inter.entity.EngineInter;

import java.util.List;
import java.util.Map;

/**
 * Author:  yongmingz
 * Created on : 2018.1.11
 */
public interface InterfaceService {

    Map<String, Map> getDataMap(String innerIfType, String parameters, Long userId) throws Exception;

    Map<String, Map> getInterfaceDataMap(String innerIfType, String parameters, Long userId) throws Exception;

	Map<String, Map> getInterfaceDataMapTongDun(List<String> tongDunInerList, String parameters);

    String getInterfaceDataMap(String interName, String parameters, Long userId, Integer type) throws Exception;

    String getDataMap(String innerIfType, String parameters, Long userId, Integer type) throws Exception;

    String getInterfaceDataMapTongDun(List<String> tongDunInerList, String parameters, Integer type);

    /**
     * 获取所有的接口集合
     * @return
     */
    List<EngineInter> getAllInter();

    /**
     * 返回入参计算模块数据
     * @param datasourceInnerList
     * @param parameters
     * @return
     */
    Map<String, Map> getParameterCalcData(List<String> datasourceInnerList,String parameters);

    /**
     * 返回数据平台数据
     * @param dataPlateFormInnerList
     * @param parameters
     * @return
     */
    Map<String,Map> getDataPlateFormData(List<String> dataPlateFormInnerList,String parameters);
}
