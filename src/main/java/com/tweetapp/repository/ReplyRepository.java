package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.Reply;

@Repository
public interface ReplyRepository extends MongoRepository<Reply, String>{
	
	Reply findByReplyId(String replyId);

	List<Reply> findByTweetId(String tweetId);

}
