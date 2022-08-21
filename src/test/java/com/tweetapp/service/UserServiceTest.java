package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tweetapp.kafka.Producer;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

import io.jsonwebtoken.lang.Assert;

@SpringBootTest
class UserServiceTest {

	@InjectMocks
	TweetService tweetService;

	@InjectMocks
	UserService userService;

	@Mock
	PasswordEncoder passwordEncoder;

	@Mock
	TweetRepository tweetRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	Authentication authentication;
	
	@Mock
	Producer producer;

	@Test
	void test_create_new_user() throws Exception {
		User user = new User();
		user.setUserName("gaurav_singh");
		user.setEmail("abc@gmail.com");
		user.setFirstName("Gaurav");
		user.setLastName("Singh");
		user.setPassword("Password@1");

		Mockito.when(userRepository.findByUserName("gaurav_singh")).thenReturn(null);
		Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn("Password@1");
		Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
		User savedUser = userService.createNewUser(user);
		Assert.isTrue(savedUser instanceof User);
		assertEquals("gaurav_singh", savedUser.getUserName());
	}

}
