package com.tweetapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.ReplyRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.ReplyService;

import io.jsonwebtoken.lang.Assert;

@SpringBootTest
public class ReplyServiceTest {

	@InjectMocks
	ReplyService replyService;
	@Mock
	TweetRepository tweetRepository;

	@Mock
	ReplyRepository replyRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	Authentication authentication;

	@Test
	void test_create_new_reply() throws Exception {
		User loggedInUser = new User();
		loggedInUser.setUserName("gaurav_singh");

		Tweet tweet = new Tweet();
		tweet.setTweetId("1");

		Reply reply = new Reply();
		reply.setReplyText("New Reply");
		reply.setUserName("gaurav_singh");

		Mockito.when(authentication.getName()).thenReturn("gaurav_singh");
		Mockito.when(userRepository.findByUserName("gaurav_singh")).thenReturn(loggedInUser);
		Mockito.when(tweetRepository.findByTweetId("1")).thenReturn(tweet);

		Mockito.when(replyRepository.save(any(Reply.class))).thenReturn(reply);

		Reply savedReply = replyService.replyToTweet(authentication, "1", reply);
		Assert.isTrue(savedReply instanceof Reply);
		assertEquals("New Reply", savedReply.getReplyText());
		assertEquals(loggedInUser.getUserName(), savedReply.getUserName());
	}

	@Test
	void test_delete_reply() throws Exception {
		User loggedInUser = new User();
		loggedInUser.setUserName("gaurav_singh");

		Reply reply = new Reply();
		reply.setReplyText("New Reply");
		reply.setReplyId("1");
		reply.setUserName("gaurav_singh");

		Mockito.when(authentication.getName()).thenReturn("gaurav_singh");
		Mockito.when(userRepository.findByUserName("gaurav_singh")).thenReturn(loggedInUser);
		Mockito.when(replyRepository.findByReplyId("1")).thenReturn(reply);

		// run
		Reply deletedReply = replyService.deleteReply(authentication, "1");

		// Assertions
		assertEquals("New Reply", deletedReply.getReplyText());
	}

}
