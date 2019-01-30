package com.geo.rcs.modules.abtest.task;

import com.geo.rcs.common.schedule.ScheduleUtils;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import com.geo.rcs.modules.monitor.service.TaskLogService;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.task
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年08月28日 上午10:10
 */
@Component("cleanAbTestJobsOnTime")
public class CleanAbTestJobsOnTime {

    @Resource
    private Scheduler scheduler;

    @Autowired
    private TaskLogService taskLogService;

    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @Autowired
    private ScheduleJobService scheduleJobService;

    @Transactional
    public void cleanJobsOnTime(Long parm1,Long parm2){

        long start = System.currentTimeMillis();

        Map<String, Object> hashMap = new HashMap<>();
        try{
            //查找需要删除的任务编号
            List<ScheduleJob> scheduleJobs =  scheduleJobService.scanOutOfDateJobs();

            for (ScheduleJob scheduleJob : scheduleJobs) {
                String tableName = "monitor_task_"+scheduleJob.getUserId()/10+"_part";
                String logTableName = "monitor_task_log_"+scheduleJob.getUserId()/10+"_part";
                scheduleTaskService.queryTableIFExists(tableName);
                hashMap.put("jobId",scheduleJob.getJobId());
                hashMap.put("tableName",tableName);
                hashMap.put("logTableName",logTableName);
                scheduleJobService.deleteByPrimaryKey(hashMap);
                ScheduleUtils.deleteScheduleJob(scheduler,scheduleJob.getJobId());
                scheduleTaskService.deleteByJobId(hashMap);
                taskLogService.deleteByJobId(hashMap);
            }
        }catch (Exception e){
            LogUtil.error("定时删除任务", "耗时:"+(System.currentTimeMillis() - start)+"ms", "系统", e);
            System.out.printf("耗时:"+(System.currentTimeMillis() - start)+"ms");
        }
    }
}
