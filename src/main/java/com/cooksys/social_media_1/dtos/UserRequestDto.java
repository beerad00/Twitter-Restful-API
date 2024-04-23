package com.cooksys.social_media_1.dtos;

import com.cooksys.social_media_1.entities.Credentials;
import com.cooksys.social_media_1.entities.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequestDto {
    
    private CredentialsRequestDto credentials;

    private ProfileRequestDto profile;
}

