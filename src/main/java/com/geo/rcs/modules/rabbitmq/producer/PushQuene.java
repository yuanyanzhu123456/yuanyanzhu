package com.geo.rcs.modules.rabbitmq.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.itextpdf.text.log.SysoCounter;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.alibaba.fastjson.JSON;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class PushQuene {

	private static final String TASK_QUEUE_NAME1 = "Mail-high";
	private static final String TASK_QUEUE_NAME2 = "Mail-mid";
	private static final String TASK_QUEUE_NAME3 = "Mail-super";
	private static final String TASK_QUEUE_NAME4 = "Mail-low";

	public static void main(String[] argv) throws java.io.IOException, TimeoutException {

		Message message = null;
		ConnectionFactory factory = new ConnectionFactory();
//		 factory.setHost("localhost");
		 factory.setHost("10.111.32.144");
//		factory.setUsername("guest");
		factory.setUsername("rcs");
//		factory.setPassword("guest");
		factory.setPassword("rcs@geotmt123");
		factory.setPort(5672);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		Map<String, String> parmMap = new HashMap<>();
		// 指定队列持久化
//		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

		HashMap<String, Object> hashMap = new HashMap<>();
		for (int i = 0; i < 30; i++) {
			if (i == 2) {
				hashMap.put("name", "小明");
			} else {
				hashMap.put("name", "小红");

			}
			message = new Message(Long.valueOf(i), hashMap);
			String beanToJson = JSON.toJSONString(message);
			// 指定消息持久化

			channel.basicPublish("", TASK_QUEUE_NAME1, MessageProperties.PERSISTENT_TEXT_PLAIN, beanToJson.getBytes());
			channel.basicPublish("", TASK_QUEUE_NAME2, MessageProperties.PERSISTENT_TEXT_PLAIN, beanToJson.getBytes());
			channel.basicPublish("", TASK_QUEUE_NAME3, MessageProperties.PERSISTENT_TEXT_PLAIN, beanToJson.getBytes());
			channel.basicPublish("", TASK_QUEUE_NAME4, MessageProperties.PERSISTENT_TEXT_PLAIN, beanToJson.getBytes());
			System.out.println(" [x] Sent '" + beanToJson + "'");
		}
		long messageCount = channel.messageCount(TASK_QUEUE_NAME1);
		System.out.println("队列大小:"+messageCount);

//		channel.queueDelete("queue2");
//		channel.queueDelete("queue1");
//		channel.queueDelete("topic.message");
		channel.close();
		connection.close();
	}

	// ...
}