import fs from 'fs'
import Rettwit from 'rettiwt-api'
import retry from 'async-retry';

import parse from 'csv-parser'

let NUM_OF_TWEETS = 1000
let TWEEET_IDS = new Set([])
let MIN_ID = null;

// const rettiwt = new Rettwit.Rettiwt();

// // // Logging in an getting the API_KEY
// rettiwt.auth.login('sohnathan555@gmail.com', 'webscraper555', 'B@ndung123')
// .then(apiKey => {
//     // Use the API_KEY
// 	console.log(apiKey)
// })

const rettiwt = new Rettwit.Rettiwt({ apiKey: "a2R0PXRIMHQwNklHeFJTUk5Rb2tnTVRXakNLd2lvYzM4V1FSa2ZvQnZmY3o7dHdpZD0idT0xNzczMTc0NzI0NjQ0MDE2MTI4IjtjdDA9ZjQyYmU5MWY2MDFjZDQwY2U4NGU1Mjc1OWVlMmMyZDM7YXV0aF90b2tlbj04MDZhMjI0MzQxYjA2YjdkODVjMjQ0M2E0YzIwOGFjZWQyZDViZDkxOw=="});
// const rettiwt = new Rettwit.Rettiwt({ apiKey: "a2R0PUxKckpaSERWVEkyTjkzNDVpV2o4Um5tV2hGQzNHZWJpS0xvNjY5dlA7dHdpZD0idT0xNzczMTk2MDM3MDA1NzEzNDA4IjtjdDA9MjA2ODFiMzI5YjUxMmU1YTlhYjkzNjA1YzQxMGFhNTk7YXV0aF90b2tlbj1iZmU0ZDEzNTVmYmJmY2U4OWViOGI4Y2U5ZWZlMDM5MGU4YzBkZTJhOw=="});
let csvContent = "id,createdAt,fullName,userName,profileImage,fullText,replyTo,lang,quoteCount,retweetCount,replyCount,retweetCount,likeCount\r\n";
let data = null;

const escapeCsvField = (field) => {
  if (typeof field === 'string' && /[",\n]/.test(field)) {
    return `"${field.replace(/"/g, '""')}"`;
  }
  return field;
};

const fetchDataChunk = async () => {
  try {
    data = await retry(
      async () => {
        // let rettiwt = new Rettwit.Rettiwt()
        return await rettiwt.tweet.search({
          fromUsers: ['BBCWorld', 'ChannelNewsAsia'],
          includeWords: ['elon', 'musk']
        }, 20, data?.next.value);
      },
      {
        retries: 3, // Number of retries
        minTimeout: 480000, // Initial timeout in milliseconds
        maxTimeout: 480000, // Maximum timeout in milliseconds
        randomize: true, // Randomize the timeouts
      }
    );

    console.log(data);

    NUM_OF_TWEETS -= data.list.length;
    data.list.forEach((row) => {
      TWEEET_IDS.add(row.id)
      if (MIN_ID === null || Number(row.id) < Number(MIN_ID)) MIN_ID = row.id
      console.log(MIN_ID)
      let dataRow = [
        row.id,
        row.createdAt,
        escapeCsvField(row.tweetBy.fullName),
        escapeCsvField(row.tweetBy.userName),
        escapeCsvField(row.tweetBy.profileImage),
        escapeCsvField(row.fullText),
        escapeCsvField(row.replyTo),
        row.lang,
        row.quoteCount,
        row.retweetCount,
        row.replyCount,
        row.retweetCount,
        row.likeCount
      ];
      csvContent += dataRow.join(',') + "\r\n";
    });

    fs.writeFileSync('news_tweets.csv', csvContent, 'utf-8');
    if (!data.next || NUM_OF_TWEETS <= 0) {
      // If there is no more data or reached the desired number of tweets, write the CSV file
      console.log("CSV File created successfully");
    } else {
      // Continue fetching more data
      await fetchDataChunk();
    }
  } catch (error) {
    console.error("Error fetching data:", error.message);

    // Check if it's a rate-limiting error
    if (error.statusCode === 429) {
      const waitTimeInMinutes = 15;
      console.log(`Rate limit exceeded. Waiting for ${waitTimeInMinutes} minutes before retrying...`);

      // Wait for 15 minutes before calling fetchDataChunk again
      setTimeout(() => {
        fetchDataChunk();
      }, waitTimeInMinutes * 60 * 1000); // Convert minutes to milliseconds
    }
  }
};

const fetchRepliesOfTweets = async () => {
  try {
    data = await retry(
      async () => {
        return await rettiwt.tweet.search({
          toUsers: ['ChannelNewsAsia'],
          language: "en",
          sinceId: MIN_ID
        }, 20, data?.next.value);
      },
      {
        retries: 3, // Number of retries
        minTimeout: 480000, // Initial timeout in milliseconds
        maxTimeout: 480000, // Maximum timeout in milliseconds
        randomize: true, // Randomize the timeouts
      }
    );

    console.log(data);

    data.list.forEach((row) => {
      if ((row.replyTo !== undefined && TWEEET_IDS.has(row.replyTo)) || (row.quoted !== undefined && TWEEET_IDS.has(row.quoted))) {
        let dataRow = [
          row.id,
          row.createdAt,
          escapeCsvField(row.tweetBy.fullName),
          escapeCsvField(row.tweetBy.userName),
          escapeCsvField(row.tweetBy.profileImage),
          escapeCsvField(row.fullText),
          escapeCsvField(row.replyTo),
          row.lang,
          row.quoteCount,
          row.retweetCount,
          row.replyCount,
          row.retweetCount,
          row.likeCount
        ];
        csvContent += dataRow.join(',') + "\r\n";
      }
    });

    fs.writeFileSync('replies2.csv', csvContent, 'utf-8');
    if (!data.next) {
      // If there is no more data or reached the desired number of tweets, write the CSV file
      console.log("CSV File created successfully");
    } else {
      // Continue fetching more data
      await fetchRepliesOfTweets();
    }
  } catch (error) {
    console.error("Error fetching data:", error.message);

    // Check if it's a rate-limiting error
    if (error.statusCode === 429) {
      const waitTimeInMinutes = 15;
      console.log(`Rate limit exceeded. Waiting for ${waitTimeInMinutes} minutes before retrying...`);

      // Wait for 15 minutes before calling fetchDataChunk again
      setTimeout(() => {
        fetchDataChunk();
      }, waitTimeInMinutes * 60 * 1000); // Convert minutes to milliseconds
    }
  }
}

const readCSVFile = () => {
  fs.createReadStream('./news_tweets.csv')
    .pipe(parse({ delimiter: ',', from_line: 2 }))
    .on("data", (row) => {
      TWEEET_IDS.add(row.id)
      if (MIN_ID === null || Number(row.id) < Number(MIN_ID)) MIN_ID = row.id
    })
    .on("error", (error) => {
      console.log(error)
    })
    .on("end", () => {
      console.log("done")
    })
}

// Start fetching data
// Part 1
// await fetchDataChunk();

// Part 2
readCSVFile()
await fetchRepliesOfTweets()
