package com.example.backend.news.controller;

import com.example.backend.news.dto.NewsTweetAnalysisResponse;
import com.example.backend.news.service.NewsTweetService;
import com.example.backend.solr.SolrQueryParams;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news-tweets")
public class NewsTweetController {

  private final NewsTweetService newsTweetService;

  @Autowired
  public NewsTweetController(NewsTweetService newsTweetService) {
    this.newsTweetService = newsTweetService;
  }

  @GetMapping("/search")
  public ResponseEntity<NewsTweetAnalysisResponse> getTweets(
      @ModelAttribute SolrQueryParams queryParams) {
    NewsTweetAnalysisResponse tweetsWithStats = newsTweetService.search(queryParams);
    return ResponseEntity.ok(tweetsWithStats);
  }

  @GetMapping("/autocomplete")
  public ResponseEntity<List<String>> autocomplete(@RequestParam String query) {
    List<String> suggestions = newsTweetService.autocomplete(query);
    return ResponseEntity.ok(suggestions);
  }

}
