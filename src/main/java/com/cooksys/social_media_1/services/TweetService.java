package com.cooksys.social_media_1.services;

import com.cooksys.social_media_1.dtos.ContextDto;
import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;

public interface TweetService {

    TweetResponseDto getTweet(int id);
    TweetResponseDto deleteTweet(int id, CredentialsRequestDto credentialsRequestDto);
    void postTweetLike(int id, CredentialsRequestDto credentialsRequestDto);
    ContextDto getTweetContext(int id);
}
