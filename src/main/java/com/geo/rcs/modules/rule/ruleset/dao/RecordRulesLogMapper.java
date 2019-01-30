package com.geo.rcs.modules.rule.ruleset.dao;

import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年04月10日 上午10:24
 */
@Mapper
@Component(value = "recordRulesLogMapper")
public interface RecordRulesLogMapper {

    void insertBySelective(EngineHistoryLog engineHistoryLog);

    Page<EngineHistoryLog> getRecordById(EngineHistoryLog engineHistoryLog);

    void deleteById(Long logId);
}
