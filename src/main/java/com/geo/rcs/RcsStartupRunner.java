package com.geo.rcs;

import com.geo.rcs.common.util.RcsBannerUtils;
import com.geo.rcs.modules.jobs.handler.JobCliHandler;
import com.geo.rcs.modules.kafka.HadoopConsts;
import com.geo.rcs.modules.kafka.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * 服务启动执行
 *
 * @author   yongmingz(365384722)
 * @myblog
 * @create    2018年7月11日
 */
@Component
public class RcsStartupRunner implements CommandLineRunner {

    @Autowired
    private JobCliHandler jobCliHandler;

    /**
     * 读取启动服务模式配置项
     */
    @Value("${app}")
    private String app;

    /**
     * 读取调试服务模式配置项
     */
    @Value("${spring.debug}")
    private boolean debugOpen;

    /**
     * 读取启动激活环境配置项
     */
    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private CreateWorker worker;

    /**
     * 读取Worker名称配置项
     */
    @Value("${workname}")
    private String workName;

    /**
     * 读取队列名称配置项
     */
    @Value("${quenenames}")
    private String[] queneNames;

    /**
     * 读取Worker任务角色配置项
     */
    @Value("${taskrole}")
    private String taskRole;

    /**
     * 读取API-Log2Hdfs开关配置项
     */
    @Value("${spring.kafka.hdfsLogSwitch}")
    private  boolean log2HdfsOpen;

    /**
     * 读取Kafka-API日志模式配置项
     */
    @Value("${spring.kafka.flinkStreamOpen}")
    private  boolean flinkStreamOpen;

    /**
     * 读取HDFS-Host日志模式配置项
     */
    @Value("${spring.kafka.hdfsIpPortList}")
    private  String hdfsHostList;

    /**
     * 读取Kafka-Host日志模式配置项
     */
    @Value("${spring.kafka.zkSendIpPortList}")
    private  String kafkaHostList;

    /**
     * 读取ZooKeeperHost日志模式配置项
     */
    @Value("${spring.kafka.zkListenIpPortList}")
    private  String zookeeperHostList;

    /**
     * 读取Redis配置项
     */
    @Value("${geo.redis.open}")
    private  boolean redisOpen;

    /**
     * 读取Redis配置项
     */
    @Value("${spring.redis.host}")
    private  String redisHost;

    /**
     * 读取RabbitMQ配置项
     */
    @Value("${geo.rabbitmq.open}")
    private  boolean rabbitMqOpen;

    /**
     * 读取RabbitMQ配置项
     */
    @Value("${spring.rabbitmq.host}")
    private  String rabbitMqHost;

    /**
     * 读取Quartz定时服务配置项
     */
    @Value("${geo.quartz.open}")
    private boolean quartzOpen;

    @Autowired
    private InitJob initJob;

    @Autowired
    private KafkaConsumer consumer;

    /**
     * SpringBoot启动自定义初始化选项
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        if("WEB".equals(app.toUpperCase())){
            if (log2HdfsOpen){
                consumer.createConsumers(HadoopConsts.TOPIC_API_EVENT_ENTRY, 3);
            }
            RcsBannerUtils.Web();
            rcsStartInfo();
        }
        else if("WORKER".equals(app.toUpperCase())){
            RcsBannerUtils.Workers();
            worker.newWork(workName,queneNames,taskRole);
            System.out.println("MainApp:"+app);
        }
        else if("JOBCLI".equals(app.toUpperCase())){
            RcsBannerUtils.JobCli();
            System.out.println("MainApp:"+app);
            jobCliHandler.displayWorkers();
        }else if("SCHEDULER".equals(app.toUpperCase())){
            RcsBannerUtils.Scheduler();
            System.out.println("MainApp:"+app);
            initJob.init();
        }

    }

    /**
     * 输出服务启动项详细信息
     * @throws Exception
     */
    public void rcsStartInfo() throws Exception {

        if(debugOpen){
            System.out.println(
                    " :: Rcs Version                        ::        v2.x RELEASE                            \n"+
                    " :: Rcs Running Mode                   ::        "+ app +" mode                          \n" +
                    " :: Rcs Running Debug                  ::        "+ debugOpen +"                         \n" +
                    " :: Rcs Running Environment            ::        "+ active +"                            \n" +
                    " :: Rcs Quartz Service OPEN            ::        "+ quartzOpen +"                        \n" +
                    " :: Rcs Redis Service OPEN             ::        "+ redisOpen +"("+ redisHost+")         \n" +
                    " :: Rcs RabbitMQ Service OPEN          ::        "+ rabbitMqOpen  +"("+ rabbitMqHost+")  \n" +
                    " :: Rcs Log2Hdfs Service OPEN          ::        "+ log2HdfsOpen  +"                     \n" +
                    " :: Rcs Log2Flink Service OPEN         ::        "+ flinkStreamOpen  +"                  \n" +
                    " :: Rcs HDFS Service HostList          ::        "+ hdfsHostList  +"                     \n" +
                    " :: Rcs Kafka Service HostList         ::        "+ kafkaHostList  +"                    \n" +
                    " :: Rcs Zookeeper Service HostList     ::        "+ zookeeperHostList  +"                \n" +
                    "                                                                                         \n"
            );
        }else{
            System.out.println(
                    " :: Rcs Version                        ::        v2.x RELEASE                            \n"+
                    " :: Rcs Running Debug                   ::       "+ debugOpen +"                         \n" +
                    " :: Rcs Running Mode                   ::        "+ app +" mode                          \n" +
                    " :: Rcs Running Environment            ::        "+ active +"                            \n" +
                    "                                                                                         \n"
            );
        }
    }


}