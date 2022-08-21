package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.ReplyDto;
import com.tweetapp.dto.TweetDto;
import com.tweetapp.dto.UserDto;
import com.tweetapp.kafka.Producer;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.ReplyRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TweetRepository tweetRepository;
	@Autowired
	private ReplyRepository replyRepository;

	@Autowired
	private Producer producer;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

		if (!username.isEmpty()) {
			producer.sendMessage("User created with userid -> " + username);
			logger.info("User created with userid -> {}", username);
		}
		return userRepository.save(user);
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
		if(!user.getUserName().isEmpty()) {
			producer.sendMessage("Changed Password for User -> " + user.getUserName());
			logger.info("Changed Password for User -> {}",user.getUserName());
		}
		
		return userRepository.save(user);
	}

	public List<UserDto> listUsers(Authentication authentication) {
		List<UserDto> userList = new ArrayList<>();
		User loggedInUser = userRepository.findByUserName(authentication.getName());

		// get all users
		List<User> savedUsers = userRepository.findAll();

		if (savedUsers != null) {
			for (User user : savedUsers) {
				if (!user.getUserName().equals(loggedInUser.getUserName())) {
					UserDto userDto = new UserDto();
					userDto.setUserName(user.getUserName());
					userDto.setFirstName(user.getFirstName());
					userDto.setLastName(user.getLastName());

					// get all tweets of users
					List<Tweet> savedTweets = tweetRepository.findByUserName(user.getUserName());

					if (savedTweets != null) {
						List<TweetDto> tweetList = new ArrayList<>();
						for (Tweet tweet : savedTweets) {
							// prepare tweet dto
							TweetDto tweetDto = new TweetDto();
							tweetDto.setTweet_message(tweet.getTweet_message());
							tweetDto.setLikes(tweet.getLikes());
							tweetDto.setUserName(tweet.getUserName());
							tweetDto.setTweet_created_at(tweet.getTweet_created_at());
							tweetDto.setTweet_updated_at(tweet.getTweet_updated_at());

							// get all replies of tweet
							List<Reply> savedReplies = replyRepository.findByTweetId(tweet.getTweetId());

							if (savedReplies != null) {
								List<ReplyDto> replyList = new ArrayList<>();
								for (Reply reply : savedReplies) {
									// prepare reply dto
									ReplyDto replyDto = new ReplyDto();
									replyDto.setReplyText(reply.getReplyText());
									replyDto.setUserName(reply.getUserName());
									replyDto.setReplyCreatedAt(reply.getReplyCreatedAt());
									replyDto.setReplyUpdatedAt(reply.getReplyUpdatedAt());
									replyList.add(replyDto);
								}
								tweetDto.setReplies(replyList);
							}
							tweetList.add(tweetDto);
						}
						userDto.setTweets(tweetList);
					}
					userList.add(userDto);

				}
			}
		}

		return userList;
	}

	private Boolean doesUserExists(User user) {
		if (userRepository.findByUserName(user.getUserName()) != null
				|| userRepository.findByEmail(user.getEmail()) != null)
			return true;

		return false;
	}
}
