package com.cooksys.social_media_1.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

	private final TweetService tweetService;
	
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getTweetReplies(@PathVariable Long id) {
		return tweetService.getTweetReplies(id);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getTweetReposts(@PathVariable Long id) {
		return tweetService.getTweetReposts(id);
	}
}
