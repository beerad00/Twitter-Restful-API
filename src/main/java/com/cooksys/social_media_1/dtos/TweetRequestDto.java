package com.cooksys.social_media_1.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {
    
    private String content;

    private CredentialsRequestDto credentials;
}
