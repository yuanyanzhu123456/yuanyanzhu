package com.geo.rcs.modules.batchtest.rabbitmq.receiver;

import com.geo.rcs.modules.batchtest.uploadfile.entity.MessageItem;

public class ThreadPoolTest implements Runnable {
	private MessageItem message;
	public ThreadPoolTest(MessageItem message) {
		// TODO Auto-generated constructor stub
		this.message=message;
	}

	@Override
    public void run() {
		synchronized (this) {
			System.out.println(Thread.currentThread().getName()+message.toString());
//				Thread.sleep(3000);
		}
	}
	}