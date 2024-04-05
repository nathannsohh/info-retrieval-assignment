package com.example.backend.news.service;

import com.example.backend.exception.SolrQueryException;
import com.example.backend.news.dto.NewsTweetResponse;
import com.example.backend.news.mapper.NewsTweetMapper;
import com.example.backend.news.model.NewsTweet;
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
public class NewsTweetService {

  private final SolrClient newsTweetsClient;
  private final SolrQueryBuilder queryBuilder;
  private final NewsTweetMapper newsTweetMapper;

  @Autowired
  public NewsTweetService(SolrServer solrServer, SolrQueryBuilder queryBuilder,
      NewsTweetMapper newsTweetMapper) {
    this.newsTweetsClient = solrServer.getNewsTweetsClient();
    this.queryBuilder = queryBuilder;
    this.newsTweetMapper = newsTweetMapper;
  }

  public List<NewsTweetResponse> search(SolrQueryParams queryParams) {
    SolrQuery query = queryBuilder.build(queryParams);
    try {
      QueryResponse queryResponse = newsTweetsClient.query(query);
      List<NewsTweet> newsTweets = queryResponse.getBeans(NewsTweet.class);
      // Mapping allows the addition of relevance score
      List<NewsTweetResponse> responses = newsTweetMapper.fromNewsTweetListToNewsTweetResponseList(
          newsTweets);

      // Populate the scores obtained from Solr live query into the response
      for (int i = 0; i < newsTweets.size(); i++) {
        responses.get(i).setScore((Float) queryResponse.getResults().get(i).getFieldValue("score"));
      }

      return responses;
    } catch (SolrServerException | IOException e) {
      throw new SolrQueryException("Error while querying News Tweets", e);
    }
  }
}
