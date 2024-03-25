package com.example.backend.news.model;

import lombok.*;
import org.apache.solr.common.SolrInputDocument;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {
    private String uri;
    private String language;
    private boolean isDuplicate;
    private Date date;
    private Date time;
    private Date dateTime;
    private Date dateTimePub;
    private String dataType;
    private double similarity;
    private String url;
    private String title;
    private String body;
    private String sourceUri;
    private String sourceDataType;
    private String sourceTitle;
    private String authorUri;
    private String authorName;
    private String authorType;
    private boolean isAgency;
    private String imageUrl;
    private String eventUri;
    private double sentiment;
    private double weight;
    private double relevance;

    public SolrInputDocument toSolrDocument() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("results__uri", uri);
        doc.addField("results__lang", language);
        doc.addField("results__isDuplicate", isDuplicate);
        doc.addField("results__date", date);
        doc.addField("results__time", time);
        doc.addField("results__dateTime", dateTime);
        doc.addField("results__dateTimePub", dateTimePub);
        doc.addField("results__dataType", dataType);
        doc.addField("results__sim", similarity);
        doc.addField("results__url", url);
        doc.addField("results__title", title);
        doc.addField("results__body", body);
        doc.addField("results__source__uri", sourceUri);
        doc.addField("results__source__dataType", sourceDataType);
        doc.addField("results__source__title", sourceTitle);
        doc.addField("results__authors__uri", authorUri);
        doc.addField("results__authors__name", authorName);
        doc.addField("results__authors__type", authorType);
        doc.addField("results__authors__isAgency", isAgency);
        doc.addField("results__image", imageUrl);
        doc.addField("results__eventUri", eventUri);
        doc.addField("results__sentiment", sentiment);
        doc.addField("results__wgt", weight);
        doc.addField("results__relevance", relevance);
        return doc;
    }
}
