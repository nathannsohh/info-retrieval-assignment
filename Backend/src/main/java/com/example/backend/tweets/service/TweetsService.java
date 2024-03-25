package com.example.backend.tweets.service;

import com.example.backend.exception.SolrQueryException;
import com.example.backend.solr.SolrQueryBuilder;
import com.example.backend.solr.SolrQueryParams;
import com.example.backend.solr.SolrServer;
import com.example.backend.tweets.dto.TweetResponse;
import com.example.backend.tweets.mapper.TweetMapper;
import com.example.backend.tweets.model.Tweet;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TweetsService {

    private final SolrClient tweetsClient;
    private final SolrQueryBuilder queryBuilder;
    private final TweetMapper tweetMapper;

    @Autowired
    public TweetsService(SolrServer solrServer, SolrQueryBuilder queryBuilder, TweetMapper tweetMapper) {
        this.tweetsClient = solrServer.getTweetsClient();
        this.queryBuilder = queryBuilder;
        this.tweetMapper = tweetMapper;
    }

    public List<TweetResponse> search(SolrQueryParams queryParams) {
        SolrQuery query = queryBuilder.build(queryParams);
        try {
            QueryResponse queryResponse = tweetsClient.query(query);
            List<Tweet> tweets = queryResponse.getBeans(Tweet.class);
            return tweetMapper.fromTweetsToTweetResponseList(tweets);
        } catch (SolrServerException | IOException e) {
            throw new SolrQueryException("Error while querying tweets", e);
        }
    }
}
