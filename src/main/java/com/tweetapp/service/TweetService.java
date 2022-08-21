package com.tweetapp.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

@Service
public class TweetService {

	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	UserRepository userRepository;

	// create new tweet
	public Tweet createTweet(Authentication authentication, Tweet newTweet) {
		User LoggedInUser = userRepository.findByUserName(authentication.getName());
		newTweet.setUserName(LoggedInUser.getUserName());
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		newTweet.setTweet_created_at(currentTimestamp);
		newTweet.setTweet_updated_at(currentTimestamp);
		
		return tweetRepository.save(newTweet);
	}

	// get tweet by id
	public Tweet getTweet(String tweet_id) throws Exception {
		Tweet tweet = tweetRepository.findByTweetId(tweet_id);
		if (tweet == null) {
			throw new Exception("No tweet found!");
		}
		return tweet;
	}

	// get all tweets
	public List<Tweet> getTweets() {
		List<Tweet> allTweets = tweetRepository.findAll();

		return allTweets;
	}

	// get tweets by username
	public List<Tweet> getUserTweets(String userName) {
		List<Tweet> userTweets = tweetRepository.findByUserName(userName);

		return userTweets;
	}

	// update tweet
	public Tweet updateTweet(Authentication authentication, String tweet_id, String message) throws Exception {
		Tweet tweetToUpdate = tweetRepository.findByTweetId(tweet_id);
		User loggedInUser = userRepository.findByUserName(authentication.getName());
		if (tweetToUpdate == null) {
			throw new Exception("Tweet not found");
		}
		if (!loggedInUser.getUserName().equals(tweetToUpdate.getUserName())) {
			throw new Exception("No permission to edit this tweet!");
		}
		tweetToUpdate.setTweet_message(message);
		tweetToUpdate.setTweet_updated_at(new Timestamp(System.currentTimeMillis()));
		return tweetRepository.save(tweetToUpdate);
	}

	// delete tweet
	public Tweet deleteTweet(Authentication authentication, String tweetId) throws Exception {
		Tweet tweetToDelete = tweetRepository.findByTweetId(tweetId);
		if (tweetToDelete == null) {
			throw new Exception("Tweet to delete not found!");
		}

		User LoggedInUser = userRepository.findByUserName(authentication.getName());

		if (!LoggedInUser.getUserName().equals(LoggedInUser.getUserName())) {
			throw new Exception("You have no rights to delete this tweet!");
		}

		tweetRepository.delete(tweetToDelete);

		return tweetToDelete;
	}

}
