package com.example.backend.reply.mapper;

import com.example.backend.reply.dto.ReplyTweetResponse;
import com.example.backend.reply.model.ReplyTweet;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReplyTweetMapper {

  List<ReplyTweetResponse> fromReplyTweetListToReplyTweetResponseList(List<ReplyTweet> entity);
}
