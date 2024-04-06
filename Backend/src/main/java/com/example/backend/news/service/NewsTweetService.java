package com.example.backend.news.service;

import com.example.backend.exception.SolrQueryException;
import com.example.backend.news.dto.NewsTweetResponse;
import com.example.backend.news.mapper.NewsTweetMapper;
import com.example.backend.news.model.NewsTweet;
import com.example.backend.solr.SolrQueryBuilder;
import com.example.backend.solr.SolrQueryParams;
import com.example.backend.solr.SolrServer;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsTweetService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NewsTweetService.class);
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
      long startTime = System.currentTimeMillis();
      QueryResponse queryResponse = newsTweetsClient.query(query);
      long endTime = System.currentTimeMillis();
      long queryTime = endTime - startTime;

      // Get the original query from queryParams for logging
      String userQuery = queryParams.getQuery().orElse("Unknown Query");
      LOGGER.info("Query time for \"%s\" is: %dms".formatted(userQuery, queryTime));

      List<NewsTweet> newsTweets = queryResponse.getBeans(NewsTweet.class);
      List<NewsTweetResponse> responses = newsTweetMapper.fromNewsTweetListToNewsTweetResponseList(
          newsTweets);

      // Populate scores and spellcheck suggestions
      SpellCheckResponse spellCheckResponse = queryResponse.getSpellCheckResponse();
      if (spellCheckResponse != null && !spellCheckResponse.isCorrectlySpelled()) {
        List<String> suggestions = spellCheckResponse.getSuggestions().stream()
            .sorted(Comparator.comparing(s -> -s.getAlternativeFrequencies()
                .get(0))) // Sort in descending order of frequency
            .limit(5) // Limit to top 5 suggestions
            .flatMap(suggestion -> suggestion.getAlternatives().stream())
            .distinct() // Remove duplicate suggestions
            .toList();

        // If there are news tweets, add suggestions to all. Otherwise, create a dummy response.
        if (!responses.isEmpty()) {
          responses.forEach(response -> response.setSpellingSuggestions(suggestions));
        } else {
          NewsTweetResponse suggestionResponse = new NewsTweetResponse();
          suggestionResponse.setSpellingSuggestions(suggestions);
          responses.add(suggestionResponse);
        }
      }

      // Set query time for all responses
      responses.forEach(response -> response.setQueryTime(queryTime));

      return responses;
    } catch (SolrServerException | IOException e) {
      throw new SolrQueryException("Error while querying News Tweets", e);
    }
  }

  public List<String> autocomplete(String term) {
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/suggest");
    query.set("suggest", true);
    query.set("suggest.build", true);
    query.set("suggest.dictionary",
        "newsTweetSuggester");
    query.set("suggest.q", term);

    try {
      QueryResponse response = newsTweetsClient.query(query);
      return response.getSuggesterResponse().getSuggestedTerms().values().stream()
          .flatMap(List::stream)
          .toList();
    } catch (SolrServerException | IOException e) {
      LOGGER.error("Error during autocomplete suggestion fetching", e);
      throw new SolrQueryException("Error while fetching autocomplete suggestions", e);
    }
  }
}
