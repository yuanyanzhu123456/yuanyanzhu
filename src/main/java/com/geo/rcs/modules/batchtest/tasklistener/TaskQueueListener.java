/*
package com.geo.rcs.modules.batchtest.tasklistener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.geo.rcs.modules.batchtest.rabbitmq.sender.ObjectSender;
import com.geo.rcs.modules.batchtest.uploadfile.entity.MessageItem;

*/
/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月24日 下午2:34:48
 *//*

@WebListener
public class TaskQueueListener implements ServletContextListener{

	private ObjectSender sender;


	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
				 System.out.println("初始化 application");  
			        ServletContext application = sce.getServletContext();  
			        WebApplicationContext appctx = WebApplicationContextUtils.getWebApplicationContext(application);  
			      sender = appctx.getBean(ObjectSender.class); 
			      //开启线程
			      new Thread(new Runnable(){  
			            public void run(){  
			        		System.out.println("##########任务队列监听开启!##########");
			            	while (true) {
			        			while (TaskQueue.getTaskQUeue().size() >=6) {
			        				while (TaskQueue.getTaskQUeue().size() >0) {
										try {
											MessageItem message = TaskQueue.getTaskQUeue().take();
											sender.send(message);
											message = null;
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} 
									}
			        			}
			        		}
			            }}).start();  
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}



	
}
*/
