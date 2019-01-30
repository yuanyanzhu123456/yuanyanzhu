package com.geo.rcs.modules.rabbitmq.consumer;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.jedis.RabbitmqConfig;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.rabbitmq.task.WorkerTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.util.IPUtils;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.modules.jobs.entity.JobWorker;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.rabbitmq.constant.MqConstant;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 17:09 2018/6/18
 */
@Component
public class Worker {
    /**
     * 监听队列名集合
     */
    private List<String> queneNameList;
    /**
     * worker名
     */
    private String workName;
    /**
     * 成功消费数量,测试使用
     */
    private int count;
    /**
     * 任务角色
     */
    private String taskRole;
    @Autowired
    private JobRegisterService jobRegisterService;
    @Autowired
    private WorkerTask workerTask;
    @Autowired
    private RabbitmqConfig rabbitmqConfig;
    private Channel channel;
    private JobWorker register = new JobWorker();
    private JobWorker oldRegister;
    private List<QueueingConsumer> consumerList = new ArrayList<>();
    private HashMap<String, Integer> queuePriorityMap;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public Worker() {
        super();
        // TODO Auto-generated constructor stub
    }

    public List<String> getQueneNameList() {
        return queneNameList;
    }

    public void setQueneNameList(List<String> queneNameList) {
        this.queneNameList = queneNameList;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public JobWorker getRegister() {
        return register;
    }

    public void setRegister(JobWorker register) {
        this.register = register;
    }

    public Worker(String workName, List<String> queneNameList, String taskRole, JobRegisterService jobRegisterService,
                  WorkerTask workerTask, RabbitmqConfig rabbitmqConfig, HashMap<String, Integer> queuePriorityMap) throws Exception {
        super();
        this.workName = workName;
        this.queneNameList = queneNameList;
        this.jobRegisterService = jobRegisterService;
        this.workerTask = workerTask;
        this.taskRole = taskRole;
        this.rabbitmqConfig = rabbitmqConfig;
        this.queuePriorityMap = queuePriorityMap;
        start();


    }

    /**
     * 开启监听消息队列
     *
     * @throws Exception
     */
    public void start() throws Exception {
        try {
            register.setWorkStatus(MqConstant.WorkStatus.WORK_SWITCH_OPEN.getCode());
            this.channel = rabbitmqConfig.getChannel();
            for (String queneName : queneNameList) {
                new Thread("worker:" + workName + "监听队列:" + queneName + " 线程") {
                    @Override
                    public void run() {
                        try {
                            workerListenQuene(queneName);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            LogUtil.error("", "", "", new RcsException(StatusCode.WORKER_LISTEN_QUEUE.getMessage()));
                            throw new RcsException(StatusCode.WORKER_LISTEN_QUEUE.getMessage() + queneName, e);

                        }
                    }
                }.start();
            }
            // ====
            // 等待获取mqid
            Thread.sleep(2000);
            register.initWorkerInfo(workName, IPUtils.getMyIP(), register.getMqId(),
                    MqConstant.WorkStatus.WORK_SWITCH_OPEN.getCode(), MqConstant.WorkStatus.WAIT.getCode(),
                    queneNameList, taskRole);
            register = (JobWorker) jobRegisterService.register(register);
            // ==================
            new Thread(workName+" 定时发送心跳线程:") {
                @Override
                public void run() {
                    try {
                        int workerDeadStatus = MqConstant.WorkStatus.WORKER_DEAD.getCode();
                        while (true) {
                            if (register.getWorkStatus() == workerDeadStatus) {
                                break;
                            }
                            Thread.sleep(MqConstant.HEART_TIME);
                            workerRegist();
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        LogUtil.error("", "", "", new RcsException(StatusCode.WORKER_HEART_BEAT.getMessage()));
                        throw new RcsException(StatusCode.WORKER_HEART_BEAT.getMessage(), e);
                    }
                }
            }.start();
            // ==================
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.error("", "", "", new RcsException(StatusCode.WORK_ERROR.getMessage(), e));
            throw new RcsException(StatusCode.WORK_ERROR.getMessage(), e);
        }

    }
    /**
     * 监听具体的队列
     *
     * @param queneName
     * @throws IOException
     * @throws InterruptedException
     */
    private void workerListenQuene(String queneName) throws IOException, InterruptedException {
        // 指定队列持久化
        channel.queueDeclare(queneName, true, false, false, null);
        LogUtil.info("初始化worker:"+workName,"监听队列:"+queneName,"系统","开启");
//        System.out.println("worker:  " + workName + " 监听队列:  " + queneName + "  开启");
        // 指定该消费者同时只接收一条消息
        channel.basicQos(1);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        consumerList.add(consumer);
        // this.consumer = new QueueingConsumer(channel);
        // 打开消息应答机制
        channel.basicConsume(queneName, false, consumer);
        // 获取tag
        if (StringUtils.isEmpty(register.getMqId())) {
            register.setMqId(consumer.getConsumerTag());
        } else {
            register.setMqId("," + consumer.getConsumerTag());
        }
        int workerDeadstatus = MqConstant.WorkStatus.WORKER_DEAD.getCode();
        int stopStatus = MqConstant.WorkStatus.WORK_SWITCH_STOP.getCode();
        while (true) {
            if (confirmConsum(queneName)) {
                if (register.getWorkStatus() == workerDeadstatus) {
                    break;
                }
                if (register.getWorkStatus() != stopStatus) {
                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                    String taskMessage = new String(delivery.getBody());
                    Message message = JSON.toJavaObject(JSON.parseObject(taskMessage), Message.class);
                    if (register.getWorkStatus() == workerDeadstatus) {
                        rePushQuene(queneName, delivery, message);
                        break;
                    }

                    if (register.getWorkStatus() == stopStatus || confirmConsum(queneName) == false) {
                        rePushQuene(queneName, delivery, message);
                        continue;
                    }
                    try {
                        register.setTaskStatus(MqConstant.WorkStatus.BUSY.getCode());
                        doWork(message);
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        count++;
                        LogUtil.info("消费成功",message.toString(),"系统");
//                        System.out.println("消费成功   " + message);

                    } catch (Exception e) {
                        channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
                        // TODO Auto-generated catch block
                        // 最多消费三次
                        if (message.getFailCount()< MqConstant.FAIL_MAX_NUM) {
                            message.setFailCount(message.getFailCount() + 1);
                            rePushQuene(queneName, delivery, message);
                            LogUtil.error("", "", "", new RcsException(StatusCode.WORK_EXECUTE_TASK_ERROR.getMessage() + (message.getFailCount() + 1), e));
                        } else {
                            // TODO Auto-generated catch block,添加存入数据库

                            LogUtil.error("", "", "", new RcsException(StatusCode.WORK_EXECUTE_TASK_ERROR_3.getMessage() + message, e));
                        }
                    }
                    LogUtil.info("woker消费总计",count+"条","系统");
//                    System.out.println(workName+":  消费成功::" + count + "条");
                    System.out.println();
                    System.out.println();
                    register.setTaskStatus(MqConstant.WorkStatus.WAIT.getCode());
                } else {
                    // System.out.println(workName + "==暂停==");
                }
            }
        }
    }

    /**
     * 重新放入队列
     *
     * @param queneName
     * @param delivery
     * @param message
     * @throws IOException
     */
    private void rePushQuene(String queneName, QueueingConsumer.Delivery delivery, Message message) throws IOException {
//        channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
        // 指定队列持久化
        channel.queueDeclare(queneName, true, false, false, null);
        String beanToJson = JSONUtil.beanToJson(message);
        // 指定消息持久化
        channel.basicPublish("", queneName, MessageProperties.PERSISTENT_TEXT_PLAIN, beanToJson.getBytes());
    }

    /**
     * 注册心跳
     *
     * @throws Exception
     */
    private void workerRegist() throws Exception {
        oldRegister = register;
        register = (JobWorker) jobRegisterService.keepAlive(register);
        taskRole = register.getTaskRole();
        if (oldRegister.getWorkStatus() != register.getWorkStatus()) {
            String oldStatus = MqConstant.WorkStatus.WORK_MESSAGE.getMessage(oldRegister.getWorkStatus());
            String newStatus = MqConstant.WorkStatus.WORK_MESSAGE.getMessage(register.getWorkStatus());
            if (register.getWorkStatus() == MqConstant.WorkStatus.WORKER_DEAD.getCode()) {
                destroy();
            }
            System.out.println(register.getWorkerName() + " 状态由 " + oldStatus + "  变为  " + newStatus);
        }
        displayWorker(register);
    }

    /**
     * 注销队列消费者
     *
     * @throws IOException
     */
    private void destroy() throws IOException {
        for (QueueingConsumer cons : consumerList) {
            channel.basicCancel(cons.getConsumerTag());
        }

    }

    /**
     * 执行方法
     *
     * @param message
     * @throws Exception
     */
    private void doWork(Message message) throws Exception {
        // TODO Auto-generated method stub
//        System.out.println("消费消息:   " + message);
        LogUtil.info("消费消息",message.toString(),"系统");
        workerTask.doTask(message,taskRole);
    }

    /**
     * 确认是否进行消费该队列数据
     *
     * @param queueName
     * @return
     * @throws IOException
     */
    private boolean confirmConsum(String queueName) throws IOException {
        Integer priority = queuePriorityMap.get(queueName);
        Integer otherPriority = 0;
        for (String queue : queneNameList) {
            otherPriority = queuePriorityMap.get(queue);
            if (otherPriority > priority && channel.messageCount(queue) > 0) {
                return false;
            }

        }
        return true;
    }

    /**
     * 控制台输出心跳JobWorker
     *
     * @param register
     */
    public void displayWorker(JobWorker register) {
        String msgDemo = "[WORKER]  {0} {1} {2} {3} {4} {5} {6} {7} {8}";
        int waitStatuse = MqConstant.WorkStatus.WAIT.getCode();
        int busyStatuse = MqConstant.WorkStatus.BUSY.getCode();
        int deadStatus = MqConstant.WorkStatus.WAIT.getCode();
        String workStatus = MqConstant.WorkStatus.WORK_MESSAGE.getMessage(register.getWorkStatus());
        String taskStatus = register.getTaskStatus() == waitStatuse ? "Wait" : "Busy";
        String msg = MessageFormat.format(msgDemo,
                register.getWorkerName(),
                register.getId(),
                workStatus,
                taskStatus,
                register.getTaskRole(),
                register.getIp(),
                register.getQueneNameList(),
                register.getRegistTimeString(),
                register.getUpdateTimeString()
        );

        if (register.getTaskStatus() == waitStatuse) {
            System.out.println(ANSI_GREEN + msg + ANSI_RESET);
        } else if (register.getTaskStatus() == busyStatuse || register.getWorkStatus() == deadStatus) {
            System.out.println(ANSI_RED + msg + ANSI_RESET);
        }

        System.out.println();
        System.out.println();
        System.out.println();
    }
}