package com.example.backend.reply.service;

import com.example.backend.exception.SolrQueryException;
import com.example.backend.reply.dto.ReplyTweetResponse;
import com.example.backend.reply.mapper.ReplyTweetMapper;
import com.example.backend.reply.model.ReplyTweet;
import com.example.backend.solr.SolrQueryBuilder;
import com.example.backend.solr.SolrQueryParams;
import com.example.backend.solr.SolrServer;
import java.io.IOException;
import java.util.List;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyTweetService {

  private final SolrClient replyTweetsClient;
  private final SolrQueryBuilder queryBuilder;
  private final ReplyTweetMapper replyTweetMapper;

  @Autowired
  public ReplyTweetService(SolrServer solrServer, SolrQueryBuilder queryBuilder,
      ReplyTweetMapper replyTweetMapper) {
    this.replyTweetsClient = solrServer.getReplyTweetsClient();
    this.queryBuilder = queryBuilder;
    this.replyTweetMapper = replyTweetMapper;
  }

  public List<ReplyTweetResponse> search(SolrQueryParams queryParams) {
    SolrQuery query = queryBuilder.build(queryParams);
    try {
      QueryResponse queryResponse = replyTweetsClient.query(query);
      List<ReplyTweet> replyTweets = queryResponse.getBeans(ReplyTweet.class);
      return replyTweetMapper.fromReplyTweetToReplyTweetResponseList(replyTweets);
    } catch (SolrServerException | IOException e) {
      throw new SolrQueryException("Error while querying tweets", e);
    }
  }
}
