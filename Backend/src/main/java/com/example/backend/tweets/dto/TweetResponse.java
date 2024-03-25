package com.example.backend.tweets.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TweetResponse {
    private Date createdAt;
    private String userName;
    private String fullText;
    private String replyTo;
    private Integer quoteCount;
    private Integer retweetCount;
    private Integer replyCount;
    private Integer likeCount;
}
