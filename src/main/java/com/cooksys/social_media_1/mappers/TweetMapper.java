package com.cooksys.social_media_1.mappers;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class, HashtagMapper.class })
public interface TweetMapper {

}
