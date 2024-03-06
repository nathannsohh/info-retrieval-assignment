import './App.css';
import { Box } from '@chakra-ui/react';
import MainPage from './components/MainPage';
import { useState } from 'react';

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
  const [firstSearch, setFirstSearch] = useState(true)

  const updateFirstSearch = () => {
    setFirstSearch(false)
  }

  return (
    <Box h="100vh">
      {firstSearch ? <MainPage autoCompleteValues={AUTO_COMPLETE} onFirstSearch={updateFirstSearch}/> :
      <></>}
    </Box>
  );
}

export default App;
