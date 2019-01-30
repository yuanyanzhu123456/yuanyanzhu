package com.geo.rcs.common.schedule;

import com.geo.rcs.common.util.SpringContextUtils;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import com.geo.rcs.modules.monitor.entity.ScheduleJobLog;
import com.geo.rcs.modules.monitor.service.ScheduleJobLogService;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * 定时任务
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
public class ScheduleJob extends QuartzJobBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ExecutorService service = Executors.newSingleThreadExecutor();
	//预定更新时间
	private static Date reserveTime;
	
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        com.geo.rcs.modules.monitor.entity.ScheduleJob scheduleJob = (com.geo.rcs.modules.monitor.entity.ScheduleJob) context.getMergedJobDataMap()
        		.get(com.geo.rcs.modules.monitor.entity.ScheduleJob.JOB_PARAM_KEY);
        
        //获取spring bean
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) SpringContextUtils.getBean("scheduleJobLogService");
        ScheduleJobService scheduleJobService = (ScheduleJobService) SpringContextUtils.getBean("scheduleJobService");

		com.geo.rcs.modules.monitor.entity.ScheduleJob scheduleJob1 = scheduleJobService.queryObject(scheduleJob.getJobId(), scheduleJob.getUserId());
		//数据库保存执行记录
        ScheduleJobLog log = new ScheduleJobLog();
        log.setJobId(scheduleJob1.getJobId());
        log.setBeanName(scheduleJob1.getBeanName());
        log.setMethodName(scheduleJob1.getMethodName());
        log.setCreateTime(new Date());

        //任务开始时间
        long startTime = System.currentTimeMillis();
        
        try {
            //执行任务
//        	logger.info("任务准备执行，任务ID：" + scheduleJob1.getJobId());
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob1.getBeanName(),
					scheduleJob1.getMethodName(), scheduleJob1.getJobId(),scheduleJob1.getUserId());
            Future<?> future = service.submit(task);
            
			future.get();
			
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int)times);
			//任务状态    0：成功    1：失败
			log.setStatus(0);
			
//			logger.info("" +
//					"\n任务执行完毕，任务ID：" + scheduleJob1.getJobId() + "  总共耗时：" + times + "毫秒\n");

		} catch (Exception e) {
			logger.error("\n任务执行失败，任务ID：" + scheduleJob1.getJobId(), e);
			
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int)times);
			
			//任务状态    0：成功    1：失败
			log.setStatus(1);
			log.setError(StringUtils.substring(e.toString(), 0, 2000));
		}finally {
			//天
			if(scheduleJob1.getMonitorUnit() == 1){
				//预定执行时间
				reserveTime = DatetimeFormattor.getRecentDateime(-scheduleJob1.getInterval());
			}//月
			else if(scheduleJob1.getMonitorUnit() == 2){
				reserveTime = DatetimeFormattor.getRecentMonthDateTime(-scheduleJob1.getInterval());
			}
			if(scheduleJob1.getUpdateTime() == null){
				scheduleJob1.setMonitoredNum(scheduleJob1.getMonitoredNum()+1);
				scheduleJob1.setUpdateTime(new Date());
			}
			else if(reserveTime.compareTo(scheduleJob1.getUpdateTime()) == 1 || reserveTime.compareTo(scheduleJob1.getUpdateTime()) == 0 ){
				//若更新时间小于预定更新时间
				scheduleJob1.setMonitoredNum(scheduleJob1.getMonitoredNum()+1);
				scheduleJob1.setUpdateTime(new Date());
			}
			//scheduleJobService.update(scheduleJob1);
			//scheduleJobLogService.save(log);
		}
    }

	public ScheduleJob() {
	}
}
