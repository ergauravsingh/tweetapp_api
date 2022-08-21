package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.dto.Login;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.responses.ApiResponse;
import com.tweetapp.service.CustomUserDetailsService;
import com.tweetapp.service.UserService;
import com.tweetapp.util.JwtUtil;

@RestController
@RequestMapping("/tweets")
public class AuthController {

	@Autowired
	private UserService userCrudService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	private ApiResponse apiResponse;
	@Autowired
	private Login loginObject;
	@Autowired
	private UserRepository userRepository;

	/**
	 * Register New User
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> register(@RequestBody User user) throws Exception {
		User newUser = userCrudService.createNewUser(user);
		apiResponse.setMessage("User created!");
		apiResponse.setData(newUser);

		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.CREATED);
	}

	/**
	 * Authenticate user by userId, password
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> authenticateUser(@RequestBody User user) {
		// Create authentication object and authenticate with Authentication Manager
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
		authenticationManager.authenticate(auth);
		
		// Spring user returned by User Details Service
		final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		User LoggedInUser = userRepository.findByUserName(user.getUserName());
		
		// Prepare Login Return Object - with JWT Token
		loginObject.setJwt(jwt);
		loginObject.setUsername(user.getUserName());
		loginObject.setUsername(LoggedInUser.getUserName());

		apiResponse.setMessage("Auth Token Generated");
		apiResponse.setData(loginObject);

		return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
	}
}
