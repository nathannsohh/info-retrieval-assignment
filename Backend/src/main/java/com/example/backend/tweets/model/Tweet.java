package com.example.backend.tweets.model;

import lombok.*;
import org.apache.solr.common.SolrInputDocument;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

    private String id;
    private String createdAt; // String representation of date
    private String userName;
    private String fullText;
    private String replyTo;
    private String lang;
    private Integer quoteCount;
    private Integer retweetCount;
    private Integer replyCount;
    private Integer likeCount;

    public SolrInputDocument toSolrDocument() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", id);
        doc.addField("createdAt", createdAt);
        doc.addField("userName", userName);
        doc.addField("fullText", fullText);
        doc.addField("replyTo", replyTo);
        doc.addField("lang", lang);
        doc.addField("quoteCount", quoteCount);
        doc.addField("retweetCount", retweetCount);
        doc.addField("replyCount", replyCount);
        doc.addField("likeCount", likeCount);
        return doc;
    }

}
