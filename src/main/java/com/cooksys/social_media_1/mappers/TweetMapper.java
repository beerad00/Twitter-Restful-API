package com.cooksys.social_media_1.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.entities.Tweet;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

	List<TweetResponseDto> entitiesToDTOs(List<Tweet> tweets);
}
