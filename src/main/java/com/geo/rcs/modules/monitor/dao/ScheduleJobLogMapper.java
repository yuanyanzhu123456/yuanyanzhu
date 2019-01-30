package com.geo.rcs.modules.monitor.dao;

import com.geo.rcs.modules.monitor.entity.ScheduleJobLog;
import com.geo.rcs.modules.sys.dao.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 定时任务日志
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
@Mapper
@Component(value = "scheduleJobLogDao")
public interface ScheduleJobLogMapper extends BaseMapper<ScheduleJobLog> {

    ScheduleJobLog queryObjectByUserId(@Param(value = "id") Long id, @Param(value = "userId") Long userId);

    Page<ScheduleJobLog> findByPage(ScheduleJobLog scheduleJobLog);
}
