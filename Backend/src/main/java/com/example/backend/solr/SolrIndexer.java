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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@Component
public class SolrIndexer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrIndexer.class);

    // ISO-8601 format
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

    // Solr clients for news and tweets cores
    private final SolrClient newsClient;
    private final SolrClient tweetsClient;

    @Autowired
    public SolrIndexer(SolrServer solrServer) {
        this.newsClient = solrServer.getNewsClient();
        this.tweetsClient = solrServer.getTweetsClient();
    }

    public void indexNewsArticles(String csvFilePath) throws CsvParsingException, IndexingException, CommittingException, SolrServerException, IOException {
        newsClient.deleteByQuery("*:*"); // Clear any existing index before indexing new news articles

        List<NewsArticle> articles = parseCsv(csvFilePath, csvRecord -> {
            NewsArticle article = new NewsArticle();
            article.setUri(csvRecord.get("results__uri"));
            article.setLanguage(csvRecord.get("results__lang"));
            article.setDuplicate(Boolean.parseBoolean(csvRecord.get("results__isDuplicate")));
            // Parse and set the date
            article.setDate(parseDate(csvRecord.get("results__date"), newsDateFormat));
            article.setTime(parseDate(csvRecord.get("results__time"), newsTimeFormat));
            article.setDateTime(parseDate(csvRecord.get("results__dateTime"), isoDateFormat));
            article.setDateTimePub(parseDate(csvRecord.get("results__dateTimePub"), isoDateFormat));
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
        LOGGER.info("News Articles parsed");

        for (NewsArticle article : articles) {
            try {
                newsClient.add(article.toSolrDocument()); // Index the news article
            } catch (Exception e) {
                throw new IndexingException("Error while indexing news articles", e);
            }
        }
        LOGGER.info("News Articles indexed");

        try {
            newsClient.commit();
        } catch (Exception e) {
            throw new CommittingException("Error while committing indexed news articles", e);
        }
        LOGGER.info("News Articles committed");
    }

    public void indexTweets(String csvFilePath) throws CsvParsingException, IndexingException, CommittingException, SolrServerException, IOException {
        tweetsClient.deleteByQuery("*:*"); // Clear any existing index before indexing new tweets

        List<Tweet> tweets = parseCsv(csvFilePath, csvRecord -> {
            Tweet tweet = new Tweet();
            tweet.setId(csvRecord.get("id"));
            // Parse and set the date
            tweet.setCreatedAt(parseDate(csvRecord.get("createdAt"), tweetDateFormat));
            tweet.setUserName(csvRecord.get("userName"));
            tweet.setFullText(csvRecord.get("fullText"));
            tweet.setReplyTo(csvRecord.get("replyTo"));
            tweet.setLang(csvRecord.get("lang"));
            tweet.setQuoteCount(Integer.parseInt(csvRecord.get("quoteCount")));
            tweet.setRetweetCount(Integer.parseInt(csvRecord.get("retweetCount")));
            tweet.setReplyCount(Integer.parseInt(csvRecord.get("replyCount")));
            tweet.setLikeCount(Integer.parseInt(csvRecord.get("likeCount")));
            tweet.setSentiment(csvRecord.get("sentiment"));
            return tweet;
        });
        LOGGER.info("Tweets parsed");

        for (Tweet tweet : tweets) {
            try {
                tweetsClient.add(tweet.toSolrDocument()); // Index the tweet
            } catch (Exception e) {
                throw new IndexingException("Error while indexing tweets", e);
            }
        }
        LOGGER.info("Tweets indexed");

        try {
            tweetsClient.commit();
        } catch (Exception e) {
            throw new CommittingException("Error while committing indexed tweets", e);
        }
        LOGGER.info("Tweets committed");
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

    // Utility method to parse date, converting it from a string in .csv file to a Date object to be stored as attribute in model
    private Date parseDate(String dateStr, SimpleDateFormat dateFormat) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            LOGGER.error("Error parsing date " + dateStr, e);
            return null;
        }
    }

}
