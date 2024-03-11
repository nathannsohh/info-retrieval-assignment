package com.example.backend.solr;

import com.example.backend.news.model.NewsArticle;
import com.example.backend.tweets.model.Tweet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

import static com.example.backend.solr.SolrConfig.*;

@Component
public class SolrServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrServer.class);
    private final SolrClient newsClient;
    private final SolrClient tweetsClient;

    private static final String TWEET_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZ yyyy";
    private static final SimpleDateFormat tweetDateFormat = new SimpleDateFormat(TWEET_DATE_FORMAT, Locale.ENGLISH);

    static {
        tweetDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public SolrServer() {
        this.newsClient = new Http2SolrClient.Builder(String.format(SOLR_URL, SOLR_HOST, SOLR_PORT, CORE_NEWS)).build();
        this.tweetsClient = new Http2SolrClient.Builder(String.format(SOLR_URL, SOLR_HOST, SOLR_PORT, CORE_TWITTER)).build();
    }

    public void indexNewsArticles(String csvFilePath) {

        List<NewsArticle> articles = parseCsv(csvFilePath, csvRecord -> {
            NewsArticle article = new NewsArticle();
            article.setId(csvRecord.get("id"));
            article.setSourceName(csvRecord.get("source_name"));
            article.setAuthor(csvRecord.get("author"));
            article.setTitle(csvRecord.get("title"));
            article.setDescription(csvRecord.get("description"));
            article.setUrl(csvRecord.get("url"));
            article.setUrlToImage(csvRecord.get("urlToImage"));
            article.setPublishedAt(csvRecord.get("publishedAt"));
            article.setContent(csvRecord.get("content"));
            return article;
        });

        for (NewsArticle article : articles) {
            try {
                newsClient.add(article.toSolrDocument());
            } catch (Exception e) {
                LOGGER.error("Error while indexing news articles: {}", e.getMessage(), e);
            }
        }
        LOGGER.info("News Articles indexed, but not committed yet.");

        try {
            newsClient.commit();
            LOGGER.info("News Articles indexed are now committed.");
        } catch (Exception e) {
            LOGGER.error("Error while committing indexed news articles: {}", e.getMessage(), e);
        }
    }

    public void indexTweets(String csvFilePath) {

        List<Tweet> tweets = parseCsv(csvFilePath, csvRecord -> {
            Tweet tweet = new Tweet();
            tweet.setId(csvRecord.get("id"));
            tweet.setCreatedAt(csvRecord.get("createdAt"));
            tweet.setUserName(csvRecord.get("userName"));
            tweet.setFullText(csvRecord.get("fullText"));
            tweet.setReplyTo(csvRecord.get("replyTo"));
            tweet.setLang(csvRecord.get("lang"));
            tweet.setQuoteCount(Integer.parseInt(csvRecord.get("quoteCount")));
            tweet.setRetweetCount(Integer.parseInt(csvRecord.get("retweetCount")));
            tweet.setReplyCount(Integer.parseInt(csvRecord.get("replyCount")));
            tweet.setLikeCount(Integer.parseInt(csvRecord.get("likeCount")));
            return tweet;
        });

        for (Tweet tweet : tweets) {
//            try {
//                tweetsClient.add(tweet.toSolrDocument());
//                LOGGER.info("Tweets indexed, but not committed yet.");
//            } catch (Exception e) {
//                LOGGER.error("Error while indexing tweets: {}", e.getMessage(), e);
//            }
            try {
                // Convert createdAt date string to ISO-8601 format
                String createdAt = tweet.getCreatedAt();
                Date parsedDate = tweetDateFormat.parse(createdAt);
                SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                isoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                String isoDate = isoDateFormat.format(parsedDate);
                tweet.setCreatedAt(isoDate); // Set the converted date back into the tweet
                tweetsClient.add(tweet.toSolrDocument());
            } catch (Exception e) {
                LOGGER.error("Error while indexing tweets: {}", e.getMessage(), e);
            }
        }
        LOGGER.info("Tweets indexed, but not committed yet.");

        try {
            tweetsClient.commit();
            LOGGER.info("Tweets indexed are now committed.");
        } catch (Exception e) {
            LOGGER.error("Error while committing indexed tweets: {}", e.getMessage(), e);
        }
    }

    // Generic method to parse CSV files
    public <T> List<T> parseCsv(String csvFilePath, Function<CSVRecord, T> recordToEntity) {
        List<T> entities = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build())) {

            for (CSVRecord csvRecord : csvParser) {
                T entity = recordToEntity.apply(csvRecord);
                entities.add(entity);
            }
        } catch (Exception e) {
            LOGGER.error("Error while parsing the CSV file: {}", e.getMessage(), e);
        }
        return entities;
    }
}
