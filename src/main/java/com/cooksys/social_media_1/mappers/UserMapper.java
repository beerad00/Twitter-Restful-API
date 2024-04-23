package com.cooksys.social_media_1.mappers;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class, TweetMapper.class })
public interface UserMapper {

}
