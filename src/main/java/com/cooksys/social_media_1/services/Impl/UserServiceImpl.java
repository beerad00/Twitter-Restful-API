package com.cooksys.social_media_1.services.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.entities.User;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.repositories.UserRepository;
import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final TweetMapper tweetMapper;
	@Override
	public List<TweetResponseDto> getUserFeed() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TweetResponseDto>  getUserTweets(String username)
	{
		List<User> users = this.userRepository.findAll();
		users= users.stream().filter(user-> user.getCredentials().getUsername().equals(username)).collect(Collectors.toList());
		if(users.isEmpty())
			throw new NotFoundException("User not found");
		User user = users.get(0);

		return tweetMapper.entitestoDtos(user.getTweets());

	}



    //Add JPA repo
    //Add methods (CRUD for REST) for user service
}
