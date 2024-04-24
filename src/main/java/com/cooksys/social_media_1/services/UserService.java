package com.cooksys.social_media_1.services;

import java.util.List;

import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.dtos.UserResponseDto;

public interface UserService {

	List<TweetResponseDto> getUserFeed(String username);

	List<UserResponseDto> getUserFollowers(String username);
}
