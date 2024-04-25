package com.cooksys.social_media_1.services;

import java.util.List;

import com.cooksys.social_media_1.dtos.TweetResponseDto;

public interface TweetService {

	List<TweetResponseDto> getTweetReplies(Long id);

	List<TweetResponseDto> getTweetReposts(Long id);
}
