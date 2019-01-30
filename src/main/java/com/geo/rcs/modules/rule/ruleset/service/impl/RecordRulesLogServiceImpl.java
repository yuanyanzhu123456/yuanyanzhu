package com.geo.rcs.modules.rule.ruleset.service.impl;

import com.geo.rcs.modules.rule.ruleset.dao.RecordRulesLogMapper;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.service.RecordRulesLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年04月10日 上午10:24
 */
@Service
public class RecordRulesLogServiceImpl implements RecordRulesLogService {

    @Autowired
    private RecordRulesLogMapper recordRulesLogMapper;

    @Override
    public Page<EngineHistoryLog> getRecordById(EngineHistoryLog engineHistoryLog) {
        PageHelper.startPage(engineHistoryLog.getPageNo(), engineHistoryLog.getPageSize());
        return recordRulesLogMapper.getRecordById(engineHistoryLog);
    }

    @Override
    public void deleteById(Long logId) {
        recordRulesLogMapper.deleteById(logId);
    }
}
