package com.example.backend.solr;

import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

@Service
public class SolrQueryBuilder {

  public SolrQuery build(SolrQueryParams queryParams) {
    SolrQuery solrQuery = new SolrQuery();

    // Set the query parameter based on either NewsTweet or ReplyTweet search. Default to all if not provided.
    if (queryParams.getQuery().isPresent()) {
      String originalQuery = queryParams.getQuery().get(); // Get the query string from the Optional
      String processedQuery = originalQuery.trim()
          .replaceAll("\\s+", "+"); // Process the query string
      solrQuery.setQuery("fullText:" + processedQuery);

      // Set the fields to return. If no fields are specified, return all fields plus the score.
      if (queryParams.getFields().isPresent() && !queryParams.getFields().get().isEmpty()) {
        List<String> fieldsToReturn = new ArrayList<>(queryParams.getFields().get());
        fieldsToReturn.add("score"); // Add score to the list of fields
        solrQuery.setFields(fieldsToReturn.toArray(new String[0]));
      } else {
        // If no specific fields are provided, return all fields plus the score
        solrQuery.setFields("*,score");
      }
    } else if (queryParams.getReplyTo().isPresent()) {
      solrQuery.setQuery("replyTo:" + queryParams.getReplyTo().get());
    } else {
      solrQuery.setQuery("*:*");
    }

    // Set the sort parameter if provided
    queryParams.getSort().ifPresent(sortList -> {
      for (String sortParam : sortList) {
        String[] parts = sortParam.split("\\s+");
        if (parts.length == 2) {
          solrQuery.addSort(SolrQuery.SortClause.create(parts[0], parts[1]));
        }
      }
    });

    // Set the start and rows parameters for pagination, defaulting to 0 and 10 respectively
    solrQuery.setStart(queryParams.getStart().orElse(0));
    solrQuery.setRows(queryParams.getRows().orElse(10));

    // Set the date range filter only if both startDate and endDate provided
    if (queryParams.getStartDate().isPresent() && queryParams.getEndDate().isPresent()) {
      String startDate = queryParams.getStartDate().get() + "T00:00:00Z";
      String endDate = queryParams.getEndDate().get() + "T23:59:59Z";
      String dateRangeFilter = String.format("createdAt:[%s TO %s]", startDate, endDate);
      solrQuery.addFilterQuery(dateRangeFilter);
    }

    return solrQuery;
  }
}
