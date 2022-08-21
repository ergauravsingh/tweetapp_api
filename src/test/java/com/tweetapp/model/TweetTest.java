package com.tweetapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TweetTest {

	Tweet tweet = new Tweet();

	@Test
	void setTweetIdTest() {
		tweet.setTweetId("1");
		assertEquals("1", tweet.getTweetId());
	}

	@Test
	void setLikesTest() {
		tweet.setLikes(2);
		assertEquals(2, tweet.getLikes());
	}
	
	@Test
	void setUserName() {
		tweet.setUserName("gaurav_singh");
		assertEquals("gaurav_singh", tweet.getUserName());
	}

	
}