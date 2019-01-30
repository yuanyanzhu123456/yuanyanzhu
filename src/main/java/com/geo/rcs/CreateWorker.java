package com.geo.rcs;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.jedis.RabbitmqConfig;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.rabbitmq.constant.MqConstant;
import com.geo.rcs.modules.rabbitmq.consumer.Worker;
import com.geo.rcs.modules.rabbitmq.task.WorkerTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 17:09 2018/7/10
 */
@Component
public class CreateWorker {
    @Value("${geo.rabbitmq.open}")
    private boolean mqSwitch;
    @Value("${geo.redis.open}")
    private boolean redisSwitch;
    @Value("${rabbitmq.mailqueues}")
    private String[] mailQueuesArr;
    @Value("${rabbitmq.apiqueues}")
    private String[] apiQueuesArr;
    @Value("${rabbitmq.rulesqueues}")
    private String[] rulesQueuesArr;
    @Value("${rabbitmq.abEventqueues}")
    private String[] abEventArr;
    @Value("${rabbitmq.taskroles}")
    private String[] taskroles;
    @Autowired
    private JobRegisterService jobRegisterService;
    @Autowired
    private WorkerTask workerTask;
    @Autowired
    private RabbitmqConfig rabbitmqConfig;

    public void newWork(String workName, String[] queueNames, String taskRole) throws Exception {

        try {
            validateCreate(workName, taskRole);
            List<String> mailQueneNameListConfig = Arrays.asList(mailQueuesArr);
            List<String> apiQueneNameListConfig = Arrays.asList(apiQueuesArr);
            List<String> rulesQueneNameListConfig = Arrays.asList(rulesQueuesArr);
            List<String> abEventQueneNameListConfig = Arrays.asList(abEventArr);

            ArrayList<String> queneNameList = new ArrayList<>();
            ArrayList<String> mailList = new ArrayList<>();
            ArrayList<String> apiList = new ArrayList<>();
            ArrayList<String> rulesList = new ArrayList<>();
            ArrayList<String> abEventList = new ArrayList<>();


            if (queueNames != null && queueNames.length > 0) {
                for (int i = 0; i < queueNames.length; i++) {
                    if (mailQueneNameListConfig.contains(queueNames[i])) {
                        if (MqConstant.TaskType.MAIL_TASK.getMessage().equalsIgnoreCase(taskRole)) {
                            mailList.add(queueNames[i]);
                        } else {
                            throw new RcsException(StatusCode.WORKER_QUEUE_ILLEGAL_ERROR.getMessage());
                        }
                    } else if (apiQueneNameListConfig.contains(queueNames[i])) {
                        if (MqConstant.TaskType.API_TASK.getMessage().equalsIgnoreCase(taskRole)) {
                            apiList.add(queueNames[i]);
                        } else {
                            throw new RcsException(StatusCode.WORKER_QUEUE_ILLEGAL_ERROR.getMessage());
                        }
                    } else if (rulesQueneNameListConfig.contains(queueNames[i])) {
                        if (MqConstant.TaskType.RULES_TASK.getMessage().equalsIgnoreCase(taskRole)) {
                            rulesList.add(queueNames[i]);
                        } else {
                            throw new RcsException(StatusCode.WORKER_QUEUE_ILLEGAL_ERROR.getMessage());
                        }
                    } else if (abEventQueneNameListConfig.contains(queueNames[i])) {
                        if (MqConstant.TaskType.AB_EVENT_TASK.getMessage().equalsIgnoreCase(taskRole)) {
                            abEventList.add(queueNames[i]);
                        } else {
                            throw new RcsException(StatusCode.WORKER_QUEUE_ILLEGAL_ERROR.getMessage());
                        }
                    } else {
                        LogUtil.error("", "", "", new RcsException(StatusCode.WORKER_QUEUE_ILLEGAL_ERROR.getMessage()));
                        throw new RcsException(StatusCode.WORKER_QUEUE_ILLEGAL_ERROR.getMessage());
                    }
                    queneNameList.add(queueNames[i]);
                }
                if (Math.abs(mailList.size()-apiList.size()-rulesList.size()-abEventList.size())!=queueNames.length) {
                    LogUtil.error("", "", "", new RcsException(StatusCode.WORK_QUEUE_TYPE_ERROR.getMessage()));
                    throw new RcsException(StatusCode.WORK_QUEUE_TYPE_ERROR.getMessage());
                }
            } else {
                if (MqConstant.TaskType.MAIL_TASK.getMessage().equalsIgnoreCase(taskRole)) {
                    queneNameList.addAll(mailQueneNameListConfig);
                } else if (MqConstant.TaskType.API_TASK.getMessage().equalsIgnoreCase(taskRole)) {
                    queneNameList.addAll(apiQueneNameListConfig);
                } else if (MqConstant.TaskType.RULES_TASK.getMessage().equalsIgnoreCase(taskRole)) {
                    queneNameList.addAll(rulesQueneNameListConfig);
                }else if (MqConstant.TaskType.AB_EVENT_TASK.getMessage().equalsIgnoreCase(taskRole)) {
                    queneNameList.addAll(abEventQueneNameListConfig);
                }
            }
            HashMap<String, Integer> queuePriorityMap = new HashMap<>(4);
            queuePriorityMap = MqConstant.getQueuePriorityMap(taskRole);
            Worker worker = new Worker(workName, queneNameList, taskRole, jobRegisterService, workerTask, rabbitmqConfig, queuePriorityMap);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof RcsException) {
                throw e;
            } else {
                LogUtil.error("", "", "", new RcsException(StatusCode.WORK_ERROR.getMessage(), e));
                throw new RcsException(StatusCode.WORK_ERROR.getMessage(), e);
            }

        }

    }

    private void validateCreate(String workName, String taskRole) {
        //校验mq开关
        if (!mqSwitch) {
            LogUtil.error("", "", "", new RcsException(StatusCode.WORK_MQ_CLOSE.getMessage()));
            throw new RcsException(StatusCode.WORK_MQ_CLOSE.getMessage());
        }
        //校验redis开关
        if (!redisSwitch) {
            LogUtil.error("", "", "", new RcsException(StatusCode.WORK_REDIS_CLOSE.getMessage()));
            throw new RcsException(StatusCode.WORK_REDIS_CLOSE.getMessage());
        }
        //校验name
        if (StringUtils.isBlank(workName)) {
            LogUtil.error("", "", "", new RcsException(StatusCode.WORK_NAME_ERROR.getMessage()));
            throw new RcsException(StatusCode.WORK_NAME_ERROR.getMessage());
        }
        List<String> taskRoleList = Arrays.asList(taskroles);
        //校验任务类型
        if (!taskRoleList.contains(taskRole)) {
            LogUtil.error("", "", "", new RcsException(StatusCode.WORKER_TASKROLE_ERROR.getMessage()));
            throw new RcsException(StatusCode.WORKER_TASKROLE_ERROR.getMessage());
        }
    }
}
