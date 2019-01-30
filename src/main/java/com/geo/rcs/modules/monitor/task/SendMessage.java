package com.geo.rcs.modules.monitor.task;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.schedule.MonitorPriority;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.service.DimensionService;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.geo.rcs.modules.rabbitmq.service.RabbitMqService;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.task
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月06日 上午11:15
 */

@Component("sendMessage")
public class SendMessage{

    private static Long excessCount = 0L;

    private static Long excuteCount = 0L;

    private static Message message;

    private String queueName;

    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @Autowired
    private RabbitMqService rabbitMqService;

    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private JobRegisterService jobRegisterService;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.jobqueue.queue-max-size}")
    private String queueMaxSize;
    @Value("${spring.jobqueue.distribute-max-size}")
    private String distributeMaxSize;

    /*private static SendAbTestMessage singleton ;

    private SendAbTestMessage(){

    }
    public static SendAbTestMessage getInstance(){
        if(singleton==null){
            synchronized (SendAbTestMessage.class) {
                if(singleton==null){
                    singleton=new SendAbTestMessage();
                }
            }
        }
        return singleton;
    }*/

    /**
     * 推送到队列等待消费
     * @param concurrentHashMap
     */
    @Transactional
    public List<ScheduleTask> addToQueue(ConcurrentHashMap concurrentHashMap) {

        // 定义未分发的集合
        List<ScheduleTask> excuteScheduleTasks = new LinkedList<>();
        List<ScheduleTask> excessScheduleTasks = new ArrayList<>();

        try {
            Long jobId = Long.valueOf(concurrentHashMap.get("jobId").toString());
            Long userId = Long.valueOf(concurrentHashMap.get("userId").toString());

            //根据编号查找任务信息
            ScheduleJob scheduleJob = scheduleJobService.queryObject(jobId,userId);
            if(scheduleJob == null){
                throw new RcsException(StatusCode.PARAMS_ERROR);
            }
            //需要执行的名单列表
            List<ScheduleTask> scheduleTasks = (ArrayList)concurrentHashMap.get("scheduleTasks");
            queueName = MonitorPriority.getPriority(scheduleJob.getPriority());
            if(ValidateNull.isNull(queueName)){
                throw new RcsException(StatusCode.PARAMS_ERROR);
            }
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setPort(port);
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();
            // 指定队列持久化
            channel.queueDeclare(queueName, true, false, false, null);

            HashMap<String, Object> map = new HashMap<>();


            for (ScheduleTask scheduleTask : scheduleTasks) {
                //查询队列长度
                try{
                    long queueSize = rabbitMqService.getQueueSize(queueName);
                    if(queueSize < getMaxQueueSize()){
                        map.put("jobId",jobId.toString());
                        map.put("realName",scheduleTask.getName());
                        map.put("cid",scheduleTask.getCid());
                        map.put("paramsJson",scheduleTask.getParmsJson());
                        map.put("idNumber",scheduleTask.getIdNumber());
                        map.put("taskCycle",String.valueOf(scheduleTask.getDistributeNum()));
                        map.put("unit",MonitorPriority.getUnit(scheduleJob.getMonitorUnit()));
                        map.put("cycleNum",scheduleJob.getCycleNum().toString());
                        map.put("dimension",scheduleJob.getDimension());
                        map.put("userId",scheduleJob.getUserId().toString());
                        map.put("interval",scheduleJob.getInterval().toString());
                        map.put("updateTime", DatetimeFormattor.formatDateTime(scheduleTask.getUpdateTime()));
                        message = new Message(scheduleTask.getId(), map);
                        String beanToJson = JSON.toJSONString(message);
                        // 推送到队列中
                        channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, beanToJson.getBytes());
                        excuteScheduleTasks.add(scheduleTask);
                        if(excuteScheduleTasks.size() > getDistributeMaxSize()){
                            break;
                        }
                    }else {
//                        excessScheduleTasks.add(scheduleTask);
                        break;
                    }
                }catch (Exception e3){
                    LogUtil.error("推送子任务", scheduleTask.toString(), queueName, e3);
//                    excessScheduleTasks.add(scheduleTask);
                }
            }
            channel.close();
            connection.close();
            scheduleTasks.removeAll(excuteScheduleTasks);
            //success
            excuteCount =  excuteScheduleTasks.size()+0L;
            jobRegisterService.changeDistrbuteCount("api",1,excuteScheduleTasks.size());
            //fail
            excessCount = scheduleTasks.size()+0L;
            jobRegisterService.changeDistrbuteCount("api",0,scheduleTasks.size());

            String info = MessageFormat.format("分发状态：已分发{0}条，超额{1}条下次分发", excuteCount, excessCount);
            LogUtil.info("推送任务到队列", info, "调度器");
            return scheduleTasks;

        } catch (Exception e) {
            LogUtil.error("推送任务到队列", message.toString(), queueName, e);
            return (ArrayList)concurrentHashMap.get("scheduleTasks");
        }

    }

    private  Long getMaxQueueSize(){
        return Long.valueOf(queueMaxSize);
    }

    private  Long getDistributeMaxSize(){
        return Long.valueOf(distributeMaxSize);
    }


}