package com.example.backend.tweets.model;

import lombok.*;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrInputDocument;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

    @Field
    private String id;
    @Field
    private Date createdAt;
    @Field
    private String userName;
    @Field
    private String fullText;
    @Field
    private String replyTo;
    @Field
    private String lang;
    @Field
    private Integer quoteCount;
    @Field
    private Integer retweetCount;
    @Field
    private Integer replyCount;
    @Field
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
