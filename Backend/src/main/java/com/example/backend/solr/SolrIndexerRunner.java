package com.example.backend.solr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SolrIndexerRunner implements CommandLineRunner {

    private final SolrServer solrServer;
    @Value("${app.csv.news}")
    private String newsCsvPath;

    @Value("${app.csv.tweets}")
    private String tweetsCsvPath;

    @Autowired
    public SolrIndexerRunner(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    @Override
    public void run(String... args) throws Exception {
//        solrServer.indexNewsArticles(newsCsvPath);
        solrServer.indexTweets(tweetsCsvPath);
    }
}
