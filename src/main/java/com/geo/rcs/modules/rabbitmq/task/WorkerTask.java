package com.geo.rcs.modules.rabbitmq.task;

import com.geo.rcs.modules.api.modules.eventin.controller.ApiEventInController;

import com.geo.rcs.modules.rabbitmq.message.Message;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description:
 * @param:
 * @return:
 * @auther: qiaoShengLong
 * @date: 2018/7/24 11:36
 */
public interface WorkerTask {

    /**
     * @Description:
     * @param: taskType:任务类型 mail api parmMap:参数
     * @return:
     * @auther: qiaoShengLong
     * @date: 2018/7/24 11:36
     */
     void doTask(Message message, String taskType) throws Exception;



}
