package com.cooksys.social_media_1.services.Impl;

import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.dtos.UserRequestDto;
import com.cooksys.social_media_1.dtos.UserResponseDto;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.entities.User;
import com.cooksys.social_media_1.exceptions.NotAuthorizedException;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.mappers.UserMapper;
import com.cooksys.social_media_1.repositories.UserRepository;
import com.cooksys.social_media_1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;
	
	// Helper method to validate the given username
	private Optional<User> validateUsername(String username) {
		Optional<User> user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		
		if(user.isEmpty()) {
			throw new NotFoundException("The given username was not found. Please try again.");
		}
		
		return user;
	}
	

	@Override
	public List<TweetResponseDto> getUserFeed(String username) {
		// Validate and get user entity
		User user = validateUsername(username).get();
		
		// Get users followed by this user
		List<User> userFollowing = user.getFollowing();
		
		// Get list of Tweets from this user and add all Tweets from followed users
		List<Tweet> userTweets = user.getTweets();
		for(User followedUser : userFollowing) {
			userTweets.addAll(followedUser.getTweets());
		}
		
		return tweetMapper.entitiesToDTOs(userTweets);
	}

	@Override
	public List<UserResponseDto> getUserFollowers(String username) {		
		// Validate and get user's followers
		List<User> followers = validateUsername(username).get().getFollowers();
		
		return userMapper.entitiesToDtos(followers);
	}

	@Override
	public List<UserResponseDto> getFollowedUsers(String username) {
		// Validate and get followed users
		List<User> followedUsers = validateUsername(username).get().getFollowing();
		
		
		return userMapper.entitiesToDtos(followedUsers);
	}

	@Override
	public void followUser(String username, CredentialsRequestDto credentialsRequestDto) {
		// Validate and get user to follow
		User userToFollow = validateUsername(username).get();
		return;
	}

	@Override
	public UserResponseDto updateUsername(String username, UserRequestDto userRequestDto) {
		// Validate given username
		User userToUpdate = validateUsername(username).get();
		
		// Check for credential match
		User newUserInfo = userMapper.dtoToEntity(userRequestDto);
		if (!userToUpdate.getCredentials().equals(newUserInfo.getCredentials())) {
			throw new NotAuthorizedException("The provided credentials are invalid. Please try again.");
		}
		
		// Update and return username
		userToUpdate.setProfile(newUserInfo.getProfile());
		return userMapper.entityToDto(userRepository.saveAndFlush(userToUpdate));
	}

	public List<TweetResponseDto>  getUserTweets(String username)
	{
		List<User> users = this.userRepository.findAll();
		users= users.stream().filter(user-> user.getCredentials().getUsername().equals(username)).collect(Collectors.toList());
		if(users.isEmpty())
			throw new NotFoundException("User not found");
		User user = users.get(0);

		return tweetMapper.entitestoDtos(user.getTweets());

	}



    //Add JPA repo
    //Add methods (CRUD for REST) for user service
}
