package com.example.backend.news.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewsTweetResponse {

  private String id;
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
  private Float score; // relevance score for NewsTweet search
}
