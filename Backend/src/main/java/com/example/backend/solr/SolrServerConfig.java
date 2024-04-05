package com.example.backend.solr;

public class SolrServerConfig {

  public static final String SOLR_URL = "http://%s:%s/solr/%s/";
  public static final String SOLR_HOST = "localhost";
  public static final String SOLR_PORT = "8983";
  public static final String CORE_REPLY_TWEETS = "reply_tweets_core";
  public static final String CORE_NEWS_TWEETS = "news_tweets_core";

  // Private constructor to prevent instantiation
  private SolrServerConfig() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}