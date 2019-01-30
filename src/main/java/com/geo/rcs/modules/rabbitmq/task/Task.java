package com.geo.rcs.modules.rabbitmq.task;

import com.geo.rcs.modules.rabbitmq.message.Message;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 15:51 2018/7/26
 */
public interface Task {

   public abstract void  executeTask(Message message) throws Exception;
}
