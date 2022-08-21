package com.tweetapp.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Producer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	private static final String TOPICNAME = "TweetApp";

	public void sendMessage(String msg) {
		if(msg != null) {
			String json = "{\"message\":"+"\""+ msg + "\"}";
			logger.info("Producer :: Message Sent -> {}", msg);
			kafkaTemplate.send(TOPICNAME, json);
		}else {
			logger.info("Message not sent");
		}
	}

}
