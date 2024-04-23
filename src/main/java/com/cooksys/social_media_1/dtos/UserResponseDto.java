package com.cooksys.social_media_1.dtos;

import java.sql.Timestamp;

import org.springframework.context.annotation.Profile;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {
	
	private String username;
	
	private ProfileRequestDto profile;
	
	private Timestamp joined;

}
