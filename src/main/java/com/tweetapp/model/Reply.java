package com.tweetapp.model;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection="replies")
public class Reply {
	@Id
	private String replyId;
	private String replyText;
	
	private String tweetId;
	private String userName;
	
	private Timestamp replyCreatedAt;
	private Timestamp replyUpdatedAt;
	
}
