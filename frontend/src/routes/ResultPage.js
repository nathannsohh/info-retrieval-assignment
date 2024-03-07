import { Box, Divider, HStack, Image, Input, Text, InputGroup, InputLeftElement, Button } from "@chakra-ui/react"
import LANDSCAPE_LOGO from '../landscape-logo.png'
import { IoIosSearch } from "react-icons/io";
import { useNavigate, useSearchParams } from "react-router-dom";
import {
    AutoComplete,
    AutoCompleteInput,
    AutoCompleteItem,
    AutoCompleteList,
  } from "@choc-ui/chakra-autocomplete";
import { useState } from "react";

const ResultPage = (props) => {
    const navigate = useNavigate()

    const [searchParams, setSearchParams] = useSearchParams();
    const [inputValue, setInputValue] = useState(searchParams.get("query"))

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

    return (
        <Box width="100%">
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
            <Divider />
        </Box>
    )
}

export default ResultPage