package com.cooksys.social_media_1.services;

import java.util.List;

import com.cooksys.social_media_1.dtos.TweetRequestDto;
import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.HashtagResponseDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.dtos.UserResponseDto;

public interface TweetService {

	List<TweetResponseDto> getTweetReplies(Long id);

	List<TweetResponseDto> getTweetReposts(Long id);

	TweetResponseDto createNewTweet(TweetRequestDto tweetRequestDto);

	List<UserResponseDto> getTweetMentions(Long id);

	TweetResponseDto createNewReplyTweet(TweetRequestDto tweetRequestDto, Long id);
	
	List<TweetResponseDto> getTweets();

	List<HashtagResponseDto> getTagsById(Long id);

	List<UserResponseDto> getUsersByLikes(Long id);

	TweetResponseDto repostTweet(Long id, CredentialsRequestDto credentialsRequestDto);
}
