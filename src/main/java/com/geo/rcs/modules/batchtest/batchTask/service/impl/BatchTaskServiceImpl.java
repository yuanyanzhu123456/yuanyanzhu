package com.geo.rcs.modules.batchtest.batchTask.service.impl;

import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geo.rcs.modules.batchtest.batchTask.dao.BatchTaskMapper;
import com.geo.rcs.modules.batchtest.batchTask.entity.BatchTask;
import com.geo.rcs.modules.batchtest.batchTask.service.BatchTaskService;
import com.geo.rcs.modules.batchtest.tasklistener.TaskQueue;
import com.geo.rcs.modules.batchtest.uploadfile.dao.MessageMapper;
import com.geo.rcs.modules.batchtest.uploadfile.entity.MessageItem;


/** 
 * @author qiaoShengLong 
 * @email  qiaoshenglong@geotmt.com
 * @time   2018年5月23日 下午4:26:31 
 */
@Service
public class BatchTaskServiceImpl implements BatchTaskService{
//	//优先对列,根据MessageItem 的priorityLevel,startDate字段比较
//	public PriorityBlockingQueue<MessageItem> taskQUeue=new PriorityBlockingQueue<>();
	@Autowired
	private BatchTaskMapper batchMapper;

	@Autowired
	private MessageMapper messageMapper;
	@Transactional
	@Override
	public void start(BatchTask batchTask) {
		// TODO Auto-generated method stub
	    //先改批次表
		PriorityBlockingQueue<MessageItem> taskQUeue = TaskQueue.getTaskQUeue();
		try {
			batchMapper.updateBatchByPrimaryKey(batchTask);
			messageMapper.updateMessageByBatch(batchTask);
			ArrayList<MessageItem> messageList=messageMapper.selectByCondition((long)batchTask.getId());
			for (int i = 0; i < messageList.size(); i++) {
				taskQUeue.add(messageList.get(i));
			}
			if (taskQUeue.size()>=12) {
				 for (MessageItem user : taskQUeue) {  
				        try {  
				            System.out.println(taskQUeue.take().toString());  
				        } catch (InterruptedException e) {  
				            e.printStackTrace();  
				        }  
				    }  
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	@Override
	public BatchTask selectByPrimaryKey(long id) {
		// TODO Auto-generated method stub
		return batchMapper.selectByPrimaryKey(id);
	}

}
