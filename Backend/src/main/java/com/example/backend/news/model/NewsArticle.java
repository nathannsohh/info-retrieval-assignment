package com.example.backend.news.model;

import lombok.*;
import org.apache.solr.common.SolrInputDocument;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {
    private String id;
    private String sourceName;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt; // String representation of date
    private String content;

    public SolrInputDocument toSolrDocument() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", id);
        doc.addField("source_name", sourceName);
        doc.addField("author", author);
        doc.addField("title", title);
        doc.addField("description", description);
        doc.addField("url", url);
        doc.addField("urlToImage", urlToImage);
        doc.addField("publishedAt", publishedAt);
        doc.addField("content", content);
        return doc;
    }
}
