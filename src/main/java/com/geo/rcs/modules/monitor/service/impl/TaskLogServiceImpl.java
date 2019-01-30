package com.geo.rcs.modules.monitor.service.impl;

import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.monitor.dao.TaskLogMapper;
import com.geo.rcs.modules.monitor.entity.TaskLog;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import com.geo.rcs.modules.monitor.service.TaskLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务日志
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
@Service("taskLogService")
public class TaskLogServiceImpl implements TaskLogService {
	@Autowired
	private TaskLogMapper taskLogMapper;
	@Autowired
	private ScheduleTaskService scheduleTaskService;

	@Override
	public TaskLog queryObject(Long jobId, Long userId) {
		return taskLogMapper.queryObjectByUserId(jobId,userId);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return taskLogMapper.queryTotal(map);
	}

	@Override
	public void save(TaskLog log,Long userId) {
		String tableName = "monitor_task_log_"+userId/10+"_part";
		queryTableIFExists(tableName);
		log.setTableName(tableName);
		taskLogMapper.save(log);
	}

	@Override
	public void update(TaskLog log, Long userId) {
		String tableName = "monitor_task_log_"+userId/10+"_part";
		queryTableIFExists(tableName);
		log.setTableName(tableName);
		taskLogMapper.update(log);
	}

	@Override
	public Page<TaskLog> findByPage(TaskLog taskLog,Long userId) {
			String tableName = "monitor_task_log_"+userId/10+"_part";
			queryTableIFExists(tableName);
			PageHelper.startPage(taskLog.getPageNo(), taskLog.getPageSize());
			taskLog.setTableName(tableName);
			return taskLogMapper.findByPage(taskLog);
		}


	@Override
	public void queryTableIFExists(String tableName) {
		String s = taskLogMapper.queryTableIFExists(tableName);
		if(ValidateNull.isNull(s)){
			taskLogMapper.createTaskLogTable(tableName);
		}
	}

	@Override
	public Page<TaskLog> getAlarmData(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(taskTableName);
		map.put("logTableName",logTableName);
		map.put("taskTableName",taskTableName);
		PageHelper.startPage((int)map.get("pageNo"), (int)map.get("pageSize"));
		return taskLogMapper.getAlarmData(map);
	}

	@Override
	public List<Date> getTrendTotal(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		scheduleTaskService.queryTableIFExists(taskTableName);
		map.put("logTableName",logTableName);
		map.put("taskTableName",taskTableName);
		return taskLogMapper.getTrendTotal(map);
	}

	@Override
	public List<Date> getTrendException(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		scheduleTaskService.queryTableIFExists(taskTableName);
		map.put("logTableName",logTableName);
		map.put("taskTableName",taskTableName);
		return taskLogMapper.getTrendException(map);
	}

	@Override
	public List<TaskLog> getTaskDetail(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		map.put("tableName",logTableName);
		return taskLogMapper.getTaskDetail(map);
	}

	@Override
	public void updateReadStatusBatch(Map<String, Object> map) {
		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(taskTableName);
		map.put("taskTableName",taskTableName);
		taskLogMapper.updateReadStatusBatch(map);
	}

	@Override
	public void updateReadStatus(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		map.put("tableName",logTableName);
		taskLogMapper.updateReadStatus(map);
	}

	@Override
	public void updateByPrimaryKey(Map<String, Object> map) {
		String tableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(tableName);
		map.put("tableName",tableName);
		map.put("operateTime",new Date());
		taskLogMapper.updateByPrimaryKey(map);
	}

	@Override
	public List<Map<String,Object>> getTrendByDay(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		map.put("tableName",logTableName);
		return taskLogMapper.getTrendByDay(map);
	}

	@Override
	public List<Map<String,Object>> getTrendByHour(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		map.put("tableName",logTableName);
		return taskLogMapper.getTrendByHour(map);
	}

	@Override
	public List<Map<String,Object>> getTrendByMonth(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		map.put("tableName",logTableName);
		return taskLogMapper.getTrendByMonth(map);
	}

	@Override
	public List<Map<String,Object>> getTrendTotalByDay(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		scheduleTaskService.queryTableIFExists(taskTableName);
		map.put("logTableName",logTableName);
		map.put("taskTableName",taskTableName);
		return taskLogMapper.getTrendTotalByDay(map);
	}

	@Override
	public List<Map<String,Object>> getTrendTotalByHour(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		scheduleTaskService.queryTableIFExists(taskTableName);
		map.put("logTableName",logTableName);
		map.put("taskTableName",taskTableName);
		return taskLogMapper.getTrendTotalByHour(map);
	}

	@Override
	public List<Map<String,Object>> getTrendTotalByMonth(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		scheduleTaskService.queryTableIFExists(taskTableName);
		map.put("logTableName",logTableName);
		map.put("taskTableName",taskTableName);
		return taskLogMapper.getTrendTotalByMonth(map);
	}

	@Override
	public void deleteByJobId(Map<String, Object> map) {
		taskLogMapper.deleteByJobId(map);
	}

}

