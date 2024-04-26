package com.cooksys.social_media_1.controllers;


import com.cooksys.social_media_1.dtos.ContextDto;
import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {


    private final TweetService tweetService;
    @GetMapping("/{id}")
   TweetResponseDto getTweet(@PathVariable("id") int id)
    {
        return tweetService.getTweet(id);
    }
    @DeleteMapping("/{id}")
    TweetResponseDto deleteTweet(@PathVariable("id") int id, @RequestBody CredentialsRequestDto credentialsRequestDto)
    {
        return tweetService.deleteTweet(id,credentialsRequestDto);
    }
    @PostMapping("/{id}/like")
    void postTweetLike(@PathVariable("id") int id, @RequestBody CredentialsRequestDto credentialsRequestDto)
    {
        tweetService.postTweetLike(id, credentialsRequestDto);
    }
    @GetMapping("/{id}/context")
    ContextDto getTweetContext(@PathVariable("id") int id)
    {
        return tweetService.getTweetContext(id);
    }

	
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getTweetReplies(@PathVariable Long id) {
		return tweetService.getTweetReplies(id);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getTweetReposts(@PathVariable Long id) {
		return tweetService.getTweetReposts(id);
	}

}
