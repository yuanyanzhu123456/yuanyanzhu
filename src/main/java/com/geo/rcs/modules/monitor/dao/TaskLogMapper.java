package com.geo.rcs.modules.monitor.dao;

import com.geo.rcs.modules.monitor.entity.TaskLog;
import com.geo.rcs.modules.sys.dao.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

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
@Mapper
@Component(value = "taskLogMapper")
public interface TaskLogMapper extends BaseMapper<TaskLog> {

    TaskLog queryObjectByUserId(@Param(value = "id") Long id, @Param(value = "userId") Long userId);

    Page<TaskLog> findByPage(TaskLog taskLog);

    Page<TaskLog> getAlarmData(Map<String,Object> map);

    List<Date> getTrendTotal(Map<String, Object> map);

    List<Date> getTrendException(Map<String, Object> map);

    List<TaskLog> getTaskDetail(Map<String, Object> map);

    List<Map<String,Object>> getTrendByDay(Map<String, Object> map);

    List<Map<String,Object>> getTrendByHour(Map<String, Object> map);

    List<Map<String,Object>> getTrendByMonth(Map<String, Object> map);

    List<Map<String,Object>> getTrendTotalByDay(Map<String, Object> map);

    List<Map<String,Object>> getTrendTotalByHour(Map<String, Object> map);

    List<Map<String,Object>> getTrendTotalByMonth(Map<String, Object> map);

    void updateReadStatusBatch(Map<String, Object> map);

    void updateReadStatus(Map<String, Object> map);

    void updateByPrimaryKey(Map<String, Object> map);

    void deleteByJobId(Map<String, Object> map);
}
