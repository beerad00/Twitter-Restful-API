package com.cooksys.social_media_1.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = false, unique = true)
	private String label;
	
	@Column(nullable = false)
	@CreatedDate
	private Timestamp firstUsed;

	@Column(nullable = false)
    	@LastModifiedDate
	private Timestamp lastUsed;
	
    @ManyToMany(mappedBy = "hashtags")
    private List<Tweet> tweets;
	
}
