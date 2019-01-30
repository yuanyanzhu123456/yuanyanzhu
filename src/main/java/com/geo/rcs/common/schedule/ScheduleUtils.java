package com.geo.rcs.common.schedule;


import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.exception.GeoException;
import com.geo.rcs.modules.abtest.entity.AbTest;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import org.quartz.*;


/**
 * 定时任务工具类
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */

public class ScheduleUtils {
    private final static String JOB_NAME = "TASK_";


    /**
     * 获取触发器key
     */

    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }


    /**
     * 获取jobKey
     */

    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }



    /**
     * 获取表达式触发器
     */

    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw new GeoException("获取定时任务CronTrigger出现异常", e);
        }
    }


    /**
     * 创建定时任务
     */

    public static void createScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(com.geo.rcs.common.schedule.ScheduleJob.class).withIdentity(getJobKey(scheduleJob.getJobId())).build();

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            //scheduleBuilder.withMisfireHandlingInstructionDoNothing()   --不触发立即执行
                                                                        //——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob.getJobId())).withSchedule(scheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY, scheduleJob);

            scheduler.scheduleJob(jobDetail, trigger);

            //暂停任务
            if (scheduleJob.getStatus() == Constant.ScheduleStatus.PAUSE.getValue()) {
                pauseJob(scheduler, scheduleJob.getJobId());
            }
        } catch (SchedulerException e) {
            throw new GeoException("创建定时任务失败", e);
        }
    }


    /**
     * 更新定时任务
     */

    public static void updateScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            TriggerKey triggerKey = getTriggerKey(scheduleJob.getJobId());

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = getCronTrigger(scheduler, scheduleJob.getJobId());

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //参数
            trigger.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY, scheduleJob);

            scheduler.rescheduleJob(triggerKey, trigger);

            //暂停任务
            if (scheduleJob.getStatus() == Constant.ScheduleStatus.PAUSE.getValue()) {
                pauseJob(scheduler, scheduleJob.getJobId());
            }

        } catch (SchedulerException e) {
            throw new GeoException("更新定时任务失败", e);
        }
    }


    /**
     * 立即执行任务
     */

    public static void run(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            //参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ScheduleJob.JOB_PARAM_KEY, scheduleJob);

            scheduler.triggerJob(getJobKey(scheduleJob.getJobId()), dataMap);
        } catch (SchedulerException e) {
            throw new GeoException("立即执行定时任务失败", e);
        }
    }


    /**
     * 暂停任务
     */

    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new GeoException("暂停定时任务失败", e);
        }
    }


    /**
     * 恢复任务
     */

    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new GeoException("暂停定时任务失败", e);
        }
    }


    /**
     * 删除定时任务
     */

    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new GeoException("删除定时任务失败", e);
        }
    }

    public static void createAbTestJob(Scheduler scheduler, AbTest abTest) {
        try {
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(com.geo.rcs.common.schedule.AbTestJob.class).withIdentity(getJobKey(abTest.getJobId())).build();

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(abTest.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            //scheduleBuilder.withMisfireHandlingInstructionDoNothing()   --不触发立即执行
            //——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(abTest.getJobId())).withSchedule(scheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY, abTest);

            scheduler.scheduleJob(jobDetail, trigger);

            //暂停任务
            if (abTest.getJobStatus() == Constant.ScheduleStatus.PAUSE.getValue()) {
                pauseJob(scheduler, abTest.getJobId());
            }
        } catch (SchedulerException e) {
            throw new GeoException("创建定时任务失败", e);
        }
    }
}
