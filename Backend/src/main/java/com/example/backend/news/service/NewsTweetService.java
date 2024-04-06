package com.example.backend.news.service;

import com.example.backend.exception.SolrQueryException;
import com.example.backend.news.dto.NewsTweetResponse;
import com.example.backend.news.mapper.NewsTweetMapper;
import com.example.backend.news.model.NewsTweet;
import com.example.backend.solr.SolrQueryBuilder;
import com.example.backend.solr.SolrQueryParams;
import com.example.backend.solr.SolrServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
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
      List<NewsTweetResponse> responses;

      // Addition of relevance score if there are responses
      if (!newsTweets.isEmpty()) {
        responses = newsTweetMapper.fromNewsTweetListToNewsTweetResponseList(newsTweets);
        for (int i = 0; i < newsTweets.size(); i++) {
          responses.get(i)
              .setScore((Float) queryResponse.getResults().get(i).getFieldValue("score"));
        }
      } else { // Else there are no responses, create a dummy response to store spellcheck suggestions
        responses = new ArrayList<>();
        NewsTweetResponse suggestionResponse = new NewsTweetResponse();
        responses.add(suggestionResponse);
      }

      SpellCheckResponse spellCheckResponse = queryResponse.getSpellCheckResponse();
      if (spellCheckResponse != null && !spellCheckResponse.isCorrectlySpelled()) {
        List<String> suggestions = spellCheckResponse.getSuggestions().stream()
            .sorted((s1, s2) -> {
              List<Integer> freqs1 = s1.getAlternativeFrequencies();
              List<Integer> freqs2 = s2.getAlternativeFrequencies();
              return freqs2.get(0).compareTo(freqs1.get(0)); // Compare based on the first frequency
            })
            .limit(3) // Limit to top 3 suggestions
            .flatMap(suggestion -> suggestion.getAlternatives().stream())
            .toList();
        // Assign suggestions to the first response (or only response if there were no news tweets)
        responses.get(0).setSpellingSuggestions(suggestions);
      }

      return responses;
    } catch (SolrServerException | IOException e) {
      throw new SolrQueryException("Error while querying News Tweets", e);
    }
  }
}
