package com.geo.rcs.modules.monitor.dao;

import com.geo.rcs.modules.abtest.entity.AbScheduleTask;
import com.geo.rcs.modules.abtest.entity.AbTest;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.sys.dao.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

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
@Mapper
@Component(value = "scheduleTaskDao")
public interface ScheduleTaskMapper extends BaseMapper<ScheduleJob> {

    List<ScheduleTask> queryByJobId(HashMap<String, Object> map);

    Page<ScheduleTask> findByPage(Map<String, Object> map);

    void saveTaskBatch(@Param(value = "list")List<ScheduleTask> scheduleTasks,@Param(value = "tableName")String tableName);

    List<ScheduleTask> queryByFilter(HashMap<String, Object> map);

    void deleteTaskBatch(@Param(value = "taskIds")Long[] taskIds,@Param(value = "tableName") String tableName);

    void deleteByJobId(Map<String, Object> map);

    ScheduleTask queryTaskStatusNum(Map<String, Object> map);

    ScheduleTask queryTotalByUserId(Map<String, Object> map);

    void updateDistributeNumBatch(@Param(value = "list")List<ScheduleTask> scheduleTasks,@Param(value = "tableName")String tableName);

    void updateByPrimaryKey(Map<String, Object> map);

    ScheduleTask queryObject(Map<String, Object> map);

    Page<ScheduleTask> getAlarmData(Map<String, Object> map);

    void updateTaskStatus(Map<String, Object> map);

    void createAbTaskTable(@Param("tableName")String tableName);

    void saveAbTaskBatch(@Param(value = "list")List<AbScheduleTask> scheduleTasks, @Param(value = "tableName")String tableName);

    Page<AbScheduleTask> findAbTaskByPage(Map<String, Object> map);

    Page<AbScheduleTask> findAbTaskByPageAll(Map<String, Object> map);

    AbScheduleTask getAbTaskDetail(Map<String, Object> map);

    List<AbScheduleTask> getTaskByJobId(Map<String, Object> map);
    /**
     * 根据任务编号获取参数集
     * @param jobId
     * @return
     */
    String getParamByJobId(Integer jobId);
    /**
     * 获取已执行完成的子任务
     * @param
     * @return
     */
    List<ScheduleTask> getOverTaskList(HashMap<String, Object> map);
}
