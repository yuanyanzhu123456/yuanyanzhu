package com.geo.rcs;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.schedule.ScheduleUtils;
import com.geo.rcs.common.util.IPUtils;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.monitor.dao.ScheduleJobMapper;
import com.geo.rcs.modules.monitor.dao.ScheduleTaskMapper;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleRegister;
import com.geo.rcs.modules.rabbitmq.constant.MqConstant;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年08月03日 上午10:17
 */
@Component
public class InitJob {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Resource
    private Scheduler scheduler;
    @Autowired
    private ScheduleJobMapper schedulerJobDao;
    @Autowired
    private ScheduleTaskMapper scheduleTaskMapper;
    /**
     * scheduler名
     */
    private String scheduleName;

    private ScheduleRegister register = new ScheduleRegister();

    @Autowired
    private JobRegisterService jobRegisterService;


    /**
     * 项目启动时，初始化定时器
     */
    public void init(){
            List<ScheduleJob> scheduleJobList = schedulerJobDao.queryList(new HashMap<String, Object>());
            for(ScheduleJob scheduleJob : scheduleJobList){
                CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
                //如果不存在，则创建
                if(cronTrigger == null) {
                    ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
                }else {
                    ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
                }
            }
            LogUtil.info("初始化quartz定时器","quartz定时器启动","系统");
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("", "", "", new RcsException(StatusCode.WORK_ERROR.getMessage(), e));
        }
    }

    /**
     * 开启监听消息队列
     *
     * @throws Exception
     */


    public void start() throws Exception {

        try {
            register.initSchedulerInfo(IPUtils.getMyIP());
            register = (ScheduleRegister) jobRegisterService.register(register);
            // ==================
            new Thread(scheduleName + " 定时发送心跳线程:") {
                @Override
                public void run() {
                    try {
                        while (true){
                            jobRegisterService.keepAlive(register);
                            //displayScheduler(register);
                            Thread.sleep(20000);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        LogUtil.error("", "", "", new RcsException(StatusCode.WORKER_HEART_BEAT.getMessage()));
                        throw new RcsException(StatusCode.WORKER_HEART_BEAT.getMessage(), e);
                    }
                }
            }.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.error("", "", "", new RcsException(StatusCode.WORK_ERROR.getMessage(), e));
            throw new RcsException(StatusCode.WORK_ERROR.getMessage(), e);
        }
    }

    /**
     * 控制台输出心跳Scheduler
     *
     * @param register
     */
    public void displayScheduler(ScheduleRegister register) {
        String msgDemo = "[SCHEDULER]  {0} {1} {2} {3} {4}";
        int waitStatuse = MqConstant.WorkStatus.WAIT.getCode();
        int busyStatuse = MqConstant.WorkStatus.BUSY.getCode();
        int deadStatus = MqConstant.WorkStatus.WAIT.getCode();
        String msg = MessageFormat.format(msgDemo,
                "scheduler",
                register.getId(),
                register.getIp(),
                register.getRegistTimeString(),
                register.getUpdateTimeString()
        );

        System.out.println(ANSI_GREEN + msg + ANSI_RESET);


        System.out.println();
        System.out.println();
        System.out.println();
    }

}
