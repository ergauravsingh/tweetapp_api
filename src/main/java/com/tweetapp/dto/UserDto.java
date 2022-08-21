package com.tweetapp.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
	private String userName;
	private String firstName;
	private String lastName;

	private List<TweetDto> tweets;
}
