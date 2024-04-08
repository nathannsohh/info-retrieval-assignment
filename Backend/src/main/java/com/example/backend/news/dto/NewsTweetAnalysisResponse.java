package com.example.backend.news.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewsTweetAnalysisResponse {

  private long totalNewsTweets;
  private long queryTime;
  private List<String> spellingSuggestions;
  private List<NewsTweetResponse> newsTweets;
}
