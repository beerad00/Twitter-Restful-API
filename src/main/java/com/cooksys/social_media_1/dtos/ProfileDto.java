package com.cooksys.social_media_1.dtos;

import org.antlr.v4.runtime.misc.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProfileDto {

	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phone;
	
}
