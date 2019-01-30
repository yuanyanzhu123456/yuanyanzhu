/*
package com.geo.rcs.modules.batchtest.rabbitmq.receiver;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.geo.rcs.modules.batchtest.uploadfile.entity.MessageItem;

@Component
public class ObjectReceiver2 {
	ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5);
	ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6, 1, TimeUnit.DAYS, queue);

	@RabbitListener(queues = "object")
	@RabbitHandler
	public void process(MessageItem message) {
		System.out.println("Receiver object2 : " + message.toString());
		executor.execute(new Thread(new ThreadPoolTest(message), "TestThread".concat("a")));
		int threadSize = queue.size();
		System.out.println("线程队列大小为-->" + threadSize);
		while (executor.getActiveCount() + queue.size() == 11) {
			System.out.println("接收222222最大线程数并且,线程队列已满5个进入等待");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
*/
