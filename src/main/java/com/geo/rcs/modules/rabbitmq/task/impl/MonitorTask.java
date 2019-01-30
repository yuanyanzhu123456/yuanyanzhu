package com.geo.rcs.modules.rabbitmq.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.modules.api.annotation.AuthIgnore;
import com.geo.rcs.modules.api.modules.eventin.controller.ApiEventInController;
import com.geo.rcs.modules.datapool.service.DataPoolService;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.entity.TaskLog;
import com.geo.rcs.modules.monitor.service.DimensionService;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import com.geo.rcs.modules.monitor.service.TaskLogService;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.geo.rcs.modules.rabbitmq.task.Task;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 15:46 2018/7/26
 */
@Component
public class MonitorTask implements Task {


    @Autowired
    private DataPoolService dataPoolService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private JobRegisterService jobRegisterService;
    @Autowired
    private DimensionService dimensionService;
    @Autowired
    private ApiEventInController apiEventInController;
    @Autowired
    private SysUserService sysUserService;
    @Value("${taskrole}")
    private String taskRole;
    @Override
    public void executeTask(Message message) throws Exception {
        Long taskId = message.getId();
        HashMap<String, Object> parmMap = null;
        Integer taskCycle = null;
        String userIdStr = null;
        Long jobId = null;
        Integer cycleNum = null;

        //默认执行中
        Integer status = 2;
        long userId = 0L;
        ScheduleJob scheduleJob = null;
//        long startTime = System.currentTimeMillis();
        try {
            parmMap = message.getTaskMethodParmMap();
            taskCycle = Integer.parseInt(String.valueOf(parmMap.get("taskCycle")));
            userIdStr = (String) parmMap.get("userId");
            jobId = Long.valueOf(String.valueOf(parmMap.get("jobId")));
            cycleNum = Integer.parseInt(String.valueOf(parmMap.get("cycleNum")));
            userId = Long.valueOf(userIdStr);
            if (message.getFailCount() == 0) {
                saveApiData(taskId, null, taskCycle, status, null, userId, parmMap,jobId,null);
            }
            scheduleJob = scheduleJobService.queryObject(jobId, userId);
            scheduleJob.setUpdateTime(new Date());
            // 多头样本一: 有数据
            Integer dimension = Integer.valueOf(parmMap.get("dimension").toString());
            String resp = null;
            Dimension dimension1 = dimensionService.selectByPrimaryKey(dimension);
            Map<String, Object> paramsJson = JSONUtil.jsonToMap(parmMap.get("paramsJson").toString());
            for (String s : paramsJson.keySet()) {
                parmMap.put(s,paramsJson.get(s));
            }
            //0:规则集,1:决策集,2:模型,3:接口
            if(dimension1.getPolicyType() == 0){
                parmMap.put("rulesId",dimension1.getPolicyId());
                Geo geo = apiEventInController.eventEntry(parmMap,sysUserService.selectByPrimaryKey(dimension1.getUnicode()));
                Map<String,Object> data = (Map)geo.get("data");
                if("2000".equals(geo.get("code").toString())){
                    String [] executeStatus= dimension1.getAlarmPolicy().split(",");
                    status=1;
                    for (String s : executeStatus) {
                        if(data.get("status").toString().equals(s)){
                            status=3;
                            break;
                        }
                    }
                    resp = data.get("eventId").toString();
                }else{
                    status = 0;
                    resp = geo.get("eventId").toString();
                }
                saveApiData(taskId, resp, taskCycle, status, "alert!=false&&alert!=true", userId, parmMap,jobId,null);
            }else if(dimension1.getPolicyType() == 1){
                resp = dataPoolService.getPersonByThreeFactor(dimension1.getDimensionName(), userId, parmMap);
            }else if(dimension1.getPolicyType() == 2){
                resp = dataPoolService.getPersonByThreeFactor(dimension1.getDimensionName(), userId, parmMap);
            }else if(dimension1.getPolicyType() == 3){
                resp = dataPoolService.getPersonByThreeFactor(dimension1.getDimensionName(), userId, parmMap);
                //测试消费三次
                System.out.println("数据池服务返回结果:");
                System.out.println(resp);

                if (resp != null && resp.length() > 0) {
                    JSONObject respData = JSONObject.parseObject(resp);

                    //判断如果含有code,说明失败
                    if (respData.get("code") != null) {
                        //5001 5002 5003 4002 //worker暂停,worker总标识,false   发邮件

                        //todo 运营端手动处理开启

                        //公司客户,验真异常,总    list[unicode]  redis,手动开启.

                        status = 0;
                        saveApiData(taskId, resp, taskCycle, status, "code!=null", userId, parmMap,jobId,null);
                        Boolean booleanReult = jobRegisterService.changeCustomCount(taskRole, 0,1);
                        if (!booleanReult){
                            System.out.println("消费失败,更新redis缓存失败!");
                        }
                    } else {
                        Boolean booleanReult = jobRegisterService.changeCustomCount(taskRole, 1,1);
                        if (!booleanReult){
                            System.out.println("消费成功,更新redis缓存失败!");
                        }
                        if (respData.get("alert") == null) {
                            status = 0;
                            saveApiData(taskId, resp, taskCycle, status, "alert=null", userId, parmMap,jobId,null);
                        } else if ((Boolean) respData.get("alert")) {
                            status = 3;
                            saveApiData(taskId, resp, taskCycle, status, "alert=true", userId, parmMap,jobId,null);
                        } else if (!(Boolean) respData.get("alert")) {
                            status = 1;
                            saveApiData(taskId, resp, taskCycle, status, "alert=false", userId, parmMap,jobId,null);
                        } else {
                            status = 0;
                            saveApiData(taskId, resp, taskCycle, status, "alert!=false&&alert!=true", userId, parmMap,jobId,null);
                        }
                    }
                 }
                else {
                    System.out.println("数据池服务返回结果为空值");
                    throw new RcsException("数据池服务返回结果为空值");
                }
            }
            if (taskCycle >= cycleNum) {
                HashMap hashMap = new HashMap(3);
                hashMap.put("taskStatus", Constant.DistributeStatus.OVER.getValue());
                hashMap.put("userId", userId);
                hashMap.put("taskId", taskId);
                scheduleTaskService.updateByPrimaryKey(hashMap);
            }

            //回调消息确认消费
            updateStatusBatch(taskId, userId, status,scheduleJob);
            scheduleJobService.update(scheduleJob);

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String msg = sw.toString();
            if (message.getFailCount() >= 2) {
                //如果失败次数已经是3次,需要记录结果
                saveApiData(taskId, "", Integer.valueOf(taskCycle), 0, msg, userId, parmMap, jobId, null);
                updateStatusBatch(taskId, userId, status, scheduleJob);

                scheduleJobService.update(scheduleJob);
                //todo 记录消息接收成功
            } else {
                //说明没有记录日志
                if (parmMap.get("id") == null || "".equals(parmMap.get("id"))) {
                    saveApiData(taskId, null, taskCycle, 2, null, userId, parmMap, jobId, null);
                }
                throw e;
            }
        }
//         finally {
//            long handleTime = System.currentTimeMillis() - startTime;
//            saveApiData(taskId, null, taskCycle, 1, null, userId, parmMap,jobId,handleTime);
//        }
    }

    private void updateStatusBatch(Long taskId, long userId, Integer status,ScheduleJob scheduleJob) {
        ArrayList<ScheduleTask> arrayList = new ArrayList<>();
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setId(taskId);
        scheduleTask.setDistributeStatus(0);
        scheduleTask.setUpdateTime(new Date());
        scheduleTask.setExecuteStatus(status);
        if(scheduleJob.getHitPolicy() == 0 && (status == 3 || status == 0)){
           scheduleTask.setTaskStatus(3);
        }
        if(status == 0){
            scheduleTask.setTaskStatus(3);
        }
        arrayList.add(scheduleTask);
        scheduleTaskService.updateStatusToInit(arrayList, userId);

    }

    private void saveApiData(Long taskId, String result, int cyclNum, int status, String remark, Long userId, HashMap<String, Object> parmMap,Long jobId,Long handleTime) {
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(taskId);

        taskLog.setCreateTime(new Date());
        if (result != null) {
            taskLog.setResultMap(result);
        }
        taskLog.setCycleNum(cyclNum);
        taskLog.setStatus(status);
        taskLog.setRemark(remark);
        taskLog.setUserId(userId);
        taskLog.setJobId(jobId);
//        taskLog.setHandleTime(handleTime);
        if (status == 2) {
            synchronized (this){
                taskLogService.save(taskLog, userId);
                parmMap.put("id", taskLog.getId() + "");
            }
        } else {
            taskLog.setId(Long.valueOf((String) parmMap.get("id")));
            taskLogService.update(taskLog, userId);
        }

    }

}