package com.geo.rcs.modules.monitor.service.impl;

import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.monitor.dao.ScheduleTaskMapper;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.entity.TaskLog;
import com.geo.rcs.modules.monitor.service.DimensionService;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import com.geo.rcs.modules.monitor.service.TaskLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 定时任务
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
@Service("scheduleTaskService")
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
	@Autowired
	private ScheduleTaskMapper scheduleTaskMapper;
	@Autowired
	private TaskLogService taskLogService;
	@Autowired
	private DimensionService dimensionService;
	@Autowired
	private ScheduleJobService scheduleJobService;

	@Override
	public List<ScheduleTask> queryByJobId(Long jobId,Long uniqueCode) {
		HashMap<String, Object> map = new HashMap<>();
		String tableName = "monitor_task_"+uniqueCode/10+"_part";
		queryTableIFExists(tableName);
		map.put("tableName",tableName);
		map.put("jobId",jobId);
		map.put("userId",uniqueCode);
		return scheduleTaskMapper.queryByJobId(map);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveBatch(List<ScheduleTask> scheduleTasks) {
		String tableName = null;
		for (ScheduleTask scheduleTask : scheduleTasks) {
			Long userId = scheduleTask.getUserId();
			tableName = "monitor_task_"+userId/10+"_part";
			scheduleTask.setCreateTime(new Date());
			scheduleTask.setExecuteStatus(Constant.ScheduleStatus.EXECUTE.getValue());
		}
		queryTableIFExists(tableName);
		scheduleTaskMapper.saveTaskBatch(scheduleTasks,tableName);
	}
	@Override
	public Page<ScheduleTask> findByPage(Map<String, Object> map) {
		PageHelper.startPage((int)map.get("pageNo"), (int)map.get("pageSize"));
		String tableName = "monitor_task_"+(Long)map.get("userId")/10+"_part";
		//queryTableIFExists(tableName);
		map.put("tableName",tableName);
		Page<ScheduleTask> scheduleTasks = scheduleTaskMapper.findByPage(map);
		return scheduleTasks;
	}

	@Override
	public List<Dimension> getDimension(Long unicode) {
		return scheduleTaskMapper.getDimension(unicode);
	}

	@Override
	public void deleteBatch(Long[] taskIds,Long userId) {
		String tableName = "monitor_task_"+userId/10+"_part";
		queryTableIFExists(tableName);
		//删除数据
		scheduleTaskMapper.deleteTaskBatch(taskIds,tableName);
	}

	@Override
	public void queryTableIFExists(String tableName) {
		String s = scheduleTaskMapper.queryTableIFExists(tableName);
		if(ValidateNull.isNull(s)){
			scheduleTaskMapper.createTaskTable(tableName);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStatusBatch(List<ScheduleTask> scheduleTasks,Long userId) {
		ArrayList<Object> ids = new ArrayList<>();
		for (ScheduleTask scheduleTask : scheduleTasks) {
			ids.add(scheduleTask.getId());
		}
		String tableName = "monitor_task_"+userId/10+"_part";
		queryTableIFExists(tableName);
		HashMap<String, Object> map = new HashMap<>();
		map.put("ids",ids);
		map.put("tableName",tableName);
		map.put("taskStatus",Constant.DistributeStatus.RUNNING.getValue());
		map.put("distributeStatus", Constant.DistributeStatus.RUNNING.getValue());
		scheduleTaskMapper.updateBatch(map);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateDistributeStatusToRunning(List<ScheduleTask> scheduleTasks,Long userId) {
		ArrayList<Object> ids = new ArrayList<>();
		for (ScheduleTask scheduleTask : scheduleTasks) {
			ids.add(scheduleTask.getId());
		}
		String tableName = "monitor_task_"+userId/10+"_part";
		queryTableIFExists(tableName);
		HashMap<String, Object> map = new HashMap<>();
		map.put("ids",ids);
		map.put("tableName",tableName);
		map.put("taskStatus",Constant.DistributeStatus.RUNNING.getValue());
		map.put("distributeStatus", Constant.DistributeStatus.RUNNING.getValue());
		scheduleTaskMapper.updateBatch(map);
	}

	@Override
	public void updateDistributeStatusToInit(List<ScheduleTask> scheduleTasks,Long userId) {
		ArrayList<Object> ids = new ArrayList<>();
		for (ScheduleTask scheduleTask : scheduleTasks) {
			ids.add(scheduleTask.getId());
		}
		String tableName = "monitor_task_"+userId/10+"_part";
		queryTableIFExists(tableName);
		HashMap<String, Object> map = new HashMap<>();
		map.put("ids",ids);
		map.put("tableName",tableName);
		map.put("taskStatus",Constant.DistributeStatus.RUNNING.getValue());
		map.put("distributeStatus", Constant.DistributeStatus.INIT.getValue());
		scheduleTaskMapper.updateBatch(map);
	}

	@Override
	public List<ScheduleTask> queryByFilter(HashMap<String, Object> map) {
		String tableName = "monitor_task_"+(Long)map.get("userId")/10+"_part";
		queryTableIFExists(tableName);
		map.put("tableName",tableName);
		return scheduleTaskMapper.queryByFilter(map);
	}

	@Override
	public void updateStatusToInit(List<ScheduleTask> scheduleTasks,Long userId) {
		ArrayList<Object> ids = new ArrayList<>();
		Date updateTime=null;
		String tableName = "monitor_task_"+userId/10+"_part";
		queryTableIFExists(tableName);
		Integer executeStatus=0;
		Integer taskStatus=0;
		for (ScheduleTask scheduleTask : scheduleTasks) {
			ids.add(scheduleTask.getId());
			updateTime = scheduleTask.getUpdateTime();
			executeStatus = scheduleTask.getExecuteStatus();
			taskStatus = scheduleTask.getTaskStatus();
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("ids",ids);
		map.put("tableName",tableName);
		map.put("distributeStatus", Constant.DistributeStatus.INIT.getValue());
		map.put("updateTime", updateTime);
		map.put("executeStatus",executeStatus);
		map.put("taskStatus",taskStatus);
		scheduleTaskMapper.updateBatch(map);
	}

	@Override
	public ScheduleTask queryTaskStatusNum(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		taskLogService.queryTableIFExists(logTableName);

		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(taskTableName);
		map.put("logTableName",logTableName);
		map.put("taskTableName",taskTableName);
		return scheduleTaskMapper.queryTaskStatusNum(map);
	}

	@Override
	public void updateDistributeNumBatch(List<ScheduleTask> scheduleTasks, Long userId) {
		for (ScheduleTask scheduleTask : scheduleTasks) {
			scheduleTask.setDistributeNum(scheduleTask.getDistributeNum()+1);
		}
		String tableName = "monitor_task_"+userId/10+"_part";
		queryTableIFExists(tableName);
		scheduleTaskMapper.updateDistributeNumBatch(scheduleTasks,tableName);
	}

	@Override
	public void revertDistributeNumBatch(List<ScheduleTask> scheduleTasks, Long userId) {
		for (ScheduleTask scheduleTask : scheduleTasks) {
			scheduleTask.setDistributeNum(scheduleTask.getDistributeNum()-1);
		}
		String tableName = "monitor_task_"+userId/10+"_part";
		queryTableIFExists(tableName);
		scheduleTaskMapper.updateDistributeNumBatch(scheduleTasks,tableName);
	}

	@Override
	public void updateByPrimaryKey(Map<String, Object> map) {
		String tableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(tableName);
		map.put("tableName",tableName);
		scheduleTaskMapper.updateByPrimaryKey(map);
	}

	@Override
	public ScheduleTask queryObject(Map<String, Object> map) {
		String tableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(tableName);
		map.put("tableName",tableName);
		return scheduleTaskMapper.queryObject(map);
	}

	@Override
	public Page<ScheduleTask> getAlarmData(Map<String, Object> map) {
		String logTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(logTableName);
		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(taskTableName);
		map.put("logTableName",logTableName);
		map.put("taskTableName",taskTableName);
		PageHelper.startPage((int)map.get("pageNo"), (int)map.get("pageSize"));
		return scheduleTaskMapper.getAlarmData(map);
	}

	@Override
	public void deleteByJobId(Map<String, Object> hashMap) {
		scheduleTaskMapper.deleteByJobId(hashMap);
	}

	@Override
	public String getParamByJobId(Integer jobId) {
		return scheduleTaskMapper.getParamByJobId(jobId);
	}


}
