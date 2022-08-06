package com.tweetapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTest {

	User user = new User();

	@Test
	void setUserNameTest() {
		user.setUserName("gaurav_singh");
		assertEquals("gaurav_singh", user.getUserName());
	}

	@Test
	void setFirstNameTest() {
		user.setFirstName("Gaurav");
		assertEquals("Gaurav", user.getFirstName());
	}

	@Test
	void setLastNameTest() {
		user.setLastName("Singh");
		assertEquals("Singh", user.getLastName());
	}

	@Test
	void setEmailTest() {
		user.setEmail("gaurav@gmail.com");
		assertEquals("gaurav@gmail.com", user.getEmail());
	}
}