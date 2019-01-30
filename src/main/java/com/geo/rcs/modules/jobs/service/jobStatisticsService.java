package com.geo.rcs.modules.jobs.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface jobStatisticsService {

    void setRegisterInfoByKey(String infoName, String infoContent);

    void updateRegisterInfoByKey(String infoName, String infoContent);

    String getRegisterInfoByKey(String infoName);

    Map getAllRegisterInfo();

    void deleteRegisterInfoByKey(String infoName);

    Boolean incrSuccess(String role,int num);

    Boolean incrfail(String role,int num);

    Boolean delStatisticData();

    Boolean incrDistrbuteSuccess(String role, int num);

    Boolean incrDistrbuteFail(String role,int num);



    /**
     * 获取分发,执行吞吐量
     *
     * @return
     * @throws ParseException
     */
    Map<String, Long> getDistrbiteExcuteThroughPut(String role) throws ParseException;

    /**
     * 定时统计api
     *
     * @param unit     单位 second minute hour day
     * @param intervel
     * @param size     保存多久的数据
     * @return
     */
    Boolean statisticsApi(String role,String unit, int intervel, int size);

    /**
     * 获取统计数据api
     *
     * @param unit
     * @param intervel
     * @param size
     * @return
     */
    List<Map<Object, Object>> getstatisticsApi(String unit, int intervel, int size,String role);


}
