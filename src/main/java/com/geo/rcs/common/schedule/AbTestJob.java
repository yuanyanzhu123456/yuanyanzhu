package com.geo.rcs.common.schedule;

import com.geo.rcs.common.util.SpringContextUtils;
import com.geo.rcs.modules.abtest.service.AbTestService;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import com.geo.rcs.modules.jobs.handler.JobCliHandler;
import com.geo.rcs.modules.monitor.entity.ScheduleJobLog;
import com.geo.rcs.modules.monitor.service.ScheduleJobLogService;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.MessageFormat;
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
public class AbTestJob extends QuartzJobBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ExecutorService service = Executors.newSingleThreadExecutor();
	//预定更新时间
	private static Date reserveTime;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        com.geo.rcs.modules.abtest.entity.AbTest abTest = (com.geo.rcs.modules.abtest.entity.AbTest) context.getMergedJobDataMap()
        		.get(com.geo.rcs.modules.abtest.entity.AbTest.JOB_PARAM_KEY);

        //获取spring bean
        AbTestService abTestService = (AbTestService) SpringContextUtils.getBean("abTestService");

		com.geo.rcs.modules.abtest.entity.AbTest abTest1 = abTestService.getAbTestJobByPrimaryKey(abTest.getJobId());

        try {
            ScheduleRunnable task = new ScheduleRunnable(abTest1.getBeanName(),
					abTest1.getMethodName(), abTest1.getJobId(),abTest1.getUniqueCode());
            Future<?> future = service.submit(task);

			future.get();

		} catch (Exception e) {
			logger.error("\n任务执行失败，任务ID：" + abTest1.getJobId(), e);

		}finally {
			if(abTest1.getCompletedCount().toString().equals(abTest1.getTaskCount().toString()) ){
				System.out.println(JobCliHandler.ANSI_GREEN + "========= Scheduler running at "+
						JobCliHandler.time2String(System.currentTimeMillis())+"========= " + JobCliHandler.ANSI_RESET);

				System.out.printf(MessageFormat.format("JobID[{0}] :: This Job is Executed! Close this job. \n\n", abTest1.getJobId()));
				abTestService.deleteBatch(abTest1.getJobId());
			}
		}
    }

}
