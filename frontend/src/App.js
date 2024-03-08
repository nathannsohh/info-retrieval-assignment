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
      articles: [
        {
          source: {
            id: null,
            name: "Autocar"
          },
          author: "Charlie Martin, Will Rimell",
          title: "IM L6: 500-mile electric saloon unveiled at Geneva motor show",
          description: "The IM L6 has an 875V electrical architecture, allowing charge rates up to 396kW\n\n\nMG sibling brand, launching in Europe next year, shows new model packing solid-state batteries\n\nIM Motors has unveiled the new L6 electric saloon at the Geneva motor show, conf…",
          url: "https://www.autocar.co.uk/car-news/new-cars/new-im-l6-chinese-tesla-model-3-rival-500-mile-range",
          urlToImage: "https://www.autocar.co.uk/sites/autocar.co.uk/files/images/car-reviews/first-drives/legacy/lm_6_2.jpg",
          publishedAt: "2024-02-26T10:30:00Z",
          content: "IM Motors has unveiled its new L6 electric saloon at the Geneva motor show and confirmed that it will arrive in Europe early next year.\r\nIM was founded in late 2020 as a tie-up between MG parent firm… [+1657 chars]"
        },
        {
          source: {
            "id": null,
            "name": "Slashdot.org"
          },
          author: "feedfeeder",
          title: "BYD Chairman Proposes Doubling Share Buyback - WSJ - The Wall Street Journal",
          description: "BYD Chairman Proposes Doubling Share Buyback - WSJThe Wall Street Journal BYD shares rise on plans to double $28 mln buyback By Investing.comInvesting.com Fresh from eclipsing Tesla in EV sales, BYD plans raft of new upscale models to grab leading share of lu…",
          url: "https://slashdot.org/firehose.pl?op=view&amp;id=173156440",
          urlToImage: null,
          publishedAt: "2024-02-26T10:12:15Z",
          content: "How many hardware guys does it take to change a light bulb?\r\n\"Well the diagnostics say it's fine buddy, so it's a software problem.\""
        },
        {
          source: {
            "id": null,
            "name": "Theaccidentalpm.com"
          },
          "author": "drjim",
          "title": "Car Product Managers Look To Software To Boost Their Revenue",
          "description": "Car product managers want to incorporate more software into their cars to allow them to be updated over the air in order to add more features to the car\nThe post Car Product Managers Look To Software To Boost Their Revenue appeared first on The Accidental Pro…",
          "url": "http://theaccidentalpm.com/sales-2/car-product-managers-look-to-software-to-boost-their-revenue",
          "urlToImage": "https://i0.wp.com/theaccidentalpm.com/wp-content/uploads/sites/2/2022/06/5-AccPM-8038803690_437ca690f7_c.jpg?fit=800%2C600",
          "publishedAt": "2024-02-26T10:00:00Z",
          "content": "If you're new here, you may want to subscribe to my RSS feed. Thanks for visiting!\r\nThe goal is to sell more cars, perhaps software is the key\r\nImage Credit: rakyan ‘boyan’ tantular\r\nProduct managers… [+7618 chars]"
        },
        {
          source: {
            "id": null,
            "name": "Forbes"
          },
          "author": "Margo T. Oge, Contributor, \n Margo T. Oge, Contributor\n https://www.forbes.com/sites/margooge/",
          "title": "Misleading Ads On EPA Car Emissions Rule Ignore The Real Benefits",
          "description": "Not only is there no \"gasoline car ban,\" but EPA’s proposal would boost the American economy, improve public health, and make the U.S. more competitive globally.",
          "url": "https://www.forbes.com/sites/margooge/2024/02/26/misleading-ads-on-epa-car-emissions-rule-ignore-the-real-benefits/",
          "urlToImage": "https://imageio.forbes.com/specials-images/imageserve/65d93410a0d571f7c15f3ce9/0x0.jpg?format=jpg&height=900&width=1600&fit=bounds",
          "publishedAt": "2024-02-26T10:00:00Z",
          "content": "Electric vs. petrol car, illustration.\r\ngetty\r\nRecently, the American Fuel and Petrochemical Manufacturers Association started running misleading ads in key swing states like Michigan, Wisconsin, and… [+10957 chars]"
        },
        {
          source: {
            "id": "financial-post",
            "name": "Financial Post"
          },
          "author": "Bloomberg News",
          "title": "Li Auto Posts First Full Year in Black But Sees Challenges Ahead",
          "description": "Li Auto Inc. reported revenue for the fourth quarter that beat analyst estimates as deep discounting to fend off rising competition in the Chinese electric vehicle market spurred sales.",
          "url": "https://financialpost.com/pmn/business-pmn/li-auto-posts-first-full-year-in-black-but-sees-challenges-ahead",
          "urlToImage": null,
          "publishedAt": "2024-02-26T09:43:48Z",
          "content": "(Bloomberg) Li Auto Inc. reported revenue for the fourth quarter that beat analyst estimates as deep discounting to fend off rising competition in the Chinese electric vehicle market spurred sales.\r\n… [+2694 chars]"
        },
        {
          source: {
            "id": null,
            "name": "Securityaffairs.com"
          },
          "author": "Pierluigi Paganini",
          "title": "IntelBroker claimed the hack of the Los Angeles International Airport",
          "description": "The popular hacker IntelBroker announced that it had hacked the Los Angeles International Airport by exploiting a flaw in one of its CRM systems. The website Hackread first reported that the popular hacker IntelBroker had breached one of the CRM systems used …",
          "url": "https://securityaffairs.com/159573/hacking/intelbroker-hacked-los-angeles-international-airport.html",
          "urlToImage": "https://securityaffairs.com/wp-content/uploads/2024/02/image-29.png",
          "publishedAt": "2024-02-26T09:38:46Z",
          "content": "US GOV OFFERS A REWARD OF UP TO $15M FOR INFO ON LOCKBIT GANG MEMBERS AND AFFILIATES\r\n | New Redis miner Migo uses novel system weakening techniques\r\n | Critical flaw found in deprecated VMware EAP. … [+47995 chars]"
        },
        {
          source: {
            "id": null,
            "name": "Biztoc.com"
          },
          "author": "aol.com",
          "title": "Malaysia’s prime minister doesn’t want to choose between the U.S. and China: ‘Why must I be tied to one interest?’",
          "description": "It's getting harder for countries to stay neutral between the U.S. and China as relations between the two superpowers get frostier. Washington is trying to remake the global trading system to encourage countries to reduce their reliance on the Chinese economy…",
          "url": "https://biztoc.com/x/52c70049708e5947",
          "urlToImage": "https://c.biztoc.com/p/52c70049708e5947/s.webp",
          "publishedAt": "2024-02-26T09:38:07Z",
          "content": "It's getting harder for countries to stay neutral between the U.S. and China as relations between the two superpowers get frostier. Washington is trying to remake the global trading system to encoura… [+283 chars]"
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
