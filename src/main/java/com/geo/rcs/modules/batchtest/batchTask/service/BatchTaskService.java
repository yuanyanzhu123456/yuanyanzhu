package com.geo.rcs.modules.batchtest.batchTask.service;

import java.util.concurrent.PriorityBlockingQueue;

import com.geo.rcs.modules.batchtest.batchTask.entity.BatchTask;
import com.geo.rcs.modules.batchtest.uploadfile.entity.MessageItem;

/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月23日 下午4:06:21 
 */
public interface BatchTaskService {

	void start(BatchTask batchTask);

	BatchTask selectByPrimaryKey(long id);
}
