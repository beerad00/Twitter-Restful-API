package com.cooksys.social_media_1.services;

import java.util.List;

import com.cooksys.social_media_1.dtos.TweetResponseDto;

public interface UserService {

	List<TweetResponseDto> getUserFeed();
	List<TweetResponseDto>  getUserTweets(String username);
}
