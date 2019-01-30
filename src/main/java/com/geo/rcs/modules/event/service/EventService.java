package com.geo.rcs.modules.event.service;

import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.event.entity.EventHistoryLog;
import com.geo.rcs.modules.event.vo.EventReport;
import com.geo.rcs.modules.event.vo.EventStatEntry;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * 事件进件
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/1/16 11:41
 */
public interface EventService {
    /**
     * 保存
     * @param entry
     * @return
     */
    EventEntry save(EventEntry entry);

    /**
     * 修改
     * @param entry
     * @return
     */
    int update(EventEntry entry);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    EventReport findById(Long id);

    /**
     * 分页查询
     * @return
     */
    Page<EventEntry> findByPage(EventEntry eventEntry);


    /** 事件统计相关接口 */
    /**
     * 今日事件统计
     * @return
     */
    EventStatEntry todayEventStat(Map<String, Object> map);

    /**
     * 风险事件统计
     * @return
     */
    EventStatEntry riskEventStat(Map<String, Object> map);

    /**
     * 风险地图统计
     */
    EventStatEntry mapEventStat(Map<String, Object> map);

    /**
     * 获取所有产品字段（名称：描述）并缓存
     */
    Map<String, String> findAllFiledKV();

    /**
     * 获取所有产品字段（名称：描述）不从redis读取，不缓存
     * @return
     */
    Map<String,String> findAllFieldKV2();

    /**
     * 客户事件统计
     * @param map
     * @return
     */
    EventStatEntry custEventStat(Map<String, Object> map);

    /**
     * 昨日事件统计
     * @param map
     * @return
     */
    List<EventStatEntry> yesterdayEventStat(Map<String, Object> map);

    EventStatEntry thisRuleSetEventStat(Map<String, Object> map);

    List<EventEntry> thisRuleSetRecentStat(Map<String, Object> map);

    EventEntry getEntryDetail(Long entryId);

    List<Map<String, Object>> thisRuleSetEventTrend(Map<String, Object> map);

    List<String> thisRuleSetResultMap(Map<String, Object> map);

    Map<String, Object>  selectBySelective(Map<String, Object> map);

    Map<String,Object> getEventCountTrend(Map<String, Object> map);

    Map<String,Object> getEventStatusTrend(Map<String, Object> map);

    List<Map<String,Object>> getEventCostTrend(Map<String, Object> map);

    List<Map<String,Object>> getEventScoreTrend(Map<String, Object> map);

    Page<Map<String,Object>> findByPageIds(Long[] ids);

    Page<Map<String,Object>> findByPageAll(EventEntry eventEntry);

    /**
     * 保存批量进件日志
     * @param map
     */
    void saveLog(Map<String, Object> map);
    /**
     * 批量进件日志列表
     * @param eventHistoryLog
     * @return EventHistoryLog
     */
    Page<EventHistoryLog> findEventLogByPage(EventHistoryLog eventHistoryLog);
    /**
     * 查询是否存在此记录
     * @param map
     * @return EventHistoryLog
     */
    EventHistoryLog getHistoryLog(Map<String, Object> map);
    /**
     * 更新日志记录
     * @param map
     */
    void updateLog(Map<String, Object> map);

    EventHistoryLog getJobInfo(EventHistoryLog eventHistoryLog);

    EventEntry saveForDecision(EventEntry entry);
}
