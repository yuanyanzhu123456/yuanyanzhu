package com.geo.rcs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * 定时任务配置
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/07/29 15:28
 */
@Configuration
public class ScheduleConfig {
    @Value("${geo.quartz.open}")
    private boolean open;
    @Value("${app}")
    private String app;
    //这里怎么不能用@Autowired，用Resource倒是可以
    @Resource
    private DataSource dataSource;
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);

        //quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", "GeoScheduler");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        //线程池配置
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "20");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        //JobStore配置
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        //集群配置
        prop.put("org.quartz.jobStore.isClustered", "true");
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        //拉取待即将触发的triggers时，是上锁的状态，即不会同时存在多个线程拉取到相同的trigger的情况，也就避免的重复调度的危险
        prop.put("org.quartz.jobStore.acquireTriggersWithinLock", "true");

        prop.put("org.quartz.jobStore.misfireThreshold", "12000");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        prop.put("waitForJobsToCompleteOnShutdown", "true");
        factory.setQuartzProperties(prop);

        factory.setSchedulerName("GeoScheduler");
        //延时启动
        factory.setStartupDelay(30);

        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        //可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        //设置自动启动，默认为true
        if("SCHEDULER".equals(app.toUpperCase())){
            System.out.println("MainApp:"+app);
            factory.setAutoStartup(true);
        }
        else{
            factory.setAutoStartup(false);
        }

        return factory;
    }
}
