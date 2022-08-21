package com.tweetapp.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.ReplyRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

@Service
public class ReplyService {
	@Autowired
	private TweetRepository tweetRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ReplyRepository replyRepository;

	public Reply replyToTweet(Authentication authentication, String tweet_id, Reply reply) throws Exception {
		Tweet tweet = tweetRepository.findByTweetId(tweet_id);
		if (tweet == null) {
			throw new Exception("Tweet not found!");
		}
		User LoggedInUser = userRepository.findByUserName(authentication.getName());
		reply.setTweetId(tweet.getTweetId());
		reply.setUserName(LoggedInUser.getUserName());
		reply.setReplyCreatedAt(new Timestamp(System.currentTimeMillis()));
		reply.setReplyUpdatedAt(new Timestamp(System.currentTimeMillis()));

		return replyRepository.save(reply);
	}

	public Reply deleteReply(Authentication authentication, String replyId) throws Exception {
		Reply replyToDelete = replyRepository.findByReplyId(replyId);
		if (replyToDelete == null) {
			throw new Exception("Reply not found");
		}
		
		User LoggedInUser = userRepository.findByUserName(authentication.getName());
		
		if (!replyToDelete.getUserName().equals(LoggedInUser.getUserName())) {
			throw new Exception("Not authorized to delete this comment");
		}

		replyRepository.delete(replyToDelete);

		return replyToDelete;
	}

	public List<Reply> getRepliesByTweet(String tweet_id) throws Exception {
		Tweet tweet = tweetRepository.findByTweetId(tweet_id);
		if (tweet == null) {
			throw new Exception("Tweet Does Not Exist!");
		}
		return replyRepository.findByTweetId(tweet_id);
	}
}
