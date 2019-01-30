package com.geo.rcs.modules.batchtest.tasklistener;

import java.util.concurrent.PriorityBlockingQueue;

import com.geo.rcs.modules.batchtest.uploadfile.entity.MessageItem;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月24日 下午3:04:45
 */
public class TaskQueue {
	public static final PriorityBlockingQueue<MessageItem> taskQueue = new PriorityBlockingQueue<>(10000);// 定义一个值为100的静态全局变量

	public static PriorityBlockingQueue<MessageItem> getTaskQUeue() {// 定义一个静态方法
		return taskQueue;
	}
}
