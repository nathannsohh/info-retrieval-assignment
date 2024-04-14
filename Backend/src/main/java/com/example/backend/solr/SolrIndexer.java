package com.example.backend.solr;

import com.example.backend.exception.CommittingException;
import com.example.backend.exception.CsvParsingException;
import com.example.backend.exception.IndexingException;
import com.example.backend.news.model.NewsTweet;
import com.example.backend.reply.model.ReplyTweet;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Function;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolrIndexer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SolrIndexer.class);

  // The date format of both newsTweets and replyTweets .csv file
  private static final String TWEET_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssX";
  private static final SimpleDateFormat tweetDateFormat = new SimpleDateFormat(TWEET_DATE_FORMAT,
      Locale.ENGLISH);

  static {
    // Tweets data formatting
    tweetDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  // Solr clients for news_tweets_core and reply_tweets_core
  private final SolrClient newsTweetsClient;
  private final SolrClient replyTweetsClient;

  @Autowired
  public SolrIndexer(SolrServer solrServer) {
    this.newsTweetsClient = solrServer.getNewsTweetsClient();
    this.replyTweetsClient = solrServer.getReplyTweetsClient();
  }

  public void indexNewsTweets(String csvFilePath)
      throws CsvParsingException, IndexingException, CommittingException, SolrServerException, IOException {
    newsTweetsClient.deleteByQuery(
        "*:*"); // Clear any existing index before indexing new replyTweets

    List<NewsTweet> newsTweets = parseCsv(csvFilePath, csvRecord -> {
      NewsTweet newsTweet = new NewsTweet();
      newsTweet.setId(csvRecord.get("id"));
      // Parse and set the date
      newsTweet.setCreatedAt(parseDate(csvRecord.get("createdAt"), tweetDateFormat));
      newsTweet.setFullName(csvRecord.get("fullName"));
      newsTweet.setUserName(csvRecord.get("userName"));
      newsTweet.setProfileImage(csvRecord.get("profileImage"));
      newsTweet.setFullText(csvRecord.get("fullText"));
      newsTweet.setReplyTo(csvRecord.get("replyTo"));
      newsTweet.setLang(csvRecord.get("lang"));
      newsTweet.setQuoteCount(safeParseLong(csvRecord.get("quoteCount")));
      newsTweet.setRetweetCount(safeParseLong(csvRecord.get("retweetCount")));
      newsTweet.setReplyCount(safeParseLong(csvRecord.get("replyCount")));
      newsTweet.setLikeCount(safeParseLong(csvRecord.get("likeCount")));
      newsTweet.setViewCount(safeParseLong(csvRecord.get("viewCount")));
      return newsTweet;
    });
    LOGGER.info("News Tweets parsed");

    for (NewsTweet newsTweet : newsTweets) {
      try {
        newsTweetsClient.add(newsTweet.toSolrDocument()); // Index the replyTweet
      } catch (Exception e) {
        throw new IndexingException("Error while indexing replyTweets", e);
      }
    }
    LOGGER.info("News Tweets indexed");

    try {
      newsTweetsClient.commit();
    } catch (Exception e) {
      throw new CommittingException("Error while committing indexed Reply Tweets", e);
    }
    LOGGER.info("News Tweets committed");
  }

  public void indexReplyTweets(String csvFilePath)
      throws CsvParsingException, IndexingException, CommittingException, SolrServerException, IOException {
    replyTweetsClient.deleteByQuery(
        "*:*"); // Clear any existing index before indexing new replyTweets

    List<ReplyTweet> replyTweets = parseCsv(csvFilePath, csvRecord -> {
      ReplyTweet replyTweet = new ReplyTweet();
      replyTweet.setId(csvRecord.get("id"));
      // Parse and set the date
      replyTweet.setCreatedAt(parseDate(csvRecord.get("createdAt"), tweetDateFormat));
      replyTweet.setFullName(csvRecord.get("fullName"));
      replyTweet.setUserName(csvRecord.get("userName"));
      replyTweet.setProfileImage(csvRecord.get("profileImage"));
      replyTweet.setFullText(csvRecord.get("fullText"));
      replyTweet.setReplyTo(csvRecord.get("replyTo"));
      replyTweet.setLang(csvRecord.get("lang"));
      replyTweet.setQuoteCount(safeParseLong(csvRecord.get("quoteCount")));
      replyTweet.setRetweetCount(safeParseLong(csvRecord.get("retweetCount")));
      replyTweet.setReplyCount(safeParseLong(csvRecord.get("replyCount")));
      replyTweet.setLikeCount(safeParseLong(csvRecord.get("likeCount")));
      replyTweet.setViewCount(safeParseLong(csvRecord.get("viewCount")));
      replyTweet.setSentiment(safeParseInt(csvRecord.get("sentiment")));
      replyTweet.setSarcasm(safeParseInt(csvRecord.get("sarcasm")));
      replyTweet.setSentimentDetail(csvRecord.get("sentimentDetail"));
      return replyTweet;
    });
    LOGGER.info("Reply Tweets parsed");

    for (ReplyTweet replyTweet : replyTweets) {
//      LOGGER.info("Preparing to index replyTweet with ID: {}", replyTweet.getId());
      if (replyTweet.getId() == null) {
        LOGGER.error("Found replyTweet with null ID, skipping: {}", replyTweet);
        continue;
      }
      try {
        replyTweetsClient.add(replyTweet.toSolrDocument()); // Index the replyTweet
      } catch (Exception e) {
        LOGGER.error("Error while indexing replyTweet with ID: {}", replyTweet.getId(), e);
        throw new IndexingException("Error while indexing replyTweets", e);
      }
    }
    LOGGER.info("Reply Tweets indexed");

    try {
      replyTweetsClient.commit();
    } catch (Exception e) {
      throw new CommittingException("Error while committing indexed Reply Tweets", e);
    }
    LOGGER.info("Reply Tweets committed");
  }

  // Generic method to parse CSV files
  public <T> List<T> parseCsv(String csvFilePath, Function<CSVRecord, T> recordToEntity)
      throws CsvParsingException {
    List<T> entities = new ArrayList<>();
    try (Reader reader = new InputStreamReader(
        BOMInputStream
            .builder()
            .setInputStream(Files.newInputStream(Paths.get(csvFilePath)))
            .get()
        , StandardCharsets.UTF_8)) {
      final CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
          .builder()
          .setHeader()
          .setSkipHeaderRecord(true)
          .build());

      for (CSVRecord csvRecord : csvParser) {
        T entity = recordToEntity.apply(csvRecord);
        entities.add(entity);
      }
    } catch (IOException e) {
      throw new CsvParsingException("Error while parsing CSV file", e);
    }
    return entities;
  }

  // Utility method to parse date, converting it from a string in .csv file to a Date object to be stored as attribute in model
  private Date parseDate(String dateStr, SimpleDateFormat dateFormat) {
    try {
      return dateFormat.parse(dateStr);
    } catch (ParseException e) {
      LOGGER.error("Error parsing date " + dateStr, e);
      return null;
    }
  }

  // Utility method to safely parse long integers from strings
  private Long safeParseLong(String str) {
    if (str == null || str.trim().isEmpty()) {
      return 0L; // Default value for empty strings
    }
    try {
      return Long.parseLong(str);
    } catch (NumberFormatException e) {
      // Only log an error if the string seems like it could be a number
      if (str.matches("-?\\d+")) { // Regex to check if string is a whole number
        LOGGER.error("Error parsing long from string: " + str, e);
      }
      // Otherwise, it's likely the string wasn't supposed to be a number,
      // so we can default to 0 without logging
      return 0L; // Default value for strings that are not valid long integers
    }
  }

  // Utility method to safely parse int from strings
  private Integer safeParseInt(String str) {
    if (str == null || str.trim().isEmpty()) {
      return 0; // Default value for empty strings
    }
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {
      LOGGER.error("Error parsing int from string: " + str, e);
      return 0; // Default value for strings that are not valid ints
    }
  }


}
