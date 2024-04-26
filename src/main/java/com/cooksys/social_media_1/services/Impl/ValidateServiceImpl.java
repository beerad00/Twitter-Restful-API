package com.cooksys.social_media_1.services.Impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.entities.Hashtag;
import com.cooksys.social_media_1.repositories.HashtagRepository;
import com.cooksys.social_media_1.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;

	@Override
	public boolean validateLabelExistence(String label) {
		Optional<Hashtag> hashtag = hashtagRepository.findByLabel(label);
		
		if (hashtag.isPresent())
			return true;
		return false;
	}

}
