package com.example.backend.reply.controller;

import com.example.backend.reply.dto.ReplyTweetResponse;
import com.example.backend.reply.service.ReplyTweetService;
import com.example.backend.solr.SolrQueryParams;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reply-tweets")
public class ReplyTweetController {

  private final ReplyTweetService replyTweetService;

  @Autowired
  public ReplyTweetController(ReplyTweetService replyTweetService) {
    this.replyTweetService = replyTweetService;
  }

  @GetMapping("/search")
  public ResponseEntity<List<ReplyTweetResponse>> getTweets(
      @ModelAttribute SolrQueryParams queryParams) {
    List<ReplyTweetResponse> tweets = replyTweetService.search(queryParams);
    return ResponseEntity.ok(tweets);
  }

}
