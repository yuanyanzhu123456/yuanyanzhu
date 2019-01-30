package com.geo.rcs.modules.decision.service;

import com.geo.rcs.modules.decision.entity.EngineDecisionLog;
import com.geo.rcs.modules.decision.entity.UserDecision;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;


/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月04日 下午6:30
 */
public interface EngineDecisionLogService {

    EngineDecisionLog save(EngineDecisionLog engineDecisionLog);

    EngineDecisionLog selectByPrimaryKey(Long id);

    void saveToUserModel(UserDecision userDecision);

    Page<EngineDecisionLog> findByPage(EngineDecisionLog engineDecisionLog);

    Map<String,Object> selectBySelective(Map<String, Object> map);

    Map<String,Object> getEventCountTrend(Map<String, Object> map);

    List<Map<String, Object>> getEventCostTrend(Map<String, Object> map);

    List<Map<String, Object>> getEventScoreTrend(Map<String, Object> map);

    Map<String,Object> getEventStatusTrend(Map<String, Object> map);

    Page<Map<String, Object>> findByPageAll(EngineDecisionLog engineDecisionLog);

    Page<Map<String, Object>> findByPageIds(Long[] ids);

    List<Map<String,Object>> findAllPdfData(EngineDecisionLog engineDecisionLog);
}
