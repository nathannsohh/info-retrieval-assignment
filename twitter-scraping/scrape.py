import asyncio
from twscrape import API, gather
import csv

async def main():
    api = API()

    await api.pool.add_account("<twitter-username>", "<twitter-password>", "<email>", "<email-password (but i found that you can put any string here and its fine)>")
    await api.pool.login_all()
    
    NEWS_AGENCIES = ["BBCWorld", "CNN", "WSJ", "nytimes", "straits_times", "ChannelNewsAsia", "FT", "guardiannews"]
    newsArticlesQuery = "elon musk lang:en "
    for i in range(len(NEWS_AGENCIES)):
        if i != len(NEWS_AGENCIES) - 1:
            newsArticlesQuery += ("from:" + NEWS_AGENCIES[i] + ' OR ')
        else:
            newsArticlesQuery += "from:" + NEWS_AGENCIES[i]

    tweet_ids = []

    # Uncomment this to run the news tweets scraping script. This will populate tweet_ids with the ids of the scraped tweets.
    # with open('newsTweets.csv', 'w', newline='') as file:
    #     writer = csv.writer(file)
    #     fields = ["id","createdAt","fullName","userName","profileImage","fullText","replyTo","lang","quoteCount","retweetCount","replyCount","likeCount", "viewCount"]
    #     writer.writerow(fields)
    #     async for tweet in api.search(newsArticlesQuery, limit=5000):
    #         print(tweet.id, tweet.user.username, tweet.rawContent)
    #         tweet_ids.append(tweet.id)
    #         writer.writerow([
    #             tweet.id, 
    #             tweet.date, 
    #             tweet.user.displayname, 
    #             tweet.user.username, 
    #             tweet.user.profileImageUrl, 
    #             tweet.rawContent,
    #             tweet.inReplyToTweetId,
    #             tweet.lang,
    #             tweet.quoteCount,
    #             tweet.retweetCount,
    #             tweet.replyCount,
    #             tweet.likeCount,
    #             tweet.viewCount
    #         ])

    # print(tweet_ids)
    
    # Reads the existing newsTweets to get the tweet ids of all the scraped news tweets and puts them in the tweets_id list
    with open('newsTweets.csv') as file:
        csv_reader = csv.reader(file, delimiter=',')
        line_count = 0

        for row in csv_reader:
            if line_count > 0:
                tweet_ids.append(row[0])
            
            line_count += 1
    
    # Uncomment this if you want to run the replies script right after finishing the news scraping
    # print(tweet_ids)
    # replyQuery = "lang:en "
    # for index, id in enumerate(tweet_ids):
    #     if index != len(tweet_ids) - 1:
    #         replyQuery += "conservation_id:" + str(id) + ' OR quoted_tweet_id:' + str(id) + ' OR '
    #     else:
    #         replyQuery += "conservation_id:" + str(id) + ' OR quoted_tweet_id:' + str(id)
    # print(replyQuery)


    # Creates the csv file for reply tweets
    with open('replyTweets.csv', 'w', newline='') as file:
        writer = csv.writer(file)
        fields = ["id","createdAt","fullName","userName","profileImage","fullText","replyTo","lang","quoteCount","retweetCount","replyCount","likeCount", "viewCount"]
        writer.writerow(fields)
        for id in tweet_ids:
            replyQuery = "lang:en conversation_id:" + str(id) + ' OR quoted_tweet_id:' + str(id)
            async for tweet in api.search(replyQuery, limit=2000):
                print(tweet.id, tweet.user.username, tweet.rawContent)
                writer.writerow([
                    tweet.id, 
                    tweet.date, 
                    tweet.user.displayname, 
                    tweet.user.username, 
                    tweet.user.profileImageUrl, 
                    tweet.rawContent,
                    tweet.inReplyToTweetId,
                    tweet.lang,
                    tweet.quoteCount,
                    tweet.retweetCount,
                    tweet.replyCount,
                    tweet.likeCount,
                    tweet.viewCount
                ])

if __name__ == "__main__":
    asyncio.run(main())