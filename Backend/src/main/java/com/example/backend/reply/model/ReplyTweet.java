package com.example.backend.reply.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrInputDocument;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReplyTweet {

  @Field
  private String id;
  @Field
  private Date createdAt;
  @Field
  private String fullName;
  @Field
  private String userName;
  @Field
  private String profileImage;
  @Field
  private String fullText;
  @Field
  private String replyTo;
  @Field
  private String lang;
  @Field
  private Long quoteCount;
  @Field
  private Long retweetCount;
  @Field
  private Long replyCount;
  @Field
  private Long likeCount;
  @Field
  private Long viewCount;
  @Field
  private Integer sentiment;
  @Field
  private Integer sarcasm;
  @Field
  private String sentimentDetail;

  public SolrInputDocument toSolrDocument() {
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", id);
    doc.addField("createdAt", createdAt);
    doc.addField("fullName", fullName);
    doc.addField("userName", userName);
    doc.addField("profileImage", profileImage);
    doc.addField("fullText", fullText);
    doc.addField("replyTo", replyTo);
    doc.addField("lang", lang);
    doc.addField("quoteCount", quoteCount);
    doc.addField("retweetCount", retweetCount);
    doc.addField("replyCount", replyCount);
    doc.addField("likeCount", likeCount);
    doc.addField("viewCount", viewCount);
    doc.addField("sentiment", sentiment);
    doc.addField("sarcasm", sarcasm);
    doc.addField("sentimentDetail", sentimentDetail);
    return doc;
  }

}
