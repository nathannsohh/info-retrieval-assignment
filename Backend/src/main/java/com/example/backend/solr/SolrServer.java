package com.example.backend.solr;

import com.example.backend.exception.CommittingException;
import com.example.backend.exception.CsvParsingException;
import com.example.backend.exception.IndexingException;
import com.example.backend.news.model.NewsArticle;
import com.example.backend.tweets.model.Tweet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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

    // ISO-8601 format for Solr indexing (we standardize to this)
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final SimpleDateFormat isoDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT);

    // Tweets data that are not in ISO-8601 format (wrt the .csv file)
    private static final String TWEET_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZ yyyy";
    private static final SimpleDateFormat tweetDateFormat = new SimpleDateFormat(TWEET_DATE_FORMAT, Locale.ENGLISH);

    // News data that are not in ISO-8601 format (wrt the .csv file)
    private static final String NEWS_DATE_FORMAT = "dd/MM/yy";
    private static final String NEWS_TIME_FORMAT = "HH:mm:ss";
    private static final SimpleDateFormat newsDateFormat = new SimpleDateFormat(NEWS_DATE_FORMAT, Locale.ENGLISH);
    private static final SimpleDateFormat newsTimeFormat = new SimpleDateFormat(NEWS_TIME_FORMAT, Locale.ENGLISH);

    static {
        // ISO-8601 data formatting
        isoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        // Tweets data formatting
        tweetDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        // News data formatting
        newsDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        newsTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public SolrServer() {
        this.newsClient = new Http2SolrClient.Builder(String.format(SOLR_URL, SOLR_HOST, SOLR_PORT, CORE_NEWS)).build();
        this.tweetsClient = new Http2SolrClient.Builder(String.format(SOLR_URL, SOLR_HOST, SOLR_PORT, CORE_TWITTER)).build();
    }

    public void indexNewsArticles(String csvFilePath) throws CsvParsingException, IndexingException, CommittingException, SolrServerException, IOException {
        newsClient.deleteByQuery("*:*"); // Clear any existing index before indexing new news articles

        List<NewsArticle> articles = parseCsv(csvFilePath, csvRecord -> {
            NewsArticle article = new NewsArticle();
            article.setUri(csvRecord.get("results__uri"));
            article.setLanguage(csvRecord.get("results__lang"));
            article.setDuplicate(Boolean.parseBoolean(csvRecord.get("results__isDuplicate")));
            article.setDate(csvRecord.get("results__date"));
            article.setTime(csvRecord.get("results__time"));
            article.setDateTime(csvRecord.get("results__dateTime"));
            article.setDateTimePub(csvRecord.get("results__dateTimePub"));
            article.setDataType(csvRecord.get("results__dataType"));
            article.setSimilarity(Double.parseDouble(csvRecord.get("results__sim")));
            article.setUrl(csvRecord.get("results__url"));
            article.setTitle(csvRecord.get("results__title"));
            article.setBody(csvRecord.get("results__body"));
            article.setSourceUri(csvRecord.get("results__source__uri"));
            article.setSourceDataType(csvRecord.get("results__source__dataType"));
            article.setSourceTitle(csvRecord.get("results__source__title"));
            article.setAuthorUri(csvRecord.get("results__authors__uri"));
            article.setAuthorName(csvRecord.get("results__authors__name"));
            article.setAuthorType(csvRecord.get("results__authors__type"));
            article.setAgency(Boolean.parseBoolean(csvRecord.get("results__authors__isAgency")));
            article.setImageUrl(csvRecord.get("results__image"));
            article.setEventUri(csvRecord.get("results__eventUri"));
            article.setSentiment(Double.parseDouble(csvRecord.get("results__sentiment")));
            article.setWeight(Double.parseDouble(csvRecord.get("results__wgt")));
            article.setRelevance(Double.parseDouble(csvRecord.get("results__relevance")));
            return article;
        });
        LOGGER.info("News Articles parsed.");

        for (NewsArticle article : articles) {
            try {
                // Convert date and time strings to ISO-8601 string format before indexing the news article
                Date parsedDate = newsDateFormat.parse(article.getDate()); // Convert the date string to a Date object
                String isoDate = isoDateFormat.format(parsedDate); // Convert the Date object to a string in ISO-8601 format
                article.setDate(isoDate); // Set the converted ISO-8601 date string back to the news article object

                Date parsedTime = newsTimeFormat.parse(article.getTime()); // Convert the time string to a Date object
                String isoTime = isoDateFormat.format(parsedTime); // Convert the Date object to a string in ISO-8601 format
                article.setTime(isoTime); // Set the converted ISO-8601 time string back to the news article object

                newsClient.add(article.toSolrDocument()); // Index the news article
            } catch (Exception e) {
                throw new IndexingException("Error while indexing news articles", e);
            }
        }
        LOGGER.info("News Articles indexed.");

        try {
            newsClient.commit();
        } catch (Exception e) {
            throw new CommittingException("Error while committing indexed news articles", e);
        }
        LOGGER.info("News Articles committed.");
    }

    public void indexTweets(String csvFilePath) throws CsvParsingException, IndexingException, CommittingException, SolrServerException, IOException {
        tweetsClient.deleteByQuery("*:*"); // Clear any existing index before indexing new tweets

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
        LOGGER.info("Tweets parsed.");

        for (Tweet tweet : tweets) {
            try {
                // Convert createdAt date string to ISO-8601 string format before indexing the tweet
                Date parsedDate = tweetDateFormat.parse(tweet.getCreatedAt()); // Convert the date string to a Date object
                String isoDate = isoDateFormat.format(parsedDate); // Convert the Date object to a string in ISO-8601 format
                tweet.setCreatedAt(isoDate); // Set the converted ISO-8601 date string back to the tweet object
                tweetsClient.add(tweet.toSolrDocument()); // Index the tweet
            } catch (Exception e) {
                throw new IndexingException("Error while indexing tweets", e);
            }
        }
        LOGGER.info("Tweets indexed.");

        try {
            tweetsClient.commit();
        } catch (Exception e) {
            throw new CommittingException("Error while committing indexed tweets", e);
        }
        LOGGER.info("Tweets committed.");
    }

    // Generic method to parse CSV files
    public <T> List<T> parseCsv(String csvFilePath, Function<CSVRecord, T> recordToEntity) throws CsvParsingException {
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
}
