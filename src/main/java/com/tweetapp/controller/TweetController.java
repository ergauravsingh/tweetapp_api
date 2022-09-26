package com.tweetapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.kafka.Producer;
import com.tweetapp.model.Tweet;
import com.tweetapp.responses.ApiResponse;
import com.tweetapp.service.TweetService;

@RestController
@RequestMapping("/tweets")
@CrossOrigin
public class TweetController {
	
	@Autowired
	private TweetService tweetService;
	@Autowired
	private ApiResponse apiResponse;
	
	@Autowired
	private Producer producer;
	

	@GetMapping(path = "/all", produces = "application/json")
	public ResponseEntity<Object> getAllTweets(Authentication authentication) {
		List<Tweet> userFeed = tweetService.getTweets();
		apiResponse.setMessage("User Feed!");
		apiResponse.setData(userFeed);
		
		producer.sendMessage("Fetch all tweets called");
		
		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

	@GetMapping(path = "/user/{user_id}", produces = "application/json")
	public ResponseEntity<Object> getTweetsByUser(@PathVariable("user_id") String userName) {
		List<Tweet> userTweets = tweetService.getUserTweets(userName);
		apiResponse.setMessage("Tweets by user");
		apiResponse.setData(userTweets);
		
		producer.sendMessage("Get tweets for user -> " + userName);
				
		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/{tweet_id}", produces = "application/json")
	public ResponseEntity<Object> getTweet(@PathVariable("tweet_id") String tweetId) throws Exception {
		Tweet tweet = tweetService.getTweet(tweetId);
		apiResponse.setMessage("Tweet");
		apiResponse.setData(tweet);
		
		producer.sendMessage("Get tweet -> " + tweetId);

		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

	@PostMapping(path = "/create", produces = "application/json")
	public ResponseEntity<Object> createTweet(Authentication authentication, @RequestBody Tweet newTweet) {
		Tweet savedTweet = tweetService.createTweet(authentication, newTweet);
		apiResponse.setMessage("Tweet created!");
		apiResponse.setData(savedTweet);
		
		producer.sendMessage("Created a tweet");

		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.CREATED);
	}

	@PutMapping(path = "/update/{tweet_id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> updateTweet(@PathVariable("tweet_id") String tweet_id, @RequestBody @Valid Tweet tweet,
			Authentication authentication) throws Exception {
		Tweet updatedTweet = tweetService.updateTweet(authentication, tweet_id, tweet.getTweet_message());
		apiResponse.setMessage("Tweet updated!");
		apiResponse.setData(updatedTweet);
		
		producer.sendMessage("Updated a tweet ");

		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

	@DeleteMapping(path = "/{tweet_id}")
	public ResponseEntity<Object> delete(@PathVariable("tweet_id") String tweet_id, Authentication authentication)
			throws Exception {
		Tweet deletedTweet = tweetService.deleteTweet(authentication, tweet_id);
		apiResponse.setMessage("Tweet deleted!");
		apiResponse.setData(deletedTweet);
		
		producer.sendMessage("Deleted a tweet ");

		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

	
}
