import { Box, Divider, HStack, Image, Input, Text, InputGroup, InputLeftElement, Button, Flex, Heading } from "@chakra-ui/react"
import LANDSCAPE_LOGO from '../landscape-logo.png'
import { IoIosSearch } from "react-icons/io";
import { useNavigate, useSearchParams, useLoaderData } from "react-router-dom";
import {
    AutoComplete,
    AutoCompleteInput,
    AutoCompleteItem,
    AutoCompleteList,
  } from "@choc-ui/chakra-autocomplete";
import { useState } from "react";
import NewsTweet from "../components/NewsTweet";
import NewsTweetResult from "../components/NewsTweetResult";

const ResultPage = (props) => {
    const navigate = useNavigate()

    const [searchParams, setSearchParams] = useSearchParams();
    const data = useLoaderData();
    const [inputValue, setInputValue] = useState(searchParams.get("query"))
    const [selected, setSelected] = useState(null)

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            e.target.blur()
            onSearchHandler(inputValue)
        }
    }

    const onChangeHandler = (e) => {
        setInputValue(e.target.value)
    }

    const onSearchHandler = (value) => {
        if (value !== "") {
            const params = {
                query: value
            }
            setSearchParams(params)
        }
      }
  
    const onOptionClickHandler = (value) => {
        setInputValue(value)
        onSearchHandler(value)
    }

    const handleSelected = (index) => {
        setSelected(prevSelected => {
            if (prevSelected === index) return null
            else return index
        })
    }

    const closeArticleResult = () => {
        setSelected(null)
    }
    return (
        <Box width="100%">
            <Box backgroundColor="white" position="fixed" zIndex={1000} width="100%" borderBottom="1px" borderColor="#DDDCDC">
                <HStack p={4} pl={16} width="850px">
                    <Image src={LANDSCAPE_LOGO} width="200px" mr={4} onClick={() => navigate("/")} _hover={{cursor: "pointer"}}/>
                    <AutoComplete openOnFocus={false} onSelectOption={(item) => { onOptionClickHandler(item.item.value) }} prefocusFirstItem={false} emptyState={false}>
                        <InputGroup borderColor="#666666" size='md' w="450px">
                        <InputLeftElement>
                            <IoIosSearch size={23} color="#666666"/>
                        </InputLeftElement>
                        <AutoCompleteInput borderRadius="20px" borderWidth="1px" borderColor="#666666" w="450px" onKeyUp={handleKeyDown} onChange={onChangeHandler} value={inputValue}/>
                        </InputGroup>
                        <AutoCompleteList>
                            {props.autoCompleteValues.map((value, index) => <AutoCompleteItem key={index} value={value}>{value}</AutoCompleteItem>)}
                        </AutoCompleteList>
                    </AutoComplete>
                    <Button colorScheme='blue' fontWeight="450" pl={5} pr={5} borderRadius="18px">Search</Button>
                </HStack>
            </Box>
            <Box pt="90px">
                <Flex>
                    <Box width="60%" height="89vh" overflowY="scroll">
                        {data.tweets.map((article, index) => <NewsTweet key={index} tweet={article} index={index} handleSelected={handleSelected} isSelected={selected === index}/>)}
                    </Box>
                    <Box width="40%" p={4} height="89vh">
                        {selected !== null && <NewsTweetResult tweetId={data.tweets[selected].id} handleClose={closeArticleResult}/>}
                    </Box>
                </Flex>

            </Box>
        </Box>
    )
}

export default ResultPage