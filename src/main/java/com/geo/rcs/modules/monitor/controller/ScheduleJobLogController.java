package com.geo.rcs.modules.monitor.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.monitor.entity.ScheduleJobLog;
import com.geo.rcs.modules.monitor.service.ScheduleJobLogService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 定时任务日志
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController extends BaseController{
    @Autowired
    private ScheduleJobLogService scheduleJobLogService;

    /**
     * 定时任务日志列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:schedule:log")
    public Geo list(ScheduleJobLog scheduleJobLog){
        //查询列表数据
        final PageInfo<ScheduleJobLog> scheduleJobLogPageInfo = new PageInfo<>(scheduleJobLogService.findByPage(scheduleJobLog));
        return Geo.ok().put("page", scheduleJobLogPageInfo);
    }

    /**
     * 定时任务日志信息
     */
    @RequestMapping("/info/{logId}")
    public Geo info(@PathVariable("logId") Long logId){
        Long userId = getUser().getUniqueCode();
        ScheduleJobLog log = scheduleJobLogService.queryObject(logId,userId);

        return Geo.ok().put("log", log);
    }
}
