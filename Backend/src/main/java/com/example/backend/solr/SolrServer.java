package com.example.backend.solr;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.example.backend.solr.SolrServerConfig.*;

@Component
@Getter
public class SolrServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrServer.class);
    private final SolrClient newsClient;
    private final SolrClient tweetsClient;

    public SolrServer() {
        this.newsClient = new Http2SolrClient.Builder(String.format(SOLR_URL, SOLR_HOST, SOLR_PORT, CORE_NEWS)).build();
        this.tweetsClient = new Http2SolrClient.Builder(String.format(SOLR_URL, SOLR_HOST, SOLR_PORT, CORE_TWITTER)).build();
    }

    @PreDestroy
    public void shutdown() {
        try {
            newsClient.close();
            tweetsClient.close();
        } catch (Exception e) {
            LOGGER.error("Error while closing Solr clients", e);
        }
    }
}
