package com.cooksys.social_media_1.services.Impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.cooksys.social_media_1.dtos.CredentialsRequestDto;
import com.cooksys.social_media_1.dtos.TweetRequestDto;
import com.cooksys.social_media_1.dtos.TweetResponseDto;
import com.cooksys.social_media_1.dtos.UserResponseDto;
import com.cooksys.social_media_1.entities.Credentials;
import com.cooksys.social_media_1.entities.Hashtag;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.entities.User;
import com.cooksys.social_media_1.exceptions.BadRequestException;
import com.cooksys.social_media_1.exceptions.NotAuthorizedException;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.CredentialsMapper;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.mappers.UserMapper;
import com.cooksys.social_media_1.repositories.HashtagRepository;
import com.cooksys.social_media_1.repositories.TweetRepository;
import com.cooksys.social_media_1.repositories.UserRepository;
import com.cooksys.social_media_1.services.TweetService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;
	private final CredentialsMapper credentialsMapper;
	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;

	// Helper method to validate the given username
	private Optional<Tweet> validateTweetId(Long id) {
		Optional<Tweet> tweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (tweet.isEmpty()) {
			throw new NotFoundException("A tweet with the given ID was not found. Please try again.");
		}

		return tweet;
	}

	// Helper method to validate and return the User for the given username
	private Optional<User> getUser(String username) {
		Optional<User> user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);

		if (user.isEmpty()) {
			throw new NotFoundException("The given username was not found. Please try again.");
		}

		return user;
	}

	// Helper method to validate and return the User for the given
	// CredentialsRequestDto
	private User getUserFromCredentials(CredentialsRequestDto credentialsRequestDto) {
		Credentials providedCredentials = credentialsMapper.dtoToEntity(credentialsRequestDto);

		User userCreatingTweet = getUser(providedCredentials.getUsername()).get();

		// Check password
		if (!userCreatingTweet.getCredentials().equals(providedCredentials)) {
			throw new NotAuthorizedException("The provided credentials are invalid. Please try again.");
		}

		return userCreatingTweet;
	}

	// Helper method to parse Tweet content for @ mentions
	private void processTweetForMentions(Tweet newTweet) {
		String tweetContent = newTweet.getContent();
		Pattern pattern = Pattern.compile("(?<=@)\\S+");
		Matcher matcher = pattern.matcher(tweetContent);
		while (matcher.find()) {
			Optional<User> user = userRepository.findByCredentialsUsernameAndDeletedFalse(matcher.group());
			if (!user.isEmpty()) {
				User mentionedUser = user.get();
				List<Tweet> tweetsMentionedIn = mentionedUser.getTweetsMentionedIn();
				tweetsMentionedIn.add(newTweet);
				mentionedUser.setTweetsMentionedIn(tweetsMentionedIn);
				userRepository.saveAndFlush(mentionedUser);
			}
		}
	}

	// Helper method to parse Tweet content for # hashtags
	private void processTweetForHashtags(Tweet newTweet) {
		List<Hashtag> tweetHashtags = newTweet.getHashtags();
		if (tweetHashtags == null) {
			tweetHashtags = new ArrayList<>();
		}
		String tweetContent = newTweet.getContent();
		Pattern pattern = Pattern.compile("(?<=#)\\S+");
		Matcher matcher = pattern.matcher(tweetContent);
		while (matcher.find()) {
			// Check if hashtag exists and add to list. Else create new Hashtag
			Optional<Hashtag> hashtag = hashtagRepository.findByLabel(matcher.group());
			if (hashtag.isEmpty()) {
				// Create new hashtag here
				Hashtag newHashtag = new Hashtag();
				newHashtag.setLabel(matcher.group());
				newHashtag.setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
				tweetHashtags.add(hashtagRepository.saveAndFlush(newHashtag));
			} else {
				// Add hashtag to list
				tweetHashtags.add(hashtag.get());
			}
		}
		newTweet.setHashtags(tweetHashtags);
		tweetRepository.saveAndFlush(newTweet);
	}

	@Override
	public List<TweetResponseDto> getTweetReplies(Long id) {
		// Validate provided Tweet ID
		Tweet tweet = validateTweetId(id).get();

		return tweetMapper.entitiesToDTOs(tweet.getReplies());
	}

	@Override
	public List<TweetResponseDto> getTweetReposts(Long id) {
		// Validate provided Tweet ID
		Tweet tweet = validateTweetId(id).get();

		return tweetMapper.entitiesToDTOs(tweet.getReposts());
	}

	@Override
	public List<UserResponseDto> getTweetMentions(Long id) {
		// Validate given tweet ID
		Tweet tweet = validateTweetId(id).get();

		List<User> mentionedUsers = tweet.getMentionedUsers();

		return userMapper.entitiesToDtos(mentionedUsers);
	}

	@Override
	public TweetResponseDto createNewTweet(TweetRequestDto tweetRequestDto) {
		// Create Tweet and User entities and set author of tweet
		Tweet newTweet = tweetMapper.dtoToEntity(tweetRequestDto);
		User userCreatingTweet = getUserFromCredentials(tweetRequestDto.getCredentials());
		newTweet.setAuthor(userCreatingTweet);

		// Check for null content
		if (newTweet.getContent() == null) {
			throw new BadRequestException(
					"The Tweet content is required to create a new Tweet. Please provide the Tweet content.");
		}

		// Check Content for @ Mentions and update to newTweet
		processTweetForMentions(tweetRepository.saveAndFlush(newTweet));

		// Check Content for # Hashtags and update to newTweet
		processTweetForHashtags(tweetRepository.saveAndFlush(newTweet));

		// System.out.println(newTweet.toString());

		return tweetMapper.entityToDto(newTweet);
	}

	@Override
	public TweetResponseDto createNewReplyTweet(TweetRequestDto tweetRequestDto, Long originalTweetId) {
		// Validate and retrieve Tweet from provided ID
		Tweet originalTweet = validateTweetId(originalTweetId).get();
		
		// Create Tweet and User entities and set author of tweet
		Tweet newTweet = tweetMapper.dtoToEntity(tweetRequestDto);
		User userCreatingTweet = getUserFromCredentials(tweetRequestDto.getCredentials());
		newTweet.setAuthor(userCreatingTweet);

		// Check for null content
		if (newTweet.getContent() == null) {
			throw new BadRequestException(
					"The Tweet content is required to create a new Tweet. Please provide the Tweet content.");
		}
		
		// Set inReplyTo to the original Tweet
		newTweet.setInReplyTo(originalTweet);
		
		// Check Content for @ Mentions and update to newTweet
		processTweetForMentions(tweetRepository.saveAndFlush(newTweet));

		// Check Content for # Hashtags and update to newTweet
		processTweetForHashtags(tweetRepository.saveAndFlush(newTweet));
		
		return tweetMapper.entityToDto(newTweet);
	}
}
