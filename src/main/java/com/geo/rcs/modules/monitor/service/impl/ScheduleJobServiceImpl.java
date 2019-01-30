package com.geo.rcs.modules.monitor.service.impl;

import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.schedule.ScheduleUtils;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.monitor.dao.ScheduleJobMapper;
import com.geo.rcs.modules.monitor.dao.ScheduleTaskMapper;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import com.geo.rcs.modules.monitor.service.TaskLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.crypto.hash.Hash;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {
	@Resource
	private Scheduler scheduler;
	@Autowired
	private ScheduleJobMapper schedulerJobDao;
	@Autowired
	private ScheduleTaskMapper scheduleTaskMapper;
	@Autowired
	private ScheduleTaskService scheduleTaskService;
	@Autowired
	private TaskLogService taskLogService;

	@Override
	public ScheduleJob queryObject(Long jobId,Long userId) {
		return schedulerJobDao.queryObjectByUserId(jobId,userId);
	}

	@Override
	public List<ScheduleJob> queryList(Map<String, Object> map) {
		return schedulerJobDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return schedulerJobDao.queryTotal(map);
	}

	@Override
	@Transactional
	public ScheduleJob save(ScheduleJob scheduleJob) {
		scheduleJob.setCreateTime(new Date());
		scheduleJob.setBeanName("scanningTask");
		scheduleJob.setMethodName("scanNing");
		scheduleJob.setCronExpression("*/5 * * * * ?");
		scheduleJob.setStatus(Constant.ScheduleStatus.PAUSE.getValue());
		scheduleJob.setMonitoredNum(Constant.ScheduleStatus.NORMAL.getValue());
		schedulerJobDao.save(scheduleJob);
		ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
		return scheduleJob;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ScheduleJob scheduleJob) {
		//ScheduleJob oldScheduleJob = schedulerJobDao.queryObjectByUserId(scheduleJob.getJobId(), scheduleJob.getUserId());
		ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
		//修改周期数大于原先的期数，把监控完成的task状态改成继续监控
		/*if(oldScheduleJob != null && scheduleJob.getCycleNum() > oldScheduleJob.getCycleNum()){
			HashMap<String, Object> map = new HashMap<>();
			String tableName = "monitor_task_"+scheduleJob.getUserId()/10+"_part";
			queryTableIFExists(tableName);
			map.put("tableName",tableName);
			map.put("jobId",scheduleJob.getJobId());
			List<ScheduleTask> overTaskList = scheduleTaskMapper.getOverTaskList(map);
			for (ScheduleTask scheduleTask : overTaskList) {
				HashMap<String, Object> map1 = new HashMap<>();
				map1.put("tableName",tableName);
				map1.put("taskId",scheduleTask.getId());
				map1.put("taskStatus",1);
				scheduleTaskMapper.updateByPrimaryKey(map1);
			}
		}*/

		schedulerJobDao.update(scheduleJob);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] jobIds) {
		for(Long jobId : jobIds){
			ScheduleUtils.deleteScheduleJob(scheduler, jobId);
			ScheduleJob scheduleJob = schedulerJobDao.queryObject(jobId);
			HashMap hashMap = new HashMap();
			String tableName = "monitor_task_"+scheduleJob.getUserId()/10+"_part";
			String logTableName = "monitor_task_log_"+scheduleJob.getUserId()/10+"_part";
			scheduleTaskService.queryTableIFExists(tableName);
			hashMap.put("jobId",scheduleJob.getJobId());
			hashMap.put("tableName",tableName);
			hashMap.put("logTableName",logTableName);
			scheduleTaskMapper.deleteByJobId(hashMap);
			taskLogService.deleteByJobId(hashMap);
		}

		//删除数据
		schedulerJobDao.deleteBatch(jobIds);
	}

	@Override
	public int updateBatch(Long[] jobIds, int status){
		Map<String, Object> map = new HashMap<>();
		map.put("list", jobIds);
		map.put("status", status);
		return schedulerJobDao.updateBatch(map);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void run(Long[] jobIds,Long userId) {
		for(Long jobId : jobIds){
			ScheduleUtils.run(scheduler, queryObject(jobId,userId));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void pause(Long[] jobIds) {
		for(Long jobId : jobIds){
			ScheduleUtils.pauseJob(scheduler, jobId);
		}

		updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
	}

	@Override
	@Transactional
	public void resume(Long[] jobIds) {
		for(Long jobId : jobIds){
			ScheduleUtils.resumeJob(scheduler, jobId);
		}

		updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());
	}

	@Override
	public List<ScheduleJob> queryOverList(Map<String, Object> map) {
		String tableName = "monitor_task_"+(Long)map.get("userId")/10+"_part";
		scheduleTaskService.queryTableIFExists(tableName);
		map.put("taskTableName",tableName);
		return schedulerJobDao.queryOverList(map);
	}

	@Override
	public int queryOverTotal(Map<String, Object> map) {
		return schedulerJobDao.queryOverTotal(map);
	}

	@Override
	public List<ScheduleJob> queryListByUserId(Map<String, Object> map) {
		String tableName = "monitor_task_"+(Long)map.get("userId")/10+"_part";
		scheduleTaskService.queryTableIFExists(tableName);
		map.put("taskTableName",tableName);
		return schedulerJobDao.queryListByUserId(map);
	}

	@Override
	public ScheduleJob queryJobStatusNum(Long userId) {
		return schedulerJobDao.queryJobStatusNum(userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateForDelete(ScheduleJob scheduleJob) {
		ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
		ScheduleUtils.pauseJob(scheduler, scheduleJob.getJobId());
		//判断执行期数是否达到阈值
		/*if(scheduleJob.getMonitoredNum() >= scheduleJob.getCycleNum()){
			//任务结束
			scheduleJob.setStatus(Constant.ScheduleStatus.OVER.getValue());
			ScheduleUtils.pauseJob(scheduler, scheduleJob.getJobId());
		}*/
		schedulerJobDao.update(scheduleJob);
	}

	@Override
	public List<ScheduleTask> beforeDelete(Map<String, Object> map) {
		String tableName = "monitor_task_"+(Long)map.get("userId")/10+"_part";
		scheduleTaskService.queryTableIFExists(tableName);
		map.put("taskTableName",tableName);
		return schedulerJobDao.beforeDelete(map);
	}

	@Override
	public List<ScheduleJob> scanOutOfDateJobs() {
		return schedulerJobDao.scanOutOfDateJobs();
	}

	@Override
	public List<ScheduleJob> selectAllUsedJobs() {
		return schedulerJobDao.selectAllUsedJobs();
	}

	@Override
	public void deleteByPrimaryKey(Map<String, Object> hashMap) {
		schedulerJobDao.deleteByPrimaryKey(hashMap);
	}

	@Override
	public Page<ScheduleJob> findByPage(ScheduleJob scheduleJob) {
		PageHelper.startPage(scheduleJob.getPageNo(), scheduleJob.getPageSize());
		return schedulerJobDao.findByPage(scheduleJob);
	}

	@Override
	public ScheduleJob queryAlarm(Map<String, Object> map) {
		String tableName = "monitor_task_"+(Long)map.get("userId")/10+"_part";
		scheduleTaskService.queryTableIFExists(tableName);
		map.put("taskTableName",tableName);
		return schedulerJobDao.queryAlarm(map);
	}

	@Override
	public Long queryHandleTime(Map<String, Object> map) {
		String tableName = "monitor_task_log_"+(Long)map.get("userId")/10+"_part";
		scheduleTaskService.queryTableIFExists(tableName);
		map.put("taskTableName",tableName);
		return schedulerJobDao.queryHandleTime(map);
	}

	@Override
	public Map<String, Object> getCountMap(Map<String, Object> map) {
		String tableName = "monitor_task_log_"+(Long)map.get("userId")/10+"_part";
		scheduleTaskService.queryTableIFExists(tableName);
		map.put("taskTableName",tableName);
		return schedulerJobDao.getCountMap(map);
	}

	@Override
	public  List<Map<String,Object>> getAlarmData(Map<String, Object> map) {
		String taskTableName = "monitor_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
        String taskLogTableName = "monitor_task_log_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
		queryTableIFExists(taskTableName);
		map.put("taskTableName",taskTableName);
		map.put("taskLogTableName",taskLogTableName);
		return schedulerJobDao.getAlarmData(map);
	}

	@Override
	public void queryTableIFExists(String tableName) {
		String s = scheduleTaskMapper.queryTableIFExists(tableName);
		if(ValidateNull.isNull(s)){
			scheduleTaskMapper.createTaskTable(tableName);
		}
	}

	@Override
	public List<ScheduleJob> getAllDimension() {
		return schedulerJobDao.getAllDimension();
	}

}
