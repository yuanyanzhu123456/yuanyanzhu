package com.geo.rcs.modules.admin.adminIndex.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/10/23  11:01
 **/

@Mapper
@Component(value = "statisticInfoMapper")
public interface StatisticInfoMapper {

    Map<Object,Object> getSingleCustomInfo(Long id);

    Map<Object,Object> StatisticInfoService();

    List<Map<Object,Object> > getActivityUserGraph(Map<Object,Object> dayNum);

    List<Map<Object,Object> > getActivityCmpGraph(Map<Object,Object> dayNum);

    List<Map<Object,Object> > getPerCompanyNewUserNum(Integer dayNum);

    List<Map<Object,Object> > getNewCustomsTrend(Map<Object,Object> map);

    List<Map<Object,Object> > getCustomAndUserNumForTime(Integer dayNum);

    Map<Object,Object> getLoginCmpAndUserNum(Integer dayNum);

    List<Map<Object,Object>> logCountByCom(Integer num);

}
