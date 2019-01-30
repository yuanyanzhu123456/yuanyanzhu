package com.geo.rcs.modules.batchtest.rabbitmq.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.geo.rcs.modules.batchtest.uploadfile.entity.MessageItem;

import java.util.Date;

@Component
public class ObjectSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send(MessageItem message) {
		this.rabbitTemplate.convertAndSend("object", message);
	}

}