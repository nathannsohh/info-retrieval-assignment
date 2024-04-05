package com.example.backend.solr;

import static com.example.backend.solr.SolrServerConfig.CORE_NEWS_TWEETS;
import static com.example.backend.solr.SolrServerConfig.CORE_REPLY_TWEETS;
import static com.example.backend.solr.SolrServerConfig.SOLR_HOST;
import static com.example.backend.solr.SolrServerConfig.SOLR_PORT;
import static com.example.backend.solr.SolrServerConfig.SOLR_URL;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SolrServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SolrServer.class);
  private final SolrClient newsTweetsClient;
  private final SolrClient replyTweetsClient;

  public SolrServer() {
    this.newsTweetsClient = new Http2SolrClient.Builder(
        String.format(SOLR_URL, SOLR_HOST, SOLR_PORT, CORE_NEWS_TWEETS)).build();
    this.replyTweetsClient = new Http2SolrClient.Builder(
        String.format(SOLR_URL, SOLR_HOST, SOLR_PORT,
            CORE_REPLY_TWEETS)).build();
  }

  @PreDestroy
  public void shutdown() {
    try {
      newsTweetsClient.close();
      replyTweetsClient.close();
    } catch (Exception e) {
      LOGGER.error("Error while closing Solr clients", e);
    }
  }
}
