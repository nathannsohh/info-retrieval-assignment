package com.example.backend.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

@Service
public class SolrQueryBuilder {

    public SolrQuery build(SolrQueryParams queryParams) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(queryParams.getQuery());
        if (queryParams.getSort() != null && !queryParams.getSort().isEmpty()) {
            String[] sortParams = queryParams.getSort().split("\\s+");
            if (sortParams.length == 2) {
                solrQuery.addSort(SolrQuery.SortClause.create(sortParams[0], sortParams[1]));
            }
        }
        solrQuery.setStart(queryParams.getStart());
        solrQuery.setRows(queryParams.getRows());
        if (queryParams.getFields() != null) {
            solrQuery.setFields(queryParams.getFields().toArray(new String[0]));
        }
        return solrQuery;
    }
}
