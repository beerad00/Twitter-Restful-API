package com.cooksys.social_media_1.services.Impl;

import com.cooksys.social_media_1.dtos.HashtagResponseDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.entities.Hashtag;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.HashtagMapper;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.repositories.HashtagRepository;
import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.services.HashtagService;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
    private final TweetMapper tweetMapper;

    public List<HashtagResponseDto> retrieveHashtags()
    {
        return hashtagMapper.entitiesToDtos(this.hashtagRepository.findAll());
    }

    public List<TweetResponseDto> retriveLabeledHashtag(String label)
    {
        List<Hashtag> hashtags = this.hashtagRepository.findAll();
        hashtags = hashtags.stream().filter(hashtag->{return hashtag.getLabel().equals(label);}).collect(Collectors.toList());
        if(hashtags.isEmpty())
            throw new NotFoundException("Hashtag not found");
        List<Tweet>  tweets= hashtags.get(0).getTweets().stream().filter(tweet->{return !tweet.isDeleted();}).collect(Collectors.toList());
        Comparator<Tweet> tweetsorter = new Comparator<Tweet>() {
            @Override
            public int compare(Tweet o1, Tweet o2) {
                return o2.getPosted().compareTo(o1.getPosted());
            }
        };
        Collections.sort(tweets, tweetsorter);
        return this.tweetMapper.entitestoDtos(tweets);
    }
}
