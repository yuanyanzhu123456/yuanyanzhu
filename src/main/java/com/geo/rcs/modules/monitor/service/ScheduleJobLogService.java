package com.geo.rcs.modules.monitor.service;


import com.geo.rcs.modules.monitor.entity.ScheduleJobLog;
import com.github.pagehelper.Page;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
public interface ScheduleJobLogService {

	/**
	 * 根据ID，查询定时任务日志
	 * @param jobId
	 * @param userId
	 * @return
	 */
	ScheduleJobLog queryObject(Long jobId,Long userId);
	/**
	 * 查询总数
	 * @param map
	 * @return
	 */
	int queryTotal(Map<String, Object> map);
	/**
	 * 保存定时任务日志
	 * @param log
	 */
	void save(ScheduleJobLog log);
	/**
	 * 任务列表
	 * @param scheduleJobLog
	 * @return
	 */
    Page<ScheduleJobLog> findByPage(ScheduleJobLog scheduleJobLog);
}
