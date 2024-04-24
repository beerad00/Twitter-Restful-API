package com.cooksys.social_media_1.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	@Override
	public List<TweetResponseDto> getUserFeed() {
		// TODO Auto-generated method stub
		return null;
	}

    //Add JPA repo
    //Add methods (CRUD for REST) for user service
}
