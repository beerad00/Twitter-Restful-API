package com.cooksys.social_media_1.dtos;

import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {
	
	private CredentialsResponseDto credentials;
	
	private ProfileRequestDto profile;
	
	private Timestamp joined;

}
