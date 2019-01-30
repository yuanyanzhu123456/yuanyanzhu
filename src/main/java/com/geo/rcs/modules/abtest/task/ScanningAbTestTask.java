package com.geo.rcs.modules.abtest.task;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.abtest.entity.AbScheduleTask;
import com.geo.rcs.modules.abtest.entity.AbTest;
import com.geo.rcs.modules.abtest.service.AbTestService;
import com.geo.rcs.modules.jobs.handler.JobCliHandler;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
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
@Component("scanningAbTestTask")
public class ScanningAbTestTask extends BaseController{

    @Autowired
    private AbTestService abTestService;

    @Autowired
    private SendAbTestMessage sendMessage;

    /**
     * 每小时扫描一次名单表，执行任务
     *
     * @param jobId
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized Geo scanNing(Long jobId,Long uniqueCode) {

        //根据任务编号及用户编号获取任务信息
        AbTest abTest = abTestService.getAbTestJobByPrimaryKey(jobId);
        if(abTest == null){
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(),StatusCode.PARAMS_ERROR.getMessage());
        }
        HashMap<String,Object> map = new HashMap<>();
        //根据条件筛选出需要执行的任务
        map.put("jobId",jobId);
        map.put("uniqueCode",uniqueCode);
        map.put("nowTime", new Date());
        map.put("limit", 1000);
        List<AbScheduleTask> scheduleTasks = abTestService.queryByFilter(map);

        if(scheduleTasks.size()>0){
            long start = System.currentTimeMillis();
            int totalCount = scheduleTasks.size();
            ConcurrentHashMap<String,Object> hashMap = new ConcurrentHashMap<>();
            //推送到队列等待消费
            hashMap.put("jobId",jobId);
            hashMap.put("uniqueCode",uniqueCode);
            hashMap.put("eventType",abTest.getEventType());
            hashMap.put("ruleType",abTest.getRuleType());
            hashMap.put("scheduleTasks",scheduleTasks);

            //将需要执行的名单状态修改：分发状态改为执行中，分发次数+1
            abTestService.updateDistributeStatusToRunning(scheduleTasks,uniqueCode);

            List<AbScheduleTask> excessScheduleTask = sendMessage.addToQueue(hashMap);

            //将超额的任务状态回滚
            if(excessScheduleTask.size()>0){
                abTestService.updateDistributeStatusToInit(excessScheduleTask,uniqueCode);
            }
            System.out.println(JobCliHandler.ANSI_GREEN + "========= Scheduler running at "+
                    JobCliHandler.time2String(System.currentTimeMillis())+"========= " + JobCliHandler.ANSI_RESET);


            int excessCount = excessScheduleTask.size();
            long costTime = (System.currentTimeMillis() - start);

            System.out.printf(MessageFormat.format("JobID[{0}] ::Distributed over. {1} success, {2} excess. {3}ms. \n",
                    jobId, totalCount-excessCount, excessCount, costTime));
            return Geo.ok();

        }
        else{
            System.out.println(JobCliHandler.ANSI_GREEN + "========= Scheduler running at "+
                    JobCliHandler.time2String(System.currentTimeMillis())+"========= " + JobCliHandler.ANSI_RESET);

            System.out.printf(MessageFormat.format("JobID[{0}] :: No task needs to be distributed! Waiting for workers cellBack. \n\n", jobId));
            //abTestService.deleteBatch(jobId);
            return Geo.ok();
        }
    }
}
