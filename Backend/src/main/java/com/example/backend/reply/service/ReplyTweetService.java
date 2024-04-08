package com.example.backend.reply.service;

import com.example.backend.exception.SolrQueryException;
import com.example.backend.reply.dto.SentimentAnalysisResponse;
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
import org.apache.solr.client.solrj.response.FacetField;
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

  public SentimentAnalysisResponse search(SolrQueryParams queryParams) {
    SolrQuery query = queryBuilder.build(queryParams);

    // Enable faceting
    query.setFacet(true);
    query.addFacetField("sentiment");
    query.setFacetMinCount(1);

    // Execute the query and fetch the documents as well as the facet counts
    try {
      QueryResponse queryResponse = replyTweetsClient.query(query);
      List<ReplyTweet> replyTweets = queryResponse.getBeans(ReplyTweet.class);
      FacetField sentimentFacet = queryResponse.getFacetField("sentiment");

      // Calculate sentiment statistics
      long positiveCount = 0;
      long negativeCount = 0;
      long neutralCount = 0;
      for (FacetField.Count count : sentimentFacet.getValues()) {
        switch (count.getName()) {
          case "0":
            negativeCount = count.getCount();
            break;
          case "1":
            neutralCount = count.getCount();
            break;
          case "2":
            positiveCount = count.getCount();
            break;
        }
      }
      long totalCount = positiveCount + negativeCount + neutralCount;

      // Create the response object
      SentimentAnalysisResponse analysisResponse = new SentimentAnalysisResponse();
      analysisResponse.setTotalReplyTweets(totalCount);
      analysisResponse.setPositive(calculatePercentage(positiveCount, totalCount));
      analysisResponse.setNegative(calculatePercentage(negativeCount, totalCount));
      analysisResponse.setNeutral(calculatePercentage(neutralCount, totalCount));
      analysisResponse.setReplyTweets(
          replyTweetMapper.fromReplyTweetListToReplyTweetResponseList(replyTweets));

      return analysisResponse;

    } catch (SolrServerException | IOException e) {
      throw new SolrQueryException("Error while querying Reply Tweets", e);
    }
  }

  private double calculatePercentage(long count, long total) {
    return total > 0 ? (double) count / total * 100 : 0;
  }
}
