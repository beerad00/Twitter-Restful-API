package com.cooksys.social_media_1.services.Impl;

import com.cooksys.social_media_1.mappers.UserMapper;
import com.cooksys.social_media_1.repositories.UserRepository;
import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
    private final UserRepository userRepository;
    public boolean validateUsernameExistence(String username)
    {
        return userRepository.findAll().stream().anyMatch(user->{return user.getCredentials().getUsername().equals(username);});
    }

    public boolean validateUsernameAvailable(String username)
    {
        if(this.validateUsernameExistence(username))
            return false;
        return true;
    }

}
