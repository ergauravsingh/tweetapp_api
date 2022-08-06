package com.tweetapp.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Document(collection="tweets")
public class Tweet {
	@Id
	private String tweetId;
	
	private String tweet_message;
	private int likes;
	private Timestamp tweet_created_at;
	private Timestamp tweet_updated_at;
	
	private String userName;	

	
}
