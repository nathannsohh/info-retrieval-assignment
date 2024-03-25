package com.example.backend.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

@Service
public class SolrQueryBuilder {

    public SolrQuery build(SolrQueryParams queryParams) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(queryParams.getQuery());
        return solrQuery;
    }
}
