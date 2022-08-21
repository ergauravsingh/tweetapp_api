package com.tweetapp.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TweetDto {

	private String tweet_message;
	private int likes;
	private Timestamp tweet_created_at;
	private Timestamp tweet_updated_at;

	private String userName;
	private List<ReplyDto> replies;

}
