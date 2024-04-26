package com.cooksys.social_media_1.services.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.HashtagResponseDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.dtos.UserResponseDto;
import com.cooksys.social_media_1.entities.Credentials;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.entities.User;
import com.cooksys.social_media_1.exceptions.NotAuthorizedException;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.CredentialsMapper;
import com.cooksys.social_media_1.mappers.HashtagMapper;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.mappers.UserMapper;
import com.cooksys.social_media_1.repositories.TweetRepository;
import com.cooksys.social_media_1.repositories.UserRepository;
import com.cooksys.social_media_1.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final HashtagMapper hashtagMapper;
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final CredentialsMapper credentialsMapper;
	
	// Helper method to validate the credentials passed in
	private Optional<User> validateCredentials(CredentialsRequestDto credentialsRequestDto) {
		Credentials credentials = credentialsMapper.dtoToEntity(credentialsRequestDto);
		Optional<User> user = userRepository.findByCredentialsUsernameAndCredentialsPasswordAndDeletedFalse(credentials.getUsername(), credentials.getPassword());
		
		if(user.isEmpty())
			throw new NotAuthorizedException("The given credentials are invalid. Please try again.");
		
		return user;
	}
	
	// Helper method to validate the given tweet id exists
	private Optional<Tweet> validateTweetId(Long id) {
		Optional<Tweet> tweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (tweet.isEmpty()) {
			throw new NotFoundException("A tweet with the given ID was not found. Please try again.");
		}

		return tweet;
	}

	@Override
	public List<TweetResponseDto> getTweetReplies(Long id) {
		// Validate provided Tweet ID
		Tweet tweet = validateTweetId(id).get();
		
		return tweetMapper.entitiesToDTOs(tweet.getReplies());
	}

	@Override
	public List<TweetResponseDto> getTweetReposts(Long id) {
		// Validate provided Tweet ID
		Tweet tweet = validateTweetId(id).get();
		
		return tweetMapper.entitiesToDTOs(tweet.getReposts());
	}

	private List<User> checkForActiveUsers(List<User> users) {
		List<User> activeUsers = new ArrayList<>();
		for (User u : users) {
			if (!u.isDeleted())
				activeUsers.add(u);
		}
		
		return activeUsers;
	}

	@Override
	public List<TweetResponseDto> getTweets() {
		return tweetMapper.entitiesToDTOs(tweetRepository.findByDeletedFalseOrderByPostedDesc());
	}


	@Override
	public List<HashtagResponseDto> getTagsById(Long id) {
		return hashtagMapper.entitiesToDtos(validateTweetId(id).get().getHashtags());
	}

	@Override
	public List<UserResponseDto> getUsersByLikes(Long id) {
		return userMapper.entitiesToDtos(checkForActiveUsers(validateTweetId(id).get().getLikedByUsers()));
	}


	@Override
	public TweetResponseDto repostTweet(Long id, CredentialsRequestDto credentialsRequestDto) {
		User user = validateCredentials(credentialsRequestDto).get();
		Tweet originalTweet = validateTweetId(id).get();
		Tweet repostTweet = new Tweet();
		repostTweet.setAuthor(user);
		repostTweet.setRepostOf(originalTweet);
		repostTweet = tweetRepository.saveAndFlush(repostTweet);
		originalTweet.getReposts().add(repostTweet);
		tweetRepository.saveAndFlush(originalTweet);
		return tweetMapper.entityToDto(repostTweet);
	}

}
