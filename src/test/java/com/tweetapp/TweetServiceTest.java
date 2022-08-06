package com.tweetapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.TweetService;

import io.jsonwebtoken.lang.Assert;

@SpringBootTest
class TweetServiceTest {

	@InjectMocks
	TweetService tweetService;
	@Mock
	TweetRepository tweetRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	Authentication authentication;

	@Test
	void test_create_new_tweet() {
		User loggedInUser = new User();
		loggedInUser.setUserName("gaurav_singh");
		Tweet newTweet = new Tweet();
		newTweet.setTweet_message("First Tweet");
		Mockito.when(authentication.getName()).thenReturn("gaurav_singh");
		Mockito.when(userRepository.findByUserName("gaurav_singh")).thenReturn(loggedInUser);
		Mockito.when(tweetRepository.save(any(Tweet.class))).thenReturn(newTweet);

		Tweet savedTweet = tweetService.createTweet(authentication, newTweet);
		Assert.isTrue(savedTweet instanceof Tweet);
		assertEquals("First Tweet", savedTweet.getTweet_message());
		assertEquals(loggedInUser.getUserName(), savedTweet.getUserName());
	}

	@Test
	void test_update_tweet() throws Exception {
		User loggedInUser = new User();
		loggedInUser.setUserName("gaurav_singh");

		Tweet tweet = new Tweet();
		tweet.setTweetId("1");
		tweet.setTweet_message("First Tweet");
		tweet.setUserName(loggedInUser.getUserName());

		Mockito.when(tweetRepository.findByTweetId("1")).thenReturn(tweet);
		Mockito.when(tweetRepository.save(tweet)).thenReturn(tweet);
		Mockito.when(authentication.getName()).thenReturn("gaurav_singh");
		Mockito.when(userRepository.findByUserName("gaurav_singh")).thenReturn(loggedInUser);

		// run
		Tweet updatedTweet = tweetService.updateTweet(authentication, "1", "Tweet Updated");

		// Assertions
		assertEquals("Tweet Updated", updatedTweet.getTweet_message());
	}

	@Test
	void test_tweet_not_found_for_update() throws Exception {
		User loggedInUser = new User();
		loggedInUser.setUserName("gaurav_singh");
		Mockito.when(authentication.getName()).thenReturn("gaurav_singh");
		Mockito.when(userRepository.findByUserName("gaurav_singh")).thenReturn(null);
		Assertions.assertThrows(Exception.class, () -> tweetService.updateTweet(authentication, "1", "Updated Tweet"));
	}

	@Test
	void test_authentication_failed_while_updating_tweet() throws Exception {
		User loggedInUser = new User();
		loggedInUser.setUserName("gaurav_singh");
		Tweet tweet = new Tweet();
		tweet.setTweetId("1");
		tweet.setTweet_message("First Tweet");
		tweet.setUserName("other_user");
		Mockito.when(authentication.getName()).thenReturn("gaurav_singh");
		Mockito.when(userRepository.findByUserName("gaurav_singh")).thenReturn(loggedInUser);
		Mockito.when(tweetRepository.findByTweetId("1")).thenReturn(tweet);
		Assertions.assertThrows(Exception.class, () -> tweetService.updateTweet(authentication, "1", "Updated Tweet"));
	}

}
