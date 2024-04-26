package com.cooksys.social_media_1.services.Impl;


import com.cooksys.social_media_1.dtos.*;
import com.cooksys.social_media_1.entities.Credentials;
import com.cooksys.social_media_1.entities.Hashtag;
import com.cooksys.social_media_1.entities.Tweet;
import com.cooksys.social_media_1.entities.User;
import com.cooksys.social_media_1.exceptions.BadRequestException;
import com.cooksys.social_media_1.exceptions.NotAuthorizedException;
import com.cooksys.social_media_1.exceptions.NotFoundException;
import com.cooksys.social_media_1.mappers.CredentialsMapper;
import com.cooksys.social_media_1.mappers.HashtagMapper;
import com.cooksys.social_media_1.mappers.TweetMapper;
import com.cooksys.social_media_1.mappers.UserMapper;
import com.cooksys.social_media_1.repositories.HashtagRepository;
import com.cooksys.social_media_1.repositories.TweetRepository;
import com.cooksys.social_media_1.repositories.UserRepository;
import com.cooksys.social_media_1.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
	// Helper method to validate the given username
	private Optional<Tweet> validateTweetId(Long id) {
		Optional<Tweet> tweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (tweet.isEmpty()) {
            throw new NotFoundException("A tweet with the given ID was not found. Please try again.");
        }

        return tweet;
    }


    private final UserRepository userRepository;
    public TweetResponseDto getTweet(int id)
    {
        if (!tweetRepository.existsById((long) id) || tweetRepository.findById((long)id).get().isDeleted())
        {
            throw new NotFoundException("Tweet not found");
        }
        return tweetMapper.entityToDto(tweetRepository.getById((long) id));
    }

    public TweetResponseDto deleteTweet(int id, CredentialsRequestDto credentialsRequestDto)
    {

        if (!tweetRepository.existsById((long) id))
        {
            throw new NotFoundException("Tweet not found");
        }

        if (!((tweetRepository.findById((long) id).get().getAuthor().getCredentials().getUsername()).equals(credentialsRequestDto.getUsername())) || !((tweetRepository.findById((long) id).get().getAuthor().getCredentials().getPassword()).equals(credentialsRequestDto.getPassword())))
        {
            throw new NotFoundException("Unauthorized credential provided");
        }
        Tweet tweet = tweetRepository.findById((long) id).get();
        tweet.setDeleted(true);
        tweetRepository.save(tweet);
        return tweetMapper.entityToDto(tweet);

    }

    public void postTweetLike(int id, CredentialsRequestDto credentialsRequestDto)
    {
        if (!tweetRepository.existsById((long) id) || tweetRepository.findById((long)id).get().isDeleted())
        {
            throw new NotFoundException("Tweet not found");
        }

        if (!((tweetRepository.findById((long) id).get().getAuthor().getCredentials().getUsername()).equals(credentialsRequestDto.getUsername())) || !((tweetRepository.findById((long) id).get().getAuthor().getCredentials().getPassword()).equals(credentialsRequestDto.getPassword())))
        {
            throw new NotFoundException("Unauthorized credential provided");
        }

        User user = userRepository.findAll().stream().filter(users->{return users.getCredentials().getUsername().equals(credentialsRequestDto.getUsername());}).collect(Collectors.toList()).get(0);
        user.getLikedTweets().add(tweetRepository.findById((long)id).get());
        userRepository.save(user);

    }

    public ContextDto getTweetContext(int id)
    {
        List<Tweet> alltweets = tweetRepository.findAll();
        Tweet contexttweet = tweetRepository.findById((long)id).get();
        Comparator<Tweet> tweetComparator = new Comparator<Tweet>() {
            @Override
            public int compare(Tweet o1, Tweet o2) {
                return o1.getPosted().compareTo(o2.getPosted());

        }
    };
        Collections.sort(alltweets,tweetComparator);
        int contextindex = alltweets.indexOf(contexttweet);
        List<Tweet> before = alltweets.subList(0,contextindex);
        List<Tweet> after = alltweets.subList(contextindex+1, alltweets.size()-1);
        ContextDto contextDto = new ContextDto();
        contextDto.setBefore(tweetMapper.entitestoDtos(before));
        contextDto.setAfter(tweetMapper.entitestoDtos(after));
        contextDto.setTarget(tweetMapper.entityToDto(contexttweet));
        return contextDto;
    }



	private final HashtagRepository hashtagRepository;
	private final CredentialsMapper credentialsMapper;
	private final UserMapper userMapper;
	private final HashtagMapper hashtagMapper;
	
	// Helper method to validate the credentials passed in
	private Optional<User> validateCredentials(CredentialsRequestDto credentialsRequestDto) {
		Credentials credentials = credentialsMapper.dtoToEntity(credentialsRequestDto);
		Optional<User> user = userRepository.findByCredentialsUsernameAndCredentialsPasswordAndDeletedFalse(credentials.getUsername(), credentials.getPassword());
		
		if(user.isEmpty())
			throw new NotAuthorizedException("The given credentials are invalid. Please try again.");
		
		return user;
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
  
	private List<User> checkForActiveUsers(List<User> users) {
		List<User> activeUsers = new ArrayList<>();
		for (User u : users) {
			if (!u.isDeleted())
				activeUsers.add(u);
		}
		
		return activeUsers;
	}

	@Override
	public List<TweetResponseDto> getTweets() {
		return tweetMapper.entitiesToDTOs(tweetRepository.findByDeletedFalseOrderByPostedDesc());
	}


	@Override
	public List<HashtagResponseDto> getTagsById(Long id) {
		return hashtagMapper.entitiesToDtos(validateTweetId(id).get().getHashtags());
	}

	@Override
	public List<UserResponseDto> getUsersByLikes(Long id) {
		return userMapper.entitiesToDtos(checkForActiveUsers(validateTweetId(id).get().getLikedByUsers()));
	}


	@Override
	public TweetResponseDto repostTweet(Long id, CredentialsRequestDto credentialsRequestDto) {
		User user = validateCredentials(credentialsRequestDto).get();
		Tweet originalTweet = validateTweetId(id).get();
		Tweet repostTweet = new Tweet();
		repostTweet.setAuthor(user);
		repostTweet.setRepostOf(originalTweet);
		repostTweet = tweetRepository.saveAndFlush(repostTweet);
		originalTweet.getReposts().add(repostTweet);
		tweetRepository.saveAndFlush(originalTweet);
		return tweetMapper.entityToDto(repostTweet);
	}
}

