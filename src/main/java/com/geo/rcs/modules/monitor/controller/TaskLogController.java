package com.geo.rcs.modules.monitor.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.entity.TaskLog;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import com.geo.rcs.modules.monitor.service.TaskLogService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月26日 下午2:07
 */
@RestController
@RequestMapping("monitor/taskLog")
public class TaskLogController extends BaseController{

    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;

    private static final String START_TIME = "startTime";

    private static final String END_TIME = "endTime";
    /**
     * 定时任务日志列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("sys:schedule:log")
    public Geo list(TaskLog taskLog){
        //查询列表数据
        final PageInfo<TaskLog> taskLogPageInfo = new PageInfo<>(taskLogService.findByPage(taskLog,getUser().getUniqueCode()));
        return Geo.ok().put("page", taskLogPageInfo);
    }

    /**
     * 定时任务日志信息
     */
    @RequestMapping("/info/{logId}")
    public Geo info(@PathVariable("logId") Long logId){
        Long userId = getUser().getUniqueCode();
        TaskLog log = taskLogService.queryObject(logId,userId);

        return Geo.ok().put("log", log);
    }

    /**
     * 查看监控详情
     */
    @RequestMapping("/getDetail")
    @RequiresPermissions("monitor:task:detail")
    public Geo getDetail(@RequestBody Map<String, Object> map){
        map.put("userId",getUser().getUniqueCode());
        List<TaskLog> logs = taskLogService.getTaskDetail(map);
        ScheduleTask taskInfo = scheduleTaskService.queryObject(map);
        map.clear();
        map.put("taskInfo",taskInfo);
        map.put("logs",logs);
        return Geo.ok().put("logs", map);
    }

    /**
     * 批量阅读
     */
    @SysLog("批量阅读")
    @RequestMapping("/readBatch")
    public Geo readBatch(@RequestBody Map<String, Object> map){
        map.put("userId",getUser().getUniqueCode());
        taskLogService.updateReadStatusBatch(map);
        return Geo.ok();
    }
    /**
     * 报警数据
     */
    @RequestMapping("/alarmData")
    public Geo alarmData(@RequestBody Map<String, Object> map){
        try {
            map.put("userId", getUser().getUniqueCode());
            PageInfo<TaskLog> pageInfo = new PageInfo<>(taskLogService.getAlarmData(map));
            return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", pageInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.error(" 名单列表 （模糊，分页）", map.toString(), getUser().getName(), e);
            return Geo.error();

        }
    }

    /**
     * 监控总量及异常趋势
     */
    @RequestMapping("/monitorTrend")
    @RequiresPermissions("monitor:task:trend")
    public Geo monitorTrend(@RequestBody Map<String, Object> map){
        List<Map<String,Object>> trendTotal = new ArrayList<>();
        List<Map<String,Object>> trendException = new ArrayList<>();
        if (map == null || map.isEmpty()) {
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "参数不能为空！");
        }
        String startTime = (String)map.get(START_TIME);
        String endTime = (String)map.get(END_TIME);
        if(StringUtils.stripToNull(startTime) == null || StringUtils.stripToNull(endTime) == null){
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "日期不能为空");
        }
        if(!DateUtils.compareTo(startTime, endTime)) {
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "结束时间不能大于开始时间");
        }
        Long userId = getUser().getUniqueCode();
        map.put("userId",userId);
        switch (map.get("type").toString()) {
            case "HOUR":
                trendException = taskLogService.getTrendByHour(map);
                trendTotal = taskLogService.getTrendTotalByHour(map);
                break;
            case "DAY":
                trendException= taskLogService.getTrendByDay(map);
                trendTotal= taskLogService.getTrendTotalByDay(map);
                break;
            case "MONTH":
                trendException = taskLogService.getTrendByMonth(map);
                trendTotal = taskLogService.getTrendTotalByMonth(map);
                break;
        }

        map.clear();
        map.put("trendTotal",trendTotal);
        map.put("trendException",trendException);
        return Geo.ok().put("data", map);
    }

}
