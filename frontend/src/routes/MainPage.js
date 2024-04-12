import { Center, VStack, Image, InputGroup, InputLeftElement, Button, Text, Input } from "@chakra-ui/react"
import { useNavigate } from "react-router-dom";
import { IoIosSearch } from "react-icons/io";
import ELONMUSK from '../main-logo.png'
import { useState } from "react";

const MainPage = (props) => {
    const [inputValue, setInputValue] = useState("")
    const navigate = useNavigate();

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            onSearchHandler(inputValue)
        }
    }

    const onChangeHandler = (e) => {
        setInputValue(e.target.value)
    }

    const onSearchHandler = (value) => {
      if (value !== "") navigate(`/search?query=${value.split(' ').join('+')}`)
    }

   return (
    <Center h="75%">
        <VStack>
          <Image src={ELONMUSK} alt="Elon Musk Image" width="600px" objectFit='contain'/>
            <InputGroup borderColor="#666666" size='lg'>
              <InputLeftElement>
                <IoIosSearch size={23} color="#666666"/>
              </InputLeftElement>
              <Input borderRadius="20px" borderWidth="1px" borderColor="#666666" onKeyDown={handleKeyDown} onChange={onChangeHandler} value={inputValue}/>
            </InputGroup>
          <Button colorScheme='blue' mt={2} fontWeight="450" pl={5} pr={5} borderRadius="18px" onClick={() => onSearchHandler(inputValue)}>Search</Button>
        </VStack>
      </Center>
   ) 
}

export default MainPage