package com.cooksys.social_media_1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.social_media_1.dtos.UserResponseDto;
import com.cooksys.social_media_1.entities.User;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

	List<UserResponseDto> entitiesToDTOs(List<User> users);
}
