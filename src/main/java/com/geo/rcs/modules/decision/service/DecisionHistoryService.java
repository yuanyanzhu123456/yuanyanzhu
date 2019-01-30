package com.geo.rcs.modules.decision.service;

import com.geo.rcs.modules.decision.entity.DecisionHistoryLog;
import com.github.pagehelper.Page;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.decision.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月19日 下午3:48
 */
public interface DecisionHistoryService {

    void insertBySelective(DecisionHistoryLog decisionHistoryLog);

    Page<DecisionHistoryLog> findByPage(DecisionHistoryLog decisionHistoryLog);

    DecisionHistoryLog selectByPrimaryKey(Long id);
}
