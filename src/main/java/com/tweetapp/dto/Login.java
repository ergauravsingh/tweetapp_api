package com.tweetapp.dto;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Login {
	private String username;
	private String jwt;
	private String firstName;
	private String lastName;
}
