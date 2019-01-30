package com.geo.rcs.modules.monitor.service;


import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 定时任务
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
public interface ScheduleJobService {

	/**
	 * 根据ID，查询定时任务
	 */
	ScheduleJob queryObject(@Param(value = "jobId")Long jobId,@Param(value = "userId")Long userId);

	/**
	 * 查询定时任务列表
	 */
	List<ScheduleJob> queryList(Map<String, Object> map);

	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 保存定时任务
	 */
	ScheduleJob save(ScheduleJob scheduleJob);

	/**
	 * 更新定时任务
	 */
	void update(ScheduleJob scheduleJob);

	/**
	 * 批量删除定时任务
	 */
	void deleteBatch(Long[] jobIds);

	/**
	 * 批量更新定时任务状态
	 */
	int updateBatch(Long[] jobIds, int status);

	/**
	 * 立即执行
	 */
	void run(Long[] jobIds,Long userId);

	/**
	 * 暂停运行
	 */
	void pause(Long[] jobIds);

	/**
	 * 恢复运行
	 */
	void resume(Long[] jobIds);

	/**
	 * 查询失效任务列表
	 */
	List<ScheduleJob> queryOverList(Map<String, Object> map);

	/**
	 * 查询失效总数
	 */
	int queryOverTotal(Map<String, Object> map);

    List<ScheduleJob> queryListByUserId(Map<String, Object> map);

	ScheduleJob queryJobStatusNum(Long userId);

	void updateForDelete(ScheduleJob scheduleJob);

	List<ScheduleTask> beforeDelete(Map<String, Object> map);

	List<ScheduleJob> scanOutOfDateJobs();

	List<ScheduleJob> selectAllUsedJobs();

	void deleteByPrimaryKey(Map<String, Object> hashMap);

	Page<ScheduleJob> findByPage(ScheduleJob scheduleJob);

	/**
	 * 获取报警数、任务名、任务id
	 */
	ScheduleJob queryAlarm(Map<String, Object> map);
	/**
	 * 处理时长
	 */
	Long queryHandleTime(Map<String, Object> map);

	/**
	 * 获取条数
	 */
	Map<String, Object> getCountMap(Map<String, Object> map);

	/**
	 * 报警清单
	 */
	List<Map<String,Object>> getAlarmData(Map<String, Object> map);

	void queryTableIFExists(@Param("tableName")String tableName);

	/**
	 * 获取所有任务中的维度编号
	 * @return
	 */
	List<ScheduleJob> getAllDimension();
}
