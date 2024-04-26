package com.cooksys.social_media_1.services;

import java.util.List;

import com.cooksys.social_media_1.dtos.TweetRequestDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.dtos.UserResponseDto;

public interface TweetService {

	List<TweetResponseDto> getTweetReplies(Long id);

	List<TweetResponseDto> getTweetReposts(Long id);

	TweetResponseDto createNewTweet(TweetRequestDto tweetRequestDto);

	List<UserResponseDto> getTweetMentions(Long id);

	TweetResponseDto createNewReplyTweet(TweetRequestDto tweetRequestDto, Long id);
}
