package com.cooksys.social_media_1.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.entities.User;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.repositories.UserRepository;
import com.cooksys.social_media_1.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final TweetMapper tweetMapper;
	
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

    //Add JPA repo
    //Add methods (CRUD for REST) for user service
}
