import { Box, Divider, HStack, Image, Input, Text, InputGroup, InputLeftElement, Button } from "@chakra-ui/react"
import LANDSCAPE_LOGO from '../landscape-logo.png'
import { IoIosSearch } from "react-icons/io";
import { useNavigate } from "react-router-dom";
import {
    AutoComplete,
    AutoCompleteInput,
    AutoCompleteItem,
    AutoCompleteList,
  } from "@choc-ui/chakra-autocomplete";

const ResultPage = (props) => {
    const navigate = useNavigate()

    return (
        <Box width="100%">
            <HStack p={4} pl={16} width="850px">
                <Image src={LANDSCAPE_LOGO} width="200px" mr={4} onClick={() => navigate("/")} _hover={{cursor: "pointer"}}/>
                <AutoComplete openOnFocus>
                    <InputGroup borderColor="#666666" size='md' w="450px">
                    <InputLeftElement>
                        <IoIosSearch size={23} color="#666666"/>
                    </InputLeftElement>
                    <AutoCompleteInput borderRadius="20px" borderWidth="1px" borderColor="#666666" w="450px"/>
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