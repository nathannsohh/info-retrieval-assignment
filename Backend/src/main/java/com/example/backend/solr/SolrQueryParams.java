package com.example.backend.solr;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SolrQueryParams {

  // For NewsTweets search
  private Optional<String> query = Optional.empty();
  // For ReplyTweets search
  private Optional<String> replyTo = Optional.empty();
  // Sorting of results by indexed field in ascending (asc) or descending (desc) order.
  // e.g., sort=createdAt asc, sort=createdAt desc
  private Optional<List<String>> sort = Optional.empty();
  // For paginating results, the index of the first document to return. Default value of 0.
  private Optional<Integer> start = Optional.of(0);
  // For paginating results, the number of documents to return. Default value of 10.
  private Optional<Integer> rows = Optional.of(10);
  // Returns only the fields specified in the response.
  // e.g., fl=id,createdAt
  private Optional<List<String>> fields = Optional.empty();
  // Filters for date range
  private Optional<String> startDate = Optional.empty();
  private Optional<String> endDate = Optional.empty();
}
