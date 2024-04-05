package com.example.backend.solr;

import com.example.backend.exception.CommittingException;
import com.example.backend.exception.CsvParsingException;
import com.example.backend.exception.IndexingException;
import java.io.IOException;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SolrIndexerRunner implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(SolrIndexerRunner.class);
  private final SolrIndexer solrIndexer;
  @Value("${app.csv.newsTweets}")
  private String newsTweetsPath;

  @Value("${app.csv.replyTweets}")
  private String replyTweetsPath;

  @Autowired
  public SolrIndexerRunner(SolrIndexer solrIndexer) {
    this.solrIndexer = solrIndexer;
  }

  @Override
  public void run(String... args) {
    try {
      solrIndexer.indexNewsTweets(newsTweetsPath);
      solrIndexer.indexReplyTweets(replyTweetsPath);
    } catch (CsvParsingException | IndexingException | CommittingException | SolrServerException |
             IOException e) {
      LOGGER.error("Error while storing data in Solr", e);
    }
  }
}
