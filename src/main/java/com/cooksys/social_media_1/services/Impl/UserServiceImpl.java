package com.cooksys.social_media_1.services.Impl;

import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.dtos.UserRequestDto;
import com.cooksys.social_media_1.dtos.UserResponseDto;
import com.cooksys.social_media_1.entities.Credentials;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.entities.User;
import com.cooksys.social_media_1.exceptions.BadRequestException;
import com.cooksys.social_media_1.exceptions.NotAuthorizedException;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.CredentialsMapper;
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
	private final CredentialsMapper credentialsMapper;
	
	// Helper method to validate and return the User for the given username
	private Optional<User> getUser(String username) {
		Optional<User> user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		
		if(user.isEmpty()) {
			throw new NotFoundException("The given username was not found. Please try again.");
		}
		
		return user;
	}
	

	@Override
	public List<TweetResponseDto> getUserFeed(String username) {
		// Validate and get user entity
		User user = getUser(username).get();
		
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
		List<User> followers = getUser(username).get().getFollowers();
		
		return userMapper.entitiesToDtos(followers);
	}

	@Override
	public List<UserResponseDto> getFollowedUsers(String username) {
		// Validate and get followed users
		List<User> followedUsers = getUser(username).get().getFollowing();
		
		
		return userMapper.entitiesToDtos(followedUsers);
	}

	@Override
	public void followUser(String username, CredentialsRequestDto credentialsRequestDto) {
		// Validate and get user to follow
		User userToFollow = getUser(username).get();
		
		// Get User by provided Credentials username. There will only be 1
		Credentials providedCredentials =  credentialsMapper.dtoToEntity(credentialsRequestDto);
		if (providedCredentials.getUsername() == null || providedCredentials.getPassword() == null) {
			throw new NotAuthorizedException("Please provide both a Username and a Password within the body of the request.");
		}
		User providedUser = getUser(providedCredentials.getUsername()).get();
		
		// Check that user for matching passwords
		if (!providedUser.getCredentials().getPassword().equals(providedCredentials.getPassword())) {
			throw new NotAuthorizedException("The provided password does not match our records. Please try again.");
		}
		
		// Check if confirmed given credentials user already follows "username"
		if (providedUser.getFollowing().contains(userToFollow)) {
			throw new BadRequestException("You already follow this user!");
		}
		
		// If all above pass, update following and return void
		List<User> userList = providedUser.getFollowing();
		userList.add(userToFollow);
		providedUser.setFollowing(userList);
		userRepository.saveAndFlush(providedUser);
		return;
	}

	@Override
	public UserResponseDto updateUsername(String username, UserRequestDto userRequestDto) {
		// Validate given username
		User userToUpdate = getUser(username).get();
		
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

		return tweetMapper.entitestoDtos(users.get(0).getTweets().stream().filter(tweet->{return !tweet.isDeleted();}).collect(Collectors.toList()));

	}

	public UserResponseDto postUser(UserRequestDto userRequestDto)
	{
		if(userRepository.findAll().stream().anyMatch(user->{return user.getCredentials().getUsername().equals(userRequestDto.getCredentials().getUsername());}) && userRepository.findAll().stream().anyMatch(user->{return user.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword());}))
		{
			User olduser = userRepository.findAll().stream().filter(user->{return user.getCredentials().getUsername().equals(userRequestDto.getCredentials().getUsername());}).collect(Collectors.toList()).get(0);
			olduser.setDeleted(false);
			userRepository.save(olduser);
			return userMapper.entityToDto(olduser);
		}

		if(userRepository.findAll().stream().anyMatch(user->{return user.getCredentials().getUsername().equals(userRequestDto.getCredentials().getUsername());}) && !userRepository.findAll().stream().anyMatch(user->{return user.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword());}))
		{
			throw new NotFoundException("User already exist");
		}

		if(userRequestDto.getCredentials()==null||userRequestDto.getProfile()==null||userRequestDto.getCredentials().getUsername()==null||userRequestDto.getCredentials().getUsername().isEmpty()||userRequestDto.getCredentials().getPassword()==null||userRequestDto.getCredentials().getPassword().isEmpty()||userRequestDto.getProfile().getEmail()==null||userRequestDto.getProfile().getEmail().isEmpty())
		{
			throw new NotFoundException("Required field are not provided");
		}


		User newuser = userMapper.dtoToEntity(userRequestDto);
		userRepository.save(newuser);
		return userMapper.entityToDto(newuser);

	}





}
