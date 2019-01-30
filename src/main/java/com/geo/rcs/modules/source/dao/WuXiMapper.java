package com.geo.rcs.modules.source.dao;

import com.geo.rcs.modules.source.entity.WuXi;
import com.geo.rcs.modules.source.entity.WuXiWhiteList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import scala.util.parsing.combinator.testing.Str;

import java.util.List;
import java.util.Map;

/**
 * Created by 曾志 on 2019/1/18.
 */
@Mapper
@Component(value = "wuXiMapper")
public interface WuXiMapper {
    //根据id取值
    WuXi getWuXiById(Long id);

    List<WuXiWhiteList> getWuXiWhiteLists(@Param("name")String name, @Param("idNumber")String idNumber);

    String getReportNo(@Param("idNumber")String idNumber);

    List<Double> getBalance(@Param("reportId")String reportId);

    List<Integer> getLoanInfoCurroverdueamount(@Param("reportId")String reportId);

    List<Integer> getLoanCardInfoCurroverdueamount(@Param("reportId")String reportId);

    String getUndestyryloancardUsedAvg6(@Param("reportId")String reportId);

    String getUndestyrysloancardCreditlimit(@Param("reportId")String reportId);

    List<Double> getAccount(@Param("reportId")String reportId);

    String getEduLevel(@Param("reportId")String reportId);

    List<String> getLoanInfoLatest24state(@Param("reportId")String reportId);

    List<String> getLoanCardInfoLast24state(@Param("reportId")String reportId);

    String getLoanCardCount(@Param("reportId")String reportId);

}
