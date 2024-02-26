import fs from 'fs'
import Rettwit from 'rettiwt-api'
import retry from 'async-retry';

let NUM_OF_TWEETS = 5000

const rettiwt = new Rettwit.Rettiwt({ apiKey: "a2R0PVpmVk1abEsyY0tWd2k2VFg0R1ZCalVzcmdtZEc0cEJxSlVYa2cxTHc7dHdpZD0idT0xNzYwNTE3ODQ5OTkzMzU1MjY0IjtjdDA9YWU3NDk3MjZmOGJmZDQ2YjhkYWZiOTM5OWQ0NmRkZDQ7YXV0aF90b2tlbj1jOGEwYjUwMTRhY2ViOWMyNDU3ZTEzNTU3MzNiNzBhNTIwY2IxZmY4Ow=="});
let csvContent = "id,createdAt,userName,fullText,replyTo,lang,quoteCount,retweetCount,replyCount,retweetCount,likeCount\r\n";
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
        return await rettiwt.tweet.search({
          includeWords: ["elon", "musk"]
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
      let dataRow = [
        row.id,
        row.createdAt,
        escapeCsvField(row.tweetBy.userName),
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

    if (!data.next || NUM_OF_TWEETS <= 0) {
      // If there is no more data or reached the desired number of tweets, write the CSV file
      fs.writeFileSync('my_data.csv', csvContent, 'utf-8');
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

// Start fetching data
fetchDataChunk();