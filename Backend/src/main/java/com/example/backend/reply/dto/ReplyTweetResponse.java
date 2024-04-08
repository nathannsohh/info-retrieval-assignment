package com.example.backend.reply.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReplyTweetResponse {

  private Date createdAt;
  private String fullName;
  private String userName;
  private String profileImage;
  private String fullText;
  private String replyTo;
  private Long quoteCount;
  private Long retweetCount;
  private Long replyCount;
  private Long likeCount;
  private Long viewCount;
  private Integer sentiment;
  private Integer sarcasm;
  private String sentimentDetail;
}
