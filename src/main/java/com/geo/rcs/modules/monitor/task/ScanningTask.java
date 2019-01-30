package com.geo.rcs.modules.monitor.task;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import com.geo.rcs.modules.jobs.handler.JobCliHandler;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.task
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月18日 下午6:09
 */
@Component("scanningTask")
public class ScanningTask extends BaseController{

    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @Autowired
    private ScheduleJobService scheduleJobService;

    @Autowired
    private SendMessage sendMessage;

    //预定执行时间
    private static Date reserveTime;

    /**
     * 每小时扫描一次名单表，执行任务
     *
     * @param jobId
     */
    @Transactional
    public synchronized Geo scanNing(Long jobId, Long userId) {

        //根据任务编号及用户编号获取任务信息
        ScheduleJob scheduleJob = scheduleJobService.queryObject(jobId, userId);
        if(scheduleJob == null){
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(),StatusCode.PARAMS_ERROR.getMessage());
        }
        //天
        if(scheduleJob.getMonitorUnit() == 1){
            //预定执行时间
            reserveTime = DatetimeFormattor.getRecentDateime(-scheduleJob.getInterval());
            //月
        }else if(scheduleJob.getMonitorUnit() == 2){
            reserveTime = DatetimeFormattor.getRecentMonthDateTime(-scheduleJob.getInterval());
            //周
        } else if(scheduleJob.getMonitorUnit() == 3){
            reserveTime = DatetimeFormattor.getRecentDateime(-(scheduleJob.getInterval()*7));
        }
        HashMap<String,Object> map = new HashMap<>();
        //根据条件筛选出需要执行的任务
        map.put("jobId",jobId);
        map.put("userId",userId);
        map.put("nowTime", new Date());
        map.put("reserveTime", reserveTime);
        map.put("limit", 1000);
        List<ScheduleTask> scheduleTasks = scheduleTaskService.queryByFilter(map);
        if(scheduleTasks.size()<=0){
            System.out.println(JobCliHandler.ANSI_GREEN + "========= Scheduler running at "+
                    JobCliHandler.time2String(System.currentTimeMillis())+"========= " + JobCliHandler.ANSI_RESET);

            System.out.printf(MessageFormat.format("JobID[{0}] :: No task needs to be distributed. \n\n", jobId));
            return Geo.ok();
        }
        long start = System.currentTimeMillis();
        int totalCount = scheduleTasks.size();
        ConcurrentHashMap<String,Object> hashMap = new ConcurrentHashMap<>();

        //推送到队列等待消费
        hashMap.put("jobId",jobId);
        hashMap.put("userId",userId);
        hashMap.put("scheduleTasks",scheduleTasks);

        //将需要执行的名单状态修改：分发状态改为执行中，分发次数+1
        scheduleTaskService.updateDistributeStatusToRunning(scheduleTasks,userId);
        scheduleTaskService.updateDistributeNumBatch(scheduleTasks,userId);

        List<ScheduleTask> excessScheduleTask = sendMessage.addToQueue(hashMap);

        //将超额的任务状态回滚
        if(excessScheduleTask.size()>0){
            scheduleTaskService.updateDistributeStatusToInit(excessScheduleTask,userId);
            scheduleTaskService.revertDistributeNumBatch(excessScheduleTask,userId);
        }
        System.out.println(JobCliHandler.ANSI_GREEN + "========= Scheduler running at "+
                JobCliHandler.time2String(System.currentTimeMillis())+"========= " + JobCliHandler.ANSI_RESET);


        int excessCount = excessScheduleTask.size();
        long costTime = (System.currentTimeMillis() - start);

        System.out.printf(MessageFormat.format("JobID[{0}] ::Distributed over. {1} success, {2} excess. {3}ms. \n  ",
                jobId, totalCount-excessCount, excessCount, costTime));
        return Geo.ok();
    }
}
