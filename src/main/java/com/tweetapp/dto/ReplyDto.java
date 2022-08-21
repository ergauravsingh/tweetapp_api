package com.tweetapp.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDto {
	
	private String replyText;
	
	private String userName;
	
	private Timestamp replyCreatedAt;
	private Timestamp replyUpdatedAt;

}
