import './App.css';
import { Box } from '@chakra-ui/react';
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import MainPage from './routes/MainPage';
import ResultPage from './routes/ResultPage';

const AUTO_COMPLETE = [
  "elon musk kid name",
  "elon musk wife",
  "elon musk openai",
  "elon musk tesla",
  "elon musk divorce",
  "elon musk net worth",
  "why elon musk rich"
]

function App() {

  const loader = async ({ request }) => {
    const url = new URL(request.url)
    const query = url.searchParams.get("query")
    console.log(url)
    console.log(query)
    // ADD QUERYING REQUEST HERE

    // Dummy Data
    const data = { 
      tweets: [
        {
          id: "1772931578835939531",
          createdAt: "2024-03-27 10:20:02+00:00",
          fullName: "The New York Times",
          username: "nytimes",
          profileImage: "https://pbs.twimg.com/profile_images/1098244578472280064/gjkVMelR_normal.png",
          fullText: "Elon Musk helped create China’s electric vehicle industry. But he is now facing challenges there as well as scrutiny in the West over his reliance on the country. https://t.co/Lxth1KJUxd",
          replyTo: undefined,
          lang: "en",
          quoteCount: "5",
          retweetCount: "20",
          replyCount: "52",
          likeCount: "98",
          viewCount: "112968"
        },
        {
          id: "1772883760859496833",
          createdAt: "2024-03-27 07:10:01+00:00",
          fullName: "The New York Times",
          username: "nytimes",
          profileImage: "https://pbs.twimg.com/profile_images/1098244578472280064/gjkVMelR_normal.png",
          fullText: "Tesla and China built a symbiotic relationship, with credits, workers and parts that made Elon Musk ultrarich. Now, his reliance on the country may give Beijing leverage. https://t.co/snQASuAT17",
          replyTo: undefined,
          lang: "en",
          quoteCount: "5",
          retweetCount: "26",
          replyCount: "60",
          likeCount: "106",
          viewCount: "99229"
        },
        {
          id: "1772682700656349647",
          createdAt: "2024-03-26 17:51:05+00:00",
          fullName: "Guardian news",
          username: "guardiannews",
          profileImage: "https://pbs.twimg.com/profile_images/1061915596328263680/EcBjYl5z_normal.jpg",
          fullText: "Twitter usage in US ‘fallen by a fifth’ since Elon Musk’s takeover https://t.co/Z9OuFuVNb4",
          replyTo: undefined,
          lang: "en",
          quoteCount: "9",
          retweetCount: "74",
          replyCount: "63",
          likeCount: "171",
          viewCount: "25385"
        },
        {
          id: "1772379383770013931",
          createdAt: "2024-03-25 21:45:48+00:00",
          fullName: "BBC News (World)",
          username: "BBCWorld",
          profileImage: "https://pbs.twimg.com/profile_images/1529107170448523264/q3VwEx38_normal.jpg",
          fullText: "Elon Musk's X anti-hate group case thrown out https://t.co/hAUQ4aLkOf",
          replyTo: undefined,
          lang: "en",
          quoteCount: "21",
          retweetCount: "141",
          replyCount: "94",
          likeCount: "464",
          viewCount: "194627"
        },
        {
          id: "1772324235249176621",
          createdAt: "2024-03-25 18:06:40+00:00",
          fullName: "Financial Times",
          username: "FT",
          profileImage: "https://pbs.twimg.com/profile_images/931156393108885504/EqEMtLhM_normal.jpg",
          fullText: "Lawsuit from Elon Musk’s X against anti-hate speech group dismissed by US judge https://t.co/iHZfej6ZeN",
          replyTo: undefined,
          lang: "en",
          quoteCount: "1",
          retweetCount: "10",
          replyCount: "7",
          likeCount: "29",
          viewCount: "28042"
        },
        {
          id: "1772318812932653227",
          createdAt: "2024-03-25 17:45:07+00:00",
          fullName: "The New York Times",
          username: "nytimes",
          profileImage: "https://pbs.twimg.com/profile_images/1098244578472280064/gjkVMelR_normal.png",
          fullText: "A federal judge in California dismissed X’s lawsuit against a nonprofit organization that studies hate speech online. The ruling is a blow to Elon Musk, who has used legal threats to battle critics of his social media platform. https://t.co/GAYjSYceOZ",
          replyTo: undefined,
          lang: "en",
          quoteCount: "6",
          retweetCount: "53",
          replyCount: "28",
          likeCount: "188",
          viewCount: "106789"
        },
        {
          id: "1770688249121349919",
          createdAt: "2024-03-21 05:45:51+00:00",
          fullName: "The Wall Street Journal",
          username: "WSJ",
          profileImage: "https://pbs.twimg.com/profile_images/971415515754266624/zCX0q9d5_normal.jpg",
          fullText: "Watch: Elon Musk's Neuralink introduced the first patient to receive its brain-computer implant, demonstrating during a livestream that he can now move a computer cursor to play chess using the device https://t.co/SJZHfCWBhX https://t.co/SJZHfCWBhX",
          replyTo: undefined,
          lang: "en",
          quoteCount: "1",
          retweetCount: "10",
          replyCount: "3",
          likeCount: "31",
          viewCount: "42070"
        },
        {
          id: "1770628655494082654",
          createdAt: "2024-03-21 01:49:02+00:00",
          fullName: "Guardian news",
          username: "guardiannews",
          profileImage: "https://pbs.twimg.com/profile_images/1061915596328263680/EcBjYl5z_normal.jpg",
          fullText: "Elon Musk’s Neuralink shows brain-chip patient playing online chess https://t.co/iKY5iwouJk",
          replyTo: undefined,
          lang: "en",
          quoteCount: "0",
          retweetCount: "4",
          replyCount: "8",
          likeCount: "16",
          viewCount: "11002"
        },
      ]
    };
    return new Response(JSON.stringify(data), {
      status: 200,
      headers: {
        "Content-Type": "application/json; utf-8",
      },
    });
  }

  const router = createBrowserRouter([
    {
      path: "/",
      element: <MainPage autoCompleteValues={AUTO_COMPLETE} />,
    },
    {
      path: "/search",
      element: <ResultPage autoCompleteValues={AUTO_COMPLETE} />,
      loader: loader
    }
  ]);

  return (
    <Box h="100vh">
      <RouterProvider router={router} />
    </Box>
  );
}

export default App;
