package com.geo.rcs.modules.rabbitmq.task.impl;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.abtest.service.AbTestService;
import com.geo.rcs.modules.api.modules.decision.DecisionEngineApi;
import com.geo.rcs.modules.api.modules.eventin.controller.ApiEventInController;
import com.geo.rcs.modules.decision.entity.DecisionForApi;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.geo.rcs.modules.rabbitmq.task.Task;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 10:35 2018/9/7
 */
@Component
public class AbEventTask implements Task {

    @Autowired
    private DecisionEngineApi decisionEngineApi;
    @Autowired
    private AbTestService abTestService;
    @Autowired
    private JobRegisterService jobRegisterService;
    @Value("${taskrole}")
    private String taskRole;

    @Override
    public void executeTask(Message message) throws Exception {
        HashMap<String, Object> parmMap = null;
        SysUser sysUser = null;
        try {
            parmMap = message.getTaskMethodParmMap();
            HashMap<String, Object> parmMapobj = new HashMap<>();
            parmMapobj.putAll(parmMap);
            sysUser = JSON.parseObject(String.valueOf(parmMap.get("sysUser")), SysUser.class);

            try {
                String result = "";
                Geo geo = decisionEngineApi.eventIn(parmMapobj, null, null, sysUser);
                Integer sysStatus = 0 ;
                String msg = "";

                if (2000 == ((int) geo.get("code"))) {
                    DecisionForApi decisionForApi = (DecisionForApi) geo.get("data");
                    if(decisionForApi==null){
                        result = "决策数据为空";
                        LogUtil.error(result, "执行ABTest任务", String.valueOf(parmMap.get("sysUser")), new Exception()) ;
                        sysStatus = 5;
                    }else{
                        result = JSON.toJSONString(decisionForApi);
                        sysStatus = decisionForApi.getSysStatus();
                    }
                } else {
                    System.out.printf("geo结果"+geo.toString());
                    msg = geo.get("msg") == null ? "规则引擎返回数据异常" +geo : "规则引擎返回数据异常，原因为"+(String) geo.get("msg");
                    LogUtil.error(msg, "执行ABTest任务", String.valueOf(parmMap.get("sysUser")), new Exception()) ;
                    result = msg;
                    sysStatus = 5;
                }
                decisionCallBack(message.getId(), sysUser, parmMap, sysStatus, result);
                jobRegisterService.changeCustomCount(taskRole, 1, 1);
            }
            catch (Exception e){
                LogUtil.error("决策引擎运行异常", "执行ABTest任务", String.valueOf(parmMap.get("sysUser")), e) ;
                e.printStackTrace();
                decisionCallBack(message.getId(), sysUser, parmMap, 5, "决策引擎异常"+e);
                jobRegisterService.changeCustomCount(taskRole, 0, 1);
            }


        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("决策引擎参数错误", "执行ABTest任务", String.valueOf(parmMap.get("sysUser")), e) ;
            decisionCallBack(message.getId(), sysUser, parmMap, 5, "决策参数错误"+e);
            jobRegisterService.changeCustomCount(taskRole, 0, 1);

        }
    }

    private void decisionCallBack(Long id, SysUser user, HashMap<String, Object> parmMap, Integer sysStatus, String resultJson) {
        ScheduleTask task = new ScheduleTask();
        task.setId(id);
        task.setUserId(user.getUniqueCode());
        task.setExecuteStatus(sysStatus);
        task.setResultMap(resultJson);
        task.setJobId(Integer.valueOf(String.valueOf(parmMap.get("jobId"))));
        Boolean updateDb = abTestService.msgCallbBack(task);
        if (!updateDb) {
            LogUtil.error("消息回调修改失败!", parmMap.toString(), resultJson, new Exception());
        }
    }


}
