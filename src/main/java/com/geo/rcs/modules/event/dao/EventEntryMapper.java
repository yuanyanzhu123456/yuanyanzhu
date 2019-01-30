package com.geo.rcs.modules.event.dao;

import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.event.entity.EventHistoryLog;
import com.geo.rcs.modules.event.vo.EventStatEntry;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 事件进件
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/1/15 15:24
 */
@Mapper
@Component(value = "eventEntryMapper")
public interface EventEntryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EventEntry record);

    int insertSelective(EventEntry record);

    EventEntry selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EventEntry record);

    int updateByPrimaryKey(EventEntry record);

    Page<EventEntry> findByPage(EventEntry record);

    List<EventStatEntry> findEventStatByParam(Map<String, Object> map);

    List<Map<String, String>> findAllFiledKV();

    List<EventStatEntry> findCustEventStatByParam(Map<String, Object> map);

    List<EventStatEntry> findYesterdayEventStat(Map<String, Object> map);

    List<EventStatEntry> thisRuleSetEventStat(Map<String, Object> map);

    List<EventEntry> thisRuleSetRecentStat(Map<String, Object> map);

    EventEntry getEntryDetail(Long entryId);

    List<Map<String, Object>>  thisRuleSetEventTrend(Map<String, Object> map);
    List<Map<String, Object>>  getEntryStatistic();
    List<Map<String, Object>>  getStatisticTotal();
    List<Map<String, Object>>  getEntryAnalysisDay(Map<String, String> parmMap);
    List<Map<String, Object>>  getEntryAnalysisMonth(Map<String, String> parmMap);

    List<Map<String, Object>>  getEntryAnalysisHour(Map<String, String> parmMap);

    List<String> thisRuleSetResultMap(Map<String, Object> map);

    Page<Map<String,Object>> findByPageIds(Long[] ids);

    Page<Map<String,Object>> findByPageAll(EventEntry eventEntry);

    List<Map<String,Object>> getEventCountTrend(Map<String, Object> map);

    List<Map<String, Object>> getEventStatusTrend(Map<String, Object> map);

    List<Map<String,Object>> getEventCostTrend(Map<String, Object> map);

    EventStatEntry getCountTrend(Map<String, Object> map);

    List<Map<String,Object>> getEventScoreTrend(Map<String, Object> map);

    void saveLog(Map<String, Object> map);

    Page<EventHistoryLog> findEventLogByPage(EventHistoryLog eventHistoryLog);

    EventHistoryLog getHistoryLog(Map<String, Object> map);

    void updateLog(Map<String, Object> map);

    EventHistoryLog getJobInfo(EventHistoryLog eventHistoryLog);

    void saveForDecision(EventEntry entry);

    /**通过字段名字查找接口名字和字段描述*/
    List<Map<String,String>> findInterNameAndFieldDesc(String fieldName);
}