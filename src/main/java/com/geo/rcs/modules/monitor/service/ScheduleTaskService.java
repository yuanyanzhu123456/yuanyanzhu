package com.geo.rcs.modules.monitor.service;


import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.entity.TaskLog;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

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
public interface ScheduleTaskService {

	/**
	 * 根据ID，查询定时任务
	 */
	List<ScheduleTask> queryByJobId(@Param(value = "jobId") Long jobId,@Param(value = "userId") Long userId);

    void saveBatch(List<ScheduleTask> excelInfo);

	Page<ScheduleTask> findByPage(Map<String, Object> map);

    List<Dimension> getDimension(Long unicode);

	List<ScheduleTask> queryByFilter(HashMap<String, Object> map);
	/**
	 * 批量删除名单
	 */
	void deleteBatch(Long[] taskIds,Long userId);

	void queryTableIFExists(@Param("tableName")String tableName);

    void updateStatusBatch(List<ScheduleTask> scheduleTasks,Long userId);

    void updateDistributeStatusToInit(List<ScheduleTask> scheduleTasks,Long userId);

    void updateDistributeStatusToRunning(List<ScheduleTask> scheduleTasks,Long userId);

    void updateStatusToInit(List<ScheduleTask> scheduleTasks,Long userId);

	ScheduleTask queryTaskStatusNum(Map<String, Object> map);

	void updateDistributeNumBatch(List<ScheduleTask> scheduleTasks, Long userId);

	void revertDistributeNumBatch(List<ScheduleTask> scheduleTasks, Long userId);

	void updateByPrimaryKey(Map<String, Object> map);

    ScheduleTask queryObject(Map<String, Object> map);

	Page<ScheduleTask> getAlarmData(Map<String, Object> map);

	void deleteByJobId(Map<String, Object> hashMap);

	/**
	 * 根据任务编号获取参数集
	 * @param jobId
	 * @return
	 */
	String getParamByJobId(Integer jobId);
}
