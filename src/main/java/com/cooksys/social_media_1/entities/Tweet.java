package com.cooksys.social_media_1.entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Tweet {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "author")
	private User author;
	
	private Timestamp posted;
	
	private boolean deleted = false;
	
	private String content;
	
	private Tweet inReplyTo;
	
	private Tweet inRepostOf;
	
    @ManyToMany(mappedBy = "likedTweets")
    private Set<User> likedByUsers;
    
    @ManyToMany(mappedBy = "tweetsMentionedIn")
    private Set<User> mentionedUsers;
    
    @ManyToMany
    @JoinTable(
        name = "tweet_hashtags",
        joinColumns = @JoinColumn(name = "tweet_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> hashtags;

}
