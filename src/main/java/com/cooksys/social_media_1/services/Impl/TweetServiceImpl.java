package com.cooksys.social_media_1.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.repositories.TweetRepository;
import com.cooksys.social_media_1.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
	// Helper method to validate the given username
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

	// Add JPA repo
	// Add methods (CRUD for REST) for user service
}
