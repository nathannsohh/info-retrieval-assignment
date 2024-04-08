package com.example.backend.reply.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SentimentAnalysisResponse {

  private long totalReplyTweets;
  private double positive;
  private double negative;
  private double neutral;
  private List<ReplyTweetResponse> replyTweets;
}
