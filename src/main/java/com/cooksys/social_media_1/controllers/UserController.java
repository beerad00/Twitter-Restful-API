package com.cooksys.social_media_1.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	
	@GetMapping("/{username}/feed")
	public List<TweetResponseDto> getUserFeed() {
		return userService.getUserFeed();
	}
}
