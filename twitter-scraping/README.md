# Twitter Scraping Script

## Running the script
To run the script, navigate to the twitter-scraping folder and run the following commands in the terminal:
```
$ npm i 
$ node index.js 
```

To change the number of tweets retrieved, change the NUM_OF_TWEETS variable to any amount desired. The twitter API library will eventually throw a 429 error so the script will try to retrieve the tweets again every 15 minutes, so it may take awhile before you get all the tweets you need.

