package com.geo.rcs.modules.decision.service.impl;

import com.geo.rcs.modules.api.annotation.AuthIgnore;
import com.geo.rcs.modules.decision.dao.DecisionHistoryMapper;
import com.geo.rcs.modules.decision.entity.DecisionHistoryLog;
import com.geo.rcs.modules.decision.service.DecisionHistoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.decision.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月19日 下午3:48
 */
@Service
public class DecisionHistoryServiceImpl implements DecisionHistoryService{

    @Autowired
    private DecisionHistoryMapper decisionHistoryMapper;

    @Override
    public void insertBySelective(DecisionHistoryLog decisionHistoryLog) {
        decisionHistoryMapper.insertBySelective(decisionHistoryLog);
    }

    @Override
    public Page<DecisionHistoryLog> findByPage(DecisionHistoryLog decisionHistoryLog) {
        PageHelper.startPage(decisionHistoryLog.getPageNo(), decisionHistoryLog.getPageSize());
        return decisionHistoryMapper.findByPage(decisionHistoryLog);
    }

    @Override
    public DecisionHistoryLog selectByPrimaryKey(Long id) {
        return decisionHistoryMapper.selectByPrimaryKey(id);
    }
}
