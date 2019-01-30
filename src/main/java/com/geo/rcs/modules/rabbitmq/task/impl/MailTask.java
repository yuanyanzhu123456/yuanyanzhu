package com.geo.rcs.modules.rabbitmq.task.impl;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.util.mail.MailService;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.geo.rcs.modules.rabbitmq.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 16:23 2018/7/26
 */
@Component
public class MailTask implements Task{
    @Autowired
    private MailService mailService;
    @Override
    public void executeTask(Message message) {
        HashMap<String, Object> parmMap = message.getTaskMethodParmMap();
        String type= String.valueOf(parmMap.get("type"));
        String parms= String.valueOf(parmMap.get("parms"));
        HashMap hashMap = JSON.parseObject(parms, HashMap.class);
        try {
            mailService.sendMail(type,hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
