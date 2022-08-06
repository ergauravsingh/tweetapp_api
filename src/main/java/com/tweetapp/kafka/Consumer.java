package com.tweetapp.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String TOPICNAME = "TweetApp";

	@KafkaListener(topics = TOPICNAME, groupId = "tweet-group")
	public String listen(String message) {
		logger.info("Consumer :: Message Recieved ->" + message);
		return "message received:" + message;

	}

}
