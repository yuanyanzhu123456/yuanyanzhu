package com.geo.rcs.modules.rabbitmq.task.impl;

import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.mail.MailService;
import com.geo.rcs.modules.datapool.service.DataPoolService;
import com.geo.rcs.modules.rabbitmq.constant.MqConstant;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.geo.rcs.modules.rabbitmq.task.WorkerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 11:32 2018/7/24
 */
@Component
public class WorkerTaskImpl implements WorkerTask{

    @Autowired
    private MailService mailService;
    @Autowired
    private DataPoolService dataPoolService;
    @Autowired
    private MonitorTask apiTask;
    @Autowired
    private MailTask mailTask;
    @Autowired
    private AbEventTask abEventTask;

    @Override
    public void doTask(Message message,String taskType) throws Exception {
        long start = System.currentTimeMillis();
        HashMap<String, Object> parmMap = message.getTaskMethodParmMap();
        LogUtil.info("开始执行任务","任务类型:"+taskType+" 入参:"+parmMap,"系统");
//        System.out.println("任务类型:   "+taskType + "   入参:   "+parmMap.toString());
            if (MqConstant.TaskType.MAIL_TASK.getMessage().equalsIgnoreCase(taskType)){
                mailTask.executeTask(message);
            }else if(MqConstant.TaskType.API_TASK.getMessage().equalsIgnoreCase(taskType)) {
                apiTask.executeTask(message);
            }else if (MqConstant.TaskType.AB_EVENT_TASK.getMessage().equalsIgnoreCase(taskType)){
                abEventTask.executeTask(message);
            }
        long time = System.currentTimeMillis() - start;
        LogUtil.info("任务结束","耗时:"+time,"系统");
    }
}
