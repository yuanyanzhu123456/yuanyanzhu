package com.geo.rcs.modules.monitor.dao;

import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.sys.dao.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

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
@Component(value = "scheduleJobDao")
public interface ScheduleJobMapper extends BaseMapper<ScheduleJob> {

    /**
     * 根据用户编号查找任务
     * @param id
     * @param userId
     * @return
     */
    ScheduleJob queryObjectByUserId(@Param(value = "id") Long id,@Param(value = "userId") Long userId);

    /**
     * 获取过期的任务列表
     * @param map
     * @return
     */
    List<ScheduleJob> queryOverList(Map<String, Object> map);

    /**
     * 获取过期任务总数
     * @param map
     * @return
     */
    int queryOverTotal(Map<String, Object> map);

    /**
     * 根据用户编号获取任务列表
     * @param map
     * @return
     */
    List<ScheduleJob> queryListByUserId(Map<String, Object> map);

    /**
     * 获取任务状态总数
     * @param userId
     * @return
     */
    ScheduleJob queryJobStatusNum(Long userId);

    /**
     * 删除之前查询是否有子任务
     * @param map
     * @return
     */
    List<ScheduleTask> beforeDelete(Map<String, Object> map);

    /**
     * 查找过期的任务
     * @return
     */
    List<ScheduleJob> scanOutOfDateJobs();

    List<ScheduleJob> selectAllUsedJobs();

    /**
     * 根据编号删除任务
     * @param hashMap
     */
    void deleteByPrimaryKey(Map<String, Object> hashMap);

    /**
     * 获取任务列表（分页）
     * @param scheduleJob
     * @return
     */
    Page<ScheduleJob> findByPage(ScheduleJob scheduleJob);

    ScheduleJob queryAlarm(Map<String, Object> map);

    Long queryHandleTime(Map<String, Object> map);

    Map<String, Object> getCountMap(Map<String, Object> map);

    List<Map<String,Object>> getAlarmData(Map<String, Object> map);

    /**
     * 获取所有任务中的维度编号
     * @return
     */
    List<ScheduleJob> getAllDimension();
}
