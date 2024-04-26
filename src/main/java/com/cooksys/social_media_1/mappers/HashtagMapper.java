package com.cooksys.social_media_1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.social_media_1.dtos.HashtagResponseDto;
import com.cooksys.social_media_1.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {
	
	List<HashtagResponseDto> entitiesToDtos(List<Hashtag> hashtags);
}
