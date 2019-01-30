package com.geo.rcs.modules.monitor.service.impl;

import com.geo.rcs.modules.monitor.dao.ScheduleJobLogMapper;
import com.geo.rcs.modules.monitor.entity.ScheduleJobLog;
import com.geo.rcs.modules.monitor.service.ScheduleJobLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {
	@Autowired
	private ScheduleJobLogMapper scheduleJobLogDao;
	
	@Override
	public ScheduleJobLog queryObject(Long jobId,Long userId) {
		return scheduleJobLogDao.queryObjectByUserId(jobId,userId);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return scheduleJobLogDao.queryTotal(map);
	}

	@Override
	public void save(ScheduleJobLog log) {
		scheduleJobLogDao.save(log);
	}

	@Override
	public Page<ScheduleJobLog> findByPage(ScheduleJobLog scheduleJobLog) {
			PageHelper.startPage(scheduleJobLog.getPageNo(), scheduleJobLog.getPageSize());
			return scheduleJobLogDao.findByPage(scheduleJobLog);
		}
	}

