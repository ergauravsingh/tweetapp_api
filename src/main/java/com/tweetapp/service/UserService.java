package com.tweetapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.Tweeter;
import com.tweetapp.dto.TweetersList;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository USER_REPOSITORY;

	static final String USER_NAME_REGEX = "[A-Za-z0-9_]+";
	static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}";
	static final String USER_EMAIL_REGEX = "^(.+)@(.+)$";

	public User createNewUser(User user) throws Exception {
		if (Boolean.TRUE.equals(doesUserExists(user)))
			throw new Exception("User already exists!");

		String userEmail = user.getEmail().trim();
		String username = user.getUserName().trim();
		String userPassword = user.getPassword().trim();

		boolean invalidUserName = (username == null) || !username.matches(USER_NAME_REGEX);
		/*
		 * Email Restriction --------------------- This expression matches email
		 * addresses, and checks that they are of the proper form. It checks to ensure
		 * the top level domain is between 2 and 4 characters long, but does not check
		 * the specific domain against a list (especially since there are so many of
		 * them now).
		 */
		boolean invalidEmail = (userEmail == null) || !userEmail.matches(USER_EMAIL_REGEX);

		/*
		 * Password Restriction ------------------------ At least 8 chars Contains at
		 * least one digit Contains at least one lower alpha char and one upper alpha
		 * char Contains at least one char within a set of special chars (@#%$^ etc.)
		 * Does not contain space, tab, etc.
		 */
		boolean invalidPassword = (userPassword == null) || !userPassword.matches(PASSWORD_REGEX);

		if (invalidUserName)
			throw new Exception("Username can only contain letters, numbers and \'_\'");
		if (invalidPassword)
			throw new Exception("Password not valid");
		if (invalidEmail)
			throw new Exception("Email not valid!");

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println(user);
		return USER_REPOSITORY.save(user);
	}

	public User changePassword(User user) throws Exception {
		String userPassword = user.getPassword().trim();
		/*
		 * Password Restriction ------------------------ At least 8 chars Contains at
		 * least one digit Contains at least one lower alpha char and one upper alpha
		 * char Contains at least one char within a set of special chars (@#%$^ etc.)
		 * Does not contain space, tab, etc.
		 */
		boolean invalidPassword = (userPassword == null) || !userPassword.matches(PASSWORD_REGEX);

		if (invalidPassword)
			throw new Exception("Password not set or not valid");

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return USER_REPOSITORY.save(user);
	}

	public TweetersList listUsers(Authentication authentication) {
		User LoggedInUser = USER_REPOSITORY.findByUserName(authentication.getName());
		List<User> userList = USER_REPOSITORY.findAll();

		TweetersList tweetersList = new TweetersList();
		for (User user : userList) {
			Tweeter tweeter = new Tweeter();
			tweeter.username = user.getUserName();
			tweeter.email = user.getEmail();
			tweetersList.tweetersList.add(tweeter);
		}

		return tweetersList;
	}

	private Boolean doesUserExists(User user) {
		if (USER_REPOSITORY.findByUserName(user.getUserName()) != null
				|| USER_REPOSITORY.findByEmail(user.getEmail()) != null)
			return true;

		return false;
	}
}
