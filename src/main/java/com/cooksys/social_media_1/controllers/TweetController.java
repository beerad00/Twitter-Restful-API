package com.cooksys.social_media_1.controllers;

import com.cooksys.social_media_1.dtos.TweetResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    @GetMapping("/{id}")
    List<TweetResponseDto> getTweet(@PathVariable("id") int id)
    {
        return null;
    }

}
