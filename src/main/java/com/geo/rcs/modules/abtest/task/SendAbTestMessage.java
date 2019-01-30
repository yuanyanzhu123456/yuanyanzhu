package com.geo.rcs.modules.abtest.task;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.schedule.MonitorPriority;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.abtest.entity.AbScheduleTask;
import com.geo.rcs.modules.abtest.entity.AbTest;
import com.geo.rcs.modules.abtest.service.AbTestService;
import com.geo.rcs.modules.api.modules.user.controller.ApiLoginController;
import com.geo.rcs.modules.api.modules.user.service.ApiUserTokenService;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.geo.rcs.modules.rabbitmq.service.RabbitMqService;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.service.SysUserService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.task
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月06日 上午11:15
 */

@Component("sendAbTestMessage")
public class SendAbTestMessage {

    private static Long excessCount = 0L;

    private static Long excuteCount = 0L;

    private static Message message;

    private String queueName;

    @Autowired
    private RabbitMqService rabbitMqService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AbTestService abTestService;
    @Autowired
    private JobRegisterService jobRegisterService;
    @Autowired
    private ApiUserTokenService apiUserTokenService;

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

    /**
     * 推送到队列等待消费
     * @param concurrentHashMap
     */
    @Transactional
    public List<AbScheduleTask> addToQueue(ConcurrentHashMap concurrentHashMap) {

        // 定义未分发的集合
        List<AbScheduleTask> excuteScheduleTasks = new LinkedList<>();
        List<AbScheduleTask> excessScheduleTasks = new ArrayList<>();

        try {
            Long jobId = Long.valueOf(concurrentHashMap.get("jobId").toString());
            Long ruleType = Long.valueOf(concurrentHashMap.get("ruleType").toString());
            Long eventType = Long.valueOf(concurrentHashMap.get("eventType").toString());
            Long uniqueCode = Long.valueOf(concurrentHashMap.get("uniqueCode").toString());

            SysUser sysUser = sysUserService.selectByPrimaryKey(uniqueCode);
            Geo token = apiUserTokenService.createToken(sysUser.getUniqueCode());
            //根据编号查找任务信息
            AbTest abTest = abTestService.getAbTestJobByPrimaryKey(jobId);
            if(abTest == null){
                throw new RcsException(StatusCode.PARAMS_ERROR);
            }
            //需要执行的名单列表
            List<AbScheduleTask> scheduleTasks = (ArrayList)concurrentHashMap.get("scheduleTasks");
            queueName = MonitorPriority.getPriority(5);
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


            for (AbScheduleTask scheduleTask : scheduleTasks) {
                try{
                    //查询队列长度
                    long queueSize = rabbitMqService.getQueueSize(queueName);
                    if(queueSize < getMaxQueueSize()){
                        for (String s : JSONUtil.jsonToMap(scheduleTask.getParmsJson()).keySet()) {
                            map.put(s,JSONUtil.jsonToMap(scheduleTask.getParmsJson()).get(s).toString());
                        }
                        map.put("jobId",jobId.toString());
                        map.put("ruleType",ruleType.toString());
                        map.put("type",eventType.toString());
                        if(abTest.getRuleType() == 0){
                            map.put("rulesId",scheduleTask.getGoalA().toString());
                        }else if(abTest.getRuleType() == 1){
                            map.put("decisionId",scheduleTask.getGoalId().toString());
                        }
                        map.put("sysUser", JSONObject.toJSONString(sysUser));
                        map.put("token", token.get("token").toString());
                        map.put("taskCycle",String.valueOf(scheduleTask.getDistributeNum()));
                        map.put("userId",abTest.getUniqueCode().toString());
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
                        break;
                    }
                }catch (Exception e3){
                    LogUtil.error("推送子任务", scheduleTask.toString(), queueName, e3);
                }
            }
            channel.close();
            connection.close();
            scheduleTasks.removeAll(excuteScheduleTasks);
            //success
            excuteCount =  excuteScheduleTasks.size()+0L;
            jobRegisterService.changeDistrbuteCount("abEvent",1,excuteScheduleTasks.size());
            //fail
            excessCount = scheduleTasks.size()+0L;
            jobRegisterService.changeDistrbuteCount("abEvent",0,scheduleTasks.size());

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