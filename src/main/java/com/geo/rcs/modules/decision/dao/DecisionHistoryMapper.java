package com.geo.rcs.modules.decision.dao;

import com.geo.rcs.modules.decision.entity.DecisionHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.decision.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月18日 上午11:58
 */
@Mapper
@Component("decisionHistoryMapper")
public interface DecisionHistoryMapper {

    void insertBySelective(DecisionHistoryLog decisionHistoryLog);

    Page<DecisionHistoryLog> findByPage(DecisionHistoryLog decisionHistoryLog);

    DecisionHistoryLog selectByPrimaryKey(Long id);
}
