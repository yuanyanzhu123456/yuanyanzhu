package com.geo.rcs.modules.monitor.service;


import com.geo.rcs.modules.monitor.entity.TaskLog;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时任务日志
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
public interface TaskLogService {

	/**
	 * 根据ID，查询定时任务日志
	 */
	TaskLog queryObject(Long jobId, Long userId);
	

	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);
	
	/**
	 * 保存定时任务日志
	 */
	void save(TaskLog log,Long userId);/**
	 * 保存定时任务日志
	 */
	void update(TaskLog log,Long userId);

    Page<TaskLog> findByPage(TaskLog taskLog,Long userId);

	void queryTableIFExists(@Param("tableName")String tableName);

	Page<TaskLog> getAlarmData(Map<String, Object> map);

    List<Date> getTrendTotal(Map<String, Object> map);

	List<Date> getTrendException(Map<String, Object> map);

    List<TaskLog> getTaskDetail(Map<String, Object> map);

    void updateReadStatusBatch(Map<String, Object> map);

	void updateReadStatus(Map<String, Object> map);

    void updateByPrimaryKey(Map<String, Object> map);

	List<Map<String,Object>> getTrendByDay(Map<String, Object> map);

	List<Map<String,Object>> getTrendByHour(Map<String, Object> map);

	List<Map<String,Object>> getTrendByMonth(Map<String, Object> map);

	List<Map<String,Object>> getTrendTotalByDay(Map<String, Object> map);

	List<Map<String,Object>> getTrendTotalByHour(Map<String, Object> map);

	List<Map<String,Object>> getTrendTotalByMonth(Map<String, Object> map);

    void deleteByJobId(Map<String, Object> map);
}
