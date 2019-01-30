package com.geo.rcs.modules.admin.taskmanger.service.impl;


import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.admin.taskmanger.entity.QueueDetail;
import com.geo.rcs.modules.admin.taskmanger.entity.TaskManger;
import com.geo.rcs.modules.admin.taskmanger.service.TaskMangerService;
import com.geo.rcs.modules.jobs.entity.JobRegister;
import com.geo.rcs.modules.jobs.entity.JobWorker;
import com.geo.rcs.modules.jobs.service.RegisterService;
import com.geo.rcs.modules.jobs.service.jobStatisticsService;
import com.geo.rcs.modules.monitor.entity.ScheduleRegister;
import com.geo.rcs.modules.rabbitmq.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月7日 上午10:14:41
 */
@Service
public class TaskMangerServiceImpl implements TaskMangerService {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private RabbitMqService rabbitMqService;
    @Autowired
    private jobStatisticsService jobStatisticsService;
    @Value("${spring.jobqueue.queue-max-size}")
    private Integer queueCapacity;
    @Value("${spring.jobqueue.distribute-max-size}")
    private Integer upperLimit;
    @Value("${rabbitmq.mailqueues}")
    private String[] mailQueuesArr;
    @Value("${rabbitmq.apiqueues}")
    private String[] apiQueuesArr;
    @Value("${rabbitmq.abEventqueues}")
    private String[] abEventqueues;
    @Override
    public TaskManger overView(String role) throws ServiceException, ParseException {
        TaskManger taskManger = new TaskManger();
        Map<String, Integer> detail = getjobCliDetail(role);
        Integer workerCount = detail.get("WORKER");
        Integer scheduleCount = detail.get("SCHEDULE");
        Integer queueCount = detail.get("QUEUE");
        //状态
        if (workerCount == 0 || scheduleCount == 0 || queueCount == 0) {
            taskManger.setStatus(false);
        } else {
            taskManger.setStatus(true);
        }
        //消费者数量
        taskManger.setCustomerCount(workerCount);
        //调度者数量
        taskManger.setScheduerCount(scheduleCount);
        //队列数量
        taskManger.setQueueCount(mailQueuesArr.length + apiQueuesArr.length+abEventqueues.length);
        //todo
        Map<String, Long> resultMap = jobStatisticsService.getDistrbiteExcuteThroughPut(role);
        //分发数量
        taskManger.setTaskScheduCount(resultMap.get("distrbuteCount"));
        //执行数量
        taskManger.setTaskExcuteCount(resultMap.get("executeCount"));
        //吞吐量
        taskManger.setTaskThroughPut(resultMap.get("throughPut"));
        taskManger.setTaskExecuteDayChange(resultMap.get("taskExecuteDayChange")==null?0L:resultMap.get("taskExecuteDayChange"));
        taskManger.setTaskExecuteWeekChange(resultMap.get("taskExecuteWeekChange"));
        taskManger.setTaskScheduDayChange(resultMap.get("taskScheduDayChange"));
        taskManger.setTaskScheduWeekChange(resultMap.get("taskScheduWeekChange"));
        return taskManger;
    }


    @Override
    public QueueDetail quueDetail(String role) throws Exception {
        QueueDetail queueDetail = new QueueDetail();
        queueDetail.setQueueNum(mailQueuesArr.length + apiQueuesArr.length+abEventqueues.length);
        //todo
        queueDetail.setEachTimeMax(upperLimit);
        queueDetail.setQueueCapacity(queueCapacity);

        ArrayList<Map<Object, Object>> list = new ArrayList<>();
        if ("api".equalsIgnoreCase(role)){
            getQueues(list, "api", apiQueuesArr.length, apiQueuesArr);
        }else if ("Mail".equalsIgnoreCase(role)){
            getQueues(list, "Mail", mailQueuesArr.length, mailQueuesArr);
        }else if ("abEvent".equalsIgnoreCase(role)){
            getQueues(list, "abEvent", abEventqueues.length, abEventqueues);
        }else if ("all".equalsIgnoreCase(role)){
            getQueues(list, "api", apiQueuesArr.length, apiQueuesArr);
            //邮件队列
            getQueues(list, "Mail", mailQueuesArr.length, mailQueuesArr);
            //邮件队列
            getQueues(list, "abEvent", abEventqueues.length, abEventqueues);
        }

        queueDetail.setQueueMapList(list);
        return queueDetail;
    }

    private void getQueues(ArrayList<Map<Object, Object>> list, String typeApi, int apiQueueNum, String[] apiQueuesArry) throws Exception {
        //贷中监控任务
        HashMap<Object, Object> apiMap = new HashMap<>();
        apiMap.put("num", apiQueueNum);
        apiMap.put("type", typeApi);
        List<Map> apiQueueMapList = new ArrayList<>();
        for (int i = 0, len = apiQueueNum; i < len; i++) {
            HashMap<Object, Object> queueMap = new HashMap<>();
            queueMap.put("queueName", apiQueuesArry[i]);
            queueMap.put("count", rabbitMqService.getQueueSize(apiQueuesArry[i]));
            apiQueueMapList.add(queueMap);
        }
        apiMap.put("queueList", apiQueueMapList);
        list.add(apiMap);
    }

    @Override
    public  List<JobRegister> getRegistrars(String role) throws Exception{
        return getAllworkers(role);
    }

    /**
     * 子方法: 获取所有
     *
     * @return
     */
    private List<JobRegister> getAllworkers(String role) {

        Map<String, String> _workers = registerService.getAllRegisterInfo();
        HashMap<String, List> map = new HashMap<>();
        List<JobRegister> jobWorkerList = new ArrayList<>();
        String typeStr = null;
        for (Map.Entry<String, String> entry : _workers.entrySet()) {
            Object type = JSON.parseObject(entry.getValue(), HashMap.class).get("type");
            if (type instanceof String) {
                typeStr = (String) type;
                Object taskRoleObj = JSON.parseObject(entry.getValue(),HashMap.class).get("taskRole");
                String taskRole="";
                if (taskRoleObj instanceof String){
                    taskRole = (String)taskRoleObj;
                }
                if ("WORKER".equalsIgnoreCase(typeStr)) {
                    if ("all".equalsIgnoreCase(role)){
                        jobWorkerList.add(JSON.parseObject(entry.getValue(), JobWorker.class));
                    }else if (taskRole.equalsIgnoreCase(role)){
                        jobWorkerList.add(JSON.parseObject(entry.getValue(), JobWorker.class));
                    }
                } else if ("SCHEDULE".equalsIgnoreCase(typeStr)) {
                        jobWorkerList.add(JSON.parseObject(entry.getValue(), ScheduleRegister.class));
                } else {
                    System.out.println("不识别!");
                }
            } else {
                System.out.println("注册者type 类型错误不是字符串!");
            }
        }
//        Integer pageSize =Integer.valueOf(parmMap.get("pageSize"));
//        Integer pageNo = Integer.valueOf(parmMap.get("pageNo"));
//        int startIndex = (pageNo - 1) * pageSize;
//        int endIndex = pageNo * pageSize;
//        if (startIndex > jobWorkerList.size() - 1) {
//            return new Page<>();
//        } else {
//            if (endIndex >= jobWorkerList.size() - 1) {
//                List<JobRegister> jobRegisters = jobWorkerList.subList(startIndex, jobWorkerList.size() - 1);
//                return (Page<JobRegister>) jobWorkerList.subList(startIndex,jobWorkerList.size()-1);
//            } else {
//              return (Page<JobRegister>) jobWorkerList.subList(startIndex,endIndex);
//            }
//        }
        return jobWorkerList;
    }

    @Override
    public TaskManger delData() throws ServiceException, ParseException {
        jobStatisticsService.delStatisticData();
        return null;
    }


    @Override
    public List<Map<Object, Object>> taskDetail(String unit, int interval, int size,String role) throws ServiceException {
        return jobStatisticsService.getstatisticsApi(unit, interval, size,role);
    }

    /**
     * 子方法: 获取所有
     *
     * @return
     */
    private Map<String, Integer> getjobCliDetail(String role) {
        Map<String, String> _workers = registerService.getAllRegisterInfo();
        HashMap<String, Integer> map = new HashMap<>();

        String typeStr = null;
        int wokerCount = 0;
        int scheduleCount = 0;
        for (Map.Entry<String, String> entry : _workers.entrySet()) {
            Object type = JSON.parseObject(entry.getValue(), HashMap.class).get("type");
            if (type instanceof String) {
                typeStr = (String) type;
                Object taskRoleObj = JSON.parseObject(entry.getValue(), HashMap.class).get("taskRole");
                String taskRole = (String)taskRoleObj;
                if ("WORKER".equalsIgnoreCase(typeStr)) {
                    if ("all".equalsIgnoreCase(role)){
                        wokerCount++;
                    }else if (role.equalsIgnoreCase(taskRole)){
                        wokerCount++;
                    }
                } else if ("SCHEDULE".equalsIgnoreCase(typeStr)) {
                        scheduleCount++;
                } else {
                    System.out.println("不识别!");
                }
            } else {

            }
        }
        map.put("WORKER", wokerCount);
        map.put("SCHEDULE", scheduleCount);
        //todo
        map.put("QUEUE", 4);
        return map;
    }
}
