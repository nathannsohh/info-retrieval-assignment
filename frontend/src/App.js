import './App.css';
import { Box } from '@chakra-ui/react';
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import MainPage from './routes/MainPage';
import ResultPage from './routes/ResultPage';
import axios from 'axios'

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
    const newsTweets = await axios.get("http://localhost:8080/api/news-tweets/search", {
      params: {
        query: query,
        start: 0,
        rows: 10,
        sort: "score desc"
      }
    })
    return newsTweets.data
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
