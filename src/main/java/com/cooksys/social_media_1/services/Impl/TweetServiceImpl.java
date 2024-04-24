package com.cooksys.social_media_1.services.Impl;

import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.repositories.TweetRepository;
import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.services.TweetService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    public TweetResponseDto getTweet(int id)
    {
        if (!tweetRepository.existsById((long) id))
        {
            throw new NotFoundException("Tweet not found");
        }
        return tweetMapper.entityToDto(tweetRepository.getById((long) id));
    }

    public TweetResponseDto deleteTweet(int id, CredentialsRequestDto credentialsRequestDto)
    {
        if (!tweetRepository.existsById((long) id))
        {
            throw new NotFoundException("Tweet not found");
        }

        if (!((tweetRepository.getById((long) id).getAuthor().getCredentials().getUsername()).equals(credentialsRequestDto.getUsername())))
        {
            throw new NotFoundException("Unauthorized credential provided");
        }
        Tweet tweet = tweetRepository.getReferenceById((long) id);
        tweet.setDeleted(true);
        tweetRepository.save(tweet);
        return tweetMapper.entityToDto(tweet);

    }

    //Add JPA repo
    //Add methods (CRUD for REST) for user service
}
