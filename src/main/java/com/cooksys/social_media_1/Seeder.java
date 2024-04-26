package com.cooksys.social_media_1;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cooksys.social_media_1.entities.Credentials;
import com.cooksys.social_media_1.entities.Hashtag;
import com.cooksys.social_media_1.entities.Profile;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.entities.User;
import com.cooksys.social_media_1.repositories.HashtagRepository;
import com.cooksys.social_media_1.repositories.TweetRepository;
import com.cooksys.social_media_1.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

	//private final HashtagRepository hashtagRepository;
	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;

	/**
	 * This method seeds the database with 3 users. First two users have 4 tweets and
	 * one of those tweets has 1 response.
	 */
	@Override
	public void run(String... args) throws Exception {
		
		User user1 = new User();
		Credentials user1Creds = new Credentials();
		user1Creds.setUsername("user1");
		user1Creds.setPassword("pw1");
		user1.setCredentials(user1Creds);
		Profile user1Profile = new Profile();
		user1Profile.setEmail("user1@email.com");
		user1Profile.setFirstName("Frank");
		user1Profile.setLastName("Peters");
		user1Profile.setPhone("9999999999");
		user1.setProfile(user1Profile);

		User user2 = new User();
		Credentials user2Creds = new Credentials();
		user2Creds.setUsername("user2");
		user2Creds.setPassword("pw2");
		user2.setCredentials(user2Creds);
		Profile user2Profile = new Profile();
		user2Profile.setEmail("user2@email.com");
		user2Profile.setFirstName("Tim");
		user2Profile.setLastName("Smith");
		user2Profile.setPhone("1111111111");
		user2.setProfile(user2Profile);
		
		User user3 = new User();
		Credentials user3Creds = new Credentials();
		user3Creds.setUsername("user3");
		user3Creds.setPassword("pw3");
		user3.setCredentials(user3Creds);
		Profile user3Profile = new Profile();
		user3Profile.setEmail("user3@email.com");
		user3Profile.setFirstName("Mister");
		user3Profile.setLastName("Three");
		user3Profile.setPhone("3333333333");
		user3.setProfile(user3Profile);

		// Set user followings
		user1.setFollowing(Arrays.asList(new User[] { user2 }));
		user1.setFollowers(Arrays.asList(new User[] { user2 }));
		user2.setFollowing(Arrays.asList(new User[] { user1 }));
		user2.setFollowers(Arrays.asList(new User[] { user1 }));
		
		userRepository.saveAllAndFlush(Arrays.asList(new User[] { user1, user2, user3 }));

		// User 1 Tweets
		Tweet tweet1 = new Tweet();
		tweet1.setAuthor(user1);
		tweet1.setContent("User 1 first tweet!");

		Tweet tweet2 = new Tweet();
		tweet2.setAuthor(user1);
		tweet2.setContent("User 1 second tweet!");

		Tweet tweet3 = new Tweet();
		tweet3.setAuthor(user1);
		tweet3.setContent("User 1 third tweet!");
		
		Tweet tweet4 = new Tweet();
		tweet4.setAuthor(user1);
		tweet4.setContent("User 1 fourth tweet!");

		// User 2 Tweets
		Tweet tweet5 = new Tweet();
		tweet5.setAuthor(user2);
		tweet5.setContent("User 2 first tweet!");

		Tweet tweet6 = new Tweet();
		tweet6.setAuthor(user2);
		tweet6.setContent("User 2 second tweet!");

		Tweet tweet7 = new Tweet();
		tweet7.setAuthor(user2);
		tweet7.setContent("User 2 third tweet!");
		
		Tweet tweet8 = new Tweet();
		tweet8.setAuthor(user2);
		tweet8.setContent("User 2 fourth tweet!");

		// User 1 Response
		Tweet response1 = new Tweet();
		response1.setAuthor(user1);
		response1.setInReplyTo(tweet5);
		response1.setContent("I agree! Nice first tweet!");
		
		// User 2 Response
		Tweet response2 = new Tweet();
		response2.setAuthor(user2);
		response2.setInReplyTo(tweet1);
		response2.setContent("I don't agree! Terrible first tweet!");

		tweetRepository.saveAllAndFlush(Arrays.asList(new Tweet[] { tweet1, tweet2, tweet3, tweet4, tweet5,
				tweet6, tweet7, tweet8, response1, response2 }));
		
		//Hashtags
		Hashtag hashtag1 = new Hashtag();
		hashtag1.setLabel("first");
		hashtag1.setTweets(Arrays.asList(new Tweet[] { tweet1, tweet5 }));
		hashtag1.setFirstUsed(tweet1.getPosted());
		hashtag1.setLastUsed(tweet5.getPosted());
		
		Hashtag hashtag2 = new Hashtag();
		hashtag2.setLabel("second");
		hashtag2.setTweets(Arrays.asList(new Tweet[] { tweet2, tweet6 }));
		hashtag2.setFirstUsed(tweet2.getPosted());
		hashtag2.setLastUsed(tweet6.getPosted());
		
		hashtagRepository.saveAllAndFlush(Arrays.asList(new Hashtag[] { hashtag1, hashtag2 }));
		
		tweet1.setHashtags(Arrays.asList(new Hashtag[] { hashtag1 }));
		tweet5.setHashtags(Arrays.asList(new Hashtag[] { hashtag1 }));
		tweet2.setHashtags(Arrays.asList(new Hashtag[] { hashtag2 }));
		tweet6.setHashtags(Arrays.asList(new Hashtag[] { hashtag2 }));
		
		tweetRepository.saveAllAndFlush(Arrays.asList(new Tweet[] { tweet1, tweet2, tweet5, tweet6 }));
		
		//Tweet likes
		user1.setLikedTweets(Arrays.asList(new Tweet[] { tweet1, tweet3, tweet6, tweet8, response1 }));
		user2.setLikedTweets(Arrays.asList(new Tweet[] { tweet1, tweet3, tweet5, tweet6 }));
		user3.setLikedTweets(Arrays.asList(new Tweet[] { tweet3, tweet4, tweet7, tweet8, response2 }));
		tweet1.setLikedByUsers(Arrays.asList(new User[] { user1, user2 }));
		tweet3.setLikedByUsers(Arrays.asList(new User[] { user1, user2, user3 }));
		tweet4.setLikedByUsers(Arrays.asList(new User[] { user3 }));
		tweet5.setLikedByUsers(Arrays.asList(new User[] { user2 }));
		tweet6.setLikedByUsers(Arrays.asList(new User[] { user1, user2 }));
		tweet7.setLikedByUsers(Arrays.asList(new User[] { user3 }));
		tweet8.setLikedByUsers(Arrays.asList(new User[] { user1, user3 }));
		response1.setLikedByUsers(Arrays.asList(new User[] { user1 }));
		response2.setLikedByUsers(Arrays.asList(new User[] { user3 }));
		
		userRepository.saveAllAndFlush(Arrays.asList(new User[] { user1, user2, user3 }));
		tweetRepository.saveAllAndFlush(Arrays.asList(new Tweet[] { tweet1, tweet2, tweet3, tweet4, tweet5,
				tweet6, tweet7, tweet8, response1, response2 }));
		
		
		//Tweet mentions
		user1.setTweetsMentionedIn(Arrays.asList(new Tweet[] { tweet1, tweet3, tweet6, tweet8, response1 }));
		user2.setTweetsMentionedIn(Arrays.asList(new Tweet[] { tweet1, tweet3, tweet5, tweet6 }));
		user3.setTweetsMentionedIn(Arrays.asList(new Tweet[] { tweet3, tweet4, tweet7, tweet8, response2 }));
		tweet1.setMentionedUsers(Arrays.asList(new User[] { user1, user2 }));
		tweet3.setMentionedUsers(Arrays.asList(new User[] { user1, user2, user3 }));
		tweet4.setMentionedUsers(Arrays.asList(new User[] { user3 }));
		tweet5.setMentionedUsers(Arrays.asList(new User[] { user2 }));
		tweet6.setMentionedUsers(Arrays.asList(new User[] { user1, user2 }));
		tweet7.setMentionedUsers(Arrays.asList(new User[] { user3 }));
		tweet8.setMentionedUsers(Arrays.asList(new User[] { user1, user3 }));
		response1.setMentionedUsers(Arrays.asList(new User[] { user1 }));
		response2.setMentionedUsers(Arrays.asList(new User[] { user3 }));
		
		userRepository.saveAllAndFlush(Arrays.asList(new User[] { user1, user2, user3 }));
		tweetRepository.saveAllAndFlush(Arrays.asList(new Tweet[] { tweet1, tweet2, tweet3, tweet4, tweet5,
				tweet6, tweet7, tweet8, response1, response2 }));
	}
}
