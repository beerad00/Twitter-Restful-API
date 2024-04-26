package com.cooksys.social_media_1.services.Impl;

import com.cooksys.social_media_1.dtos.ContextDto;
import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.entities.User;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.repositories.TweetRepository;
import com.cooksys.social_media_1.repositories.UserRepository;
import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.services.TweetService;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final UserRepository userRepository;
    public TweetResponseDto getTweet(int id)
    {
        if (!tweetRepository.existsById((long) id) || tweetRepository.findById((long)id).get().isDeleted())
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

        if (!((tweetRepository.findById((long) id).get().getAuthor().getCredentials().getUsername()).equals(credentialsRequestDto.getUsername())) || !((tweetRepository.findById((long) id).get().getAuthor().getCredentials().getPassword()).equals(credentialsRequestDto.getPassword())))
        {
            throw new NotFoundException("Unauthorized credential provided");
        }
        Tweet tweet = tweetRepository.findById((long) id).get();
        tweet.setDeleted(true);
        tweetRepository.save(tweet);
        return tweetMapper.entityToDto(tweet);

    }

    public void postTweetLike(int id, CredentialsRequestDto credentialsRequestDto)
    {
        if (!tweetRepository.existsById((long) id) || tweetRepository.findById((long)id).get().isDeleted())
        {
            throw new NotFoundException("Tweet not found");
        }

        if (!((tweetRepository.findById((long) id).get().getAuthor().getCredentials().getUsername()).equals(credentialsRequestDto.getUsername())) || !((tweetRepository.findById((long) id).get().getAuthor().getCredentials().getPassword()).equals(credentialsRequestDto.getPassword())))
        {
            throw new NotFoundException("Unauthorized credential provided");
        }

        User user = userRepository.findAll().stream().filter(users->{return users.getCredentials().getUsername().equals(credentialsRequestDto.getUsername());}).collect(Collectors.toList()).get(0);
        user.getLikedTweets().add(tweetRepository.findById((long)id).get());
        userRepository.save(user);

    }

    public ContextDto getTweetContext(int id)
    {
        List<Tweet> alltweets = tweetRepository.findAll();
        Tweet contexttweet = tweetRepository.findById((long)id).get();
        Comparator<Tweet> tweetComparator = new Comparator<Tweet>() {
            @Override
            public int compare(Tweet o1, Tweet o2) {
                return o1.getPosted().compareTo(o2.getPosted());

        }
    };
        Collections.sort(alltweets,tweetComparator);
        int contextindex = alltweets.indexOf(contexttweet);
        List<Tweet> before = alltweets.subList(0,contextindex);
        List<Tweet> after = alltweets.subList(contextindex+1, alltweets.size()-1);
        ContextDto contextDto = new ContextDto();
        contextDto.setBefore(tweetMapper.entitestoDtos(before));
        contextDto.setAfter(tweetMapper.entitestoDtos(after));
        contextDto.setTarget(tweetMapper.entityToDto(contexttweet));
        return contextDto;
    }
}

