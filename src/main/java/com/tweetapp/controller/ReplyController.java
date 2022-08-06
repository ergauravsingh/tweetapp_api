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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.responses.ApiResponse;
import com.tweetapp.service.ReplyService;

@Controller
@RequestMapping("/tweets")
public class ReplyController {
	@Autowired
	ReplyService replyService;
	@Autowired
	ApiResponse apiResponse;

	@PostMapping(path = "/reply", produces = "application/json")
	public ResponseEntity<Object> replyToTweet(@RequestBody Reply reply, Authentication authentication)
			throws Exception {

		Reply savedReply = replyService.replyToTweet(authentication, reply.getTweetId(), reply);
		apiResponse.setData(savedReply);
		apiResponse.setMessage("Reply created!");
		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

	@GetMapping(path = "/{tweet_id}/reply/all", produces = "application/json")
	public ResponseEntity<Object> getRepliesByTweet(@PathVariable("tweet_id") String tweet_id) throws Exception {
		List<Reply> tweet_comments = replyService.getRepliesByTweet(tweet_id);
		apiResponse.setData(tweet_comments);
		apiResponse.setMessage("tweet comments");
		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

	@DeleteMapping(path = "/reply/{reply_id}", produces = "application/json")
	public ResponseEntity<Object> deleteReply(Authentication authentication,
			@PathVariable("reply_id") String replyId) throws Exception {
		Reply deletedReply = replyService.deleteReply(authentication, replyId);
		apiResponse.setData(deletedReply);
		apiResponse.setMessage("Reply deleted");
		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

}
