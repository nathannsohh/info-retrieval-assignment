package com.example.backend.tweets.mapper;

import com.example.backend.tweets.dto.TweetResponse;
import com.example.backend.tweets.model.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TweetMapper {
    List<TweetResponse> fromTweetsToTweetResponseList(List<Tweet> entity);
}
