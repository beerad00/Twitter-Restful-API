package com.cooksys.social_media_1.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HashtagResponseDto {

	private String label;
	
	Timestamp firstUsed;
	
	Timestamp lastUsed;
}
