package com.tweetapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tweetapp.kafka.Producer;
import com.tweetapp.model.Reply;
import com.tweetapp.responses.ApiResponse;
import com.tweetapp.service.ReplyService;

@Controller
@RequestMapping("/tweets")
public class ReplyController {
	@Autowired
	ReplyService replyService;
	@Autowired
	ApiResponse apiResponse;

	@Autowired
	private Producer producer;
	
	@PostMapping(path = "/reply", produces = "application/json")
	public ResponseEntity<Object> replyToTweet(@RequestBody Reply reply, Authentication authentication)
			throws Exception {

		Reply savedReply = replyService.replyToTweet(authentication, reply.getTweetId(), reply);
		apiResponse.setData(savedReply);
		apiResponse.setMessage("Reply created!");
		
		producer.sendMessage("Replied to a tweet");
		
		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

	@GetMapping(path = "/{tweet_id}/reply/all", produces = "application/json")
	public ResponseEntity<Object> getRepliesByTweet(@PathVariable("tweet_id") String tweetId) throws Exception {
		List<Reply> tweetComments = replyService.getRepliesByTweet(tweetId);
		apiResponse.setData(tweetComments);
		apiResponse.setMessage("tweet comments");
		
		producer.sendMessage("Get all tweets called");
		
		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

	@DeleteMapping(path = "/reply/{reply_id}", produces = "application/json")
	public ResponseEntity<Object> deleteReply(Authentication authentication,
			@PathVariable("reply_id") String replyId) throws Exception {
		Reply deletedReply = replyService.deleteReply(authentication, replyId);
		apiResponse.setData(deletedReply);
		apiResponse.setMessage("Reply deleted");
		
		producer.sendMessage("Deleted a tweet");
		
		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

}
