package com.example.backend.news.service;

import com.example.backend.exception.SolrQueryException;
import com.example.backend.news.dto.NewsTweetAnalysisResponse;
import com.example.backend.news.dto.NewsTweetResponse;
import com.example.backend.news.mapper.NewsTweetMapper;
import com.example.backend.news.model.NewsTweet;
import com.example.backend.solr.SolrQueryBuilder;
import com.example.backend.solr.SolrQueryParams;
import com.example.backend.solr.SolrServer;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
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

  public NewsTweetAnalysisResponse search(SolrQueryParams queryParams) {
    SolrQuery query = queryBuilder.build(queryParams);
    NewsTweetAnalysisResponse analysisResponse = new NewsTweetAnalysisResponse();

    try {
      long startTime = System.currentTimeMillis();
      QueryResponse queryResponse = newsTweetsClient.query(query);
      long endTime = System.currentTimeMillis();
      long queryTime = endTime - startTime;
      // Populate query time
      analysisResponse.setQueryTime(endTime - startTime);

      // Get the original query from queryParams for logging
      String userQuery = queryParams.getQuery().orElse("Unknown Query");
      LOGGER.info("Query time for \"%s\" is: %dms".formatted(userQuery, queryTime));

      // Populate total news tweets
      List<NewsTweet> newsTweets = queryResponse.getBeans(NewsTweet.class);
      analysisResponse.setTotalNewsTweets(queryResponse.getResults().getNumFound());

      // Populate score
      List<NewsTweetResponse> responses = IntStream.range(0, newsTweets.size())
          .mapToObj(index -> {
            NewsTweet newsTweet = newsTweets.get(index);
            NewsTweetResponse response = newsTweetMapper.fromNewsTweetToNewsTweetResponse(
                newsTweet);
            response.setScore((Float) queryResponse.getResults().get(index).getFieldValue("score"));
            return response;
          })
          .toList();
      analysisResponse.setNewsTweets(responses);

      // Populate spellcheck suggestions if any
      SpellCheckResponse spellCheckResponse = queryResponse.getSpellCheckResponse();
      if (spellCheckResponse != null && !spellCheckResponse.isCorrectlySpelled()) {
        List<String> suggestions = spellCheckResponse.getSuggestions().stream()
            .sorted(Comparator.comparing(s -> -s.getAlternativeFrequencies()
                .get(0))) // Sort in descending order of frequency
            .limit(5) // Limit to top 5 suggestions
            .flatMap(suggestion -> suggestion.getAlternatives().stream())
            .distinct() // Remove duplicate suggestions
            .toList();
        analysisResponse.setSpellingSuggestions(suggestions);
      }

      return analysisResponse;
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
