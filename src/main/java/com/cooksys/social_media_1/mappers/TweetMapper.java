package com.cooksys.social_media_1.mappers;

import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {
    List<TweetResponseDto> entitestoDtos(List<Tweet> tweets);

}
