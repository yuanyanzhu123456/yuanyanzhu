package com.geo.rcs.modules.rule.ruleset.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年04月10日 上午10:23
 */
public interface RecordRulesLogService {

    Page<EngineHistoryLog> getRecordById(EngineHistoryLog engineHistoryLog) throws ServiceException;

    void deleteById(Long logId);
}
