package com.tweetapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tweetapp.dto.UserDto;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.responses.ApiResponse;
import com.tweetapp.service.UserService;

@Controller
@RequestMapping("/tweets")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ApiResponse apiResponse;

	/**
	 * Authenticate User
	 * 
	 * @param authentication
	 * @return
	 */
	@GetMapping(path = "/list", produces = "application/json")
	public ResponseEntity<Object> listOfUsers(Authentication authentication) {
		List<UserDto> userList = userService.listUsers(authentication);
		apiResponse.setData(userList);
		apiResponse.setMessage("User List");

		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}

	/**
	 * Change User Password
	 * @param authentication
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@PostMapping(path = "/change_password", produces = "application/json")
	public ResponseEntity<Object> changePassword(Authentication authentication, @RequestBody User user)
			throws Exception {
		User loggedInUser = userRepository.findByUserName(authentication.getName());
		user.setUserName(loggedInUser.getUserName());
		user.setEmail(loggedInUser.getEmail());
		user.setUserName(loggedInUser.getUserName());
		User updatedUser = userService.changePassword(user);
		apiResponse.setData(updatedUser);
		apiResponse.setMessage("Password changed");

		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}
}
