package com.geo.rcs.modules.decision.dao;

import com.geo.rcs.modules.decision.entity.*;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月04日 下午6:32
 */
@Mapper
@Component("engineDecisionLogMapper")
public interface EngineDecisionLogMapper {

    Long save(EngineDecisionLog engineDecisionLog);

    Page<EngineDecisionLog> findByPage(EngineDecisionLog engineDecisionLog);

    EngineDecisionLog selectByPrimaryKey(Long id);

    void saveToUserModel(UserDecision userDecision);

    Page<Map<String, Object>> findByPageAll(EngineDecisionLog engineDecisionLog);

    List<Map<String, Object>> findAllPdfDataList(EngineDecisionLog engineDecisionLog);

    Page<Map<String, Object>> findByPageIds(Long[] ids);

    DecisionAnalyse getEventCountBySelective(Map<String, Object> map);

    List<Map<String,Object>> getCountTrendByHour(Map<String, Object> map);

    List<Map<String,Object>> getCountTrendByDay(Map<String, Object> map);

    List<Map<String,Object>> getCountTrendByMonth(Map<String, Object> map);

    List<Map<String,Object>> getEventCostTrend(Map<String, Object> map);

    List<Map<String,Object>> getEventScoreTrend(Map<String, Object> map);

    List<Map<String,Object>> getPassCountTrendByType(Map<String, Object> map);

    List<Map<String,Object>> getManualCountTrendByType(Map<String, Object> map);

    List<Map<String,Object>> getRefuseCountTrendByType(Map<String, Object> map);

    List<Map<String,Object>> getInvalidCountTrendByType(Map<String, Object> map);

    List<Map<String,Object>> getStatusCountTrendByType(Map<String, Object> map);

    List<Map<String,Object>> getFailCountTrendByType(Map<String, Object> map);
}
