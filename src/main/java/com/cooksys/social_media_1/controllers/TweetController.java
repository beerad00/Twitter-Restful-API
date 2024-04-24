package com.cooksys.social_media_1.controllers;

import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.services.TweetService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

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

}
