package com.example.backend.solr;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SolrQueryParams {
    // The query string to search for in the format of field:value. *:* returns all documents.
    // e.g., query=userName:john, query=fullText:elonmusk
    private String query;
    // Sorting of results by indexed field in ascending (asc) or descending (desc) order.
    // e.g., sort=createdAt asc, sort=createdAt desc
    private String sort;
    // For paginating results, the index of the first document to return.
    private int start;
    // For paginating results, the number of documents to return.
    private int rows;
    // The fields to return in the response.
    // e.g., fl=id,createdAt
    private List<String> fields;
}
