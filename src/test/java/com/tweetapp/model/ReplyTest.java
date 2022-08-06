package com.tweetapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReplyTest {

	Reply reply = new Reply();

	@Test
	void setReplyIdTest() {
		reply.setReplyId("1");
		assertEquals("1", reply.getReplyId());
	}

	
	@Test
	void setReplyText() {
		reply.setReplyText("Reply Created");
		assertEquals("Reply Created", reply.getReplyText());
	}
	
	@Test
	void setTweetId() {
		reply.setTweetId("1");
		assertEquals("1", reply.getTweetId());
	}
	
	@Test
	void setUserName() {
		reply.setUserName("gaurav_singh");
		assertEquals("gaurav_singh", reply.getUserName());
	}
}