package com.example.backend.tweets.controller;

import com.example.backend.solr.SolrQueryParams;
import com.example.backend.tweets.dto.TweetResponse;
import com.example.backend.tweets.service.TweetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tweets")
public class TweetsController {

    private final TweetsService tweetsService;

    @Autowired
    public TweetsController(TweetsService tweetsService) {
        this.tweetsService = tweetsService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<TweetResponse>> getTweets(@RequestParam("q") String query) {
        SolrQueryParams queryParams = new SolrQueryParams(query);
        List<TweetResponse> tweets = tweetsService.search(queryParams);
        return ResponseEntity.ok(tweets);
    }

}
