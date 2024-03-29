import { Badge, Box, HStack, Heading, Text, IconButton, Flex, Center, Spacer } from "@chakra-ui/react"
import { FaSquareXTwitter } from "react-icons/fa6";
import { CiFaceSmile } from "react-icons/ci";
import { CiFaceFrown } from "react-icons/ci";
import { CiFaceMeh } from "react-icons/ci";
import { IoHeartSharp } from "react-icons/io5";
import { IoMdClose } from "react-icons/io";
import { useState } from "react";
import Tweet from "./Tweet";


const NewsTweetResult = (props) => {
    const [tweets, setTweets] = useState([1,1,1,1,1,1,1,1,1])
    return (
        <Flex direction={"column"}>
            <Flex>
                <Heading fontSize={30}>News Analysis</Heading>
                <Spacer />
                <IconButton icon={<IoMdClose size={25} />} variant="ghost" onClick={props.handleClose}/>
            </Flex>
            <Box color="#505357" fontSize={18} mt={3}>
                <HStack mb={1}>
                    <FaSquareXTwitter size={25} />
                    <Text><b>Total Replies:</b> 342</Text>
                </HStack>
                <HStack mb={1}>
                    <CiFaceSmile size={25}/>
                    <Text><b>Positive Replies:</b> 51%</Text>
                </HStack>
                <HStack mb={1}>
                    <CiFaceFrown size={25}/>
                    <Text><b>Negative Replies:</b> 48%</Text>
                </HStack>
                <HStack mb={1}>
                    <CiFaceMeh size={25}/>
                    <Text><b>Neutral Replies:</b> 1%</Text>
                </HStack>
                <HStack>
                    <IoHeartSharp size={25}/>
                    <Text><b>Overall Sentiment:</b> <Badge colorScheme="green">Positive</Badge></Text>
                </HStack>
            </Box>
            <Heading mt={6} mb={2} fontSize={25}>Related Tweets</Heading>
            <Box flex={1}>
                <Box borderColor="lightgray" borderRadius={15} borderWidth={1} height={{ base: "400px", xl: "400px", "2xl": "550px" }} overflowY="scroll">
                    {
                        tweets.length === 0 ? 
                        <Center h="100%">
                            <Text>No related tweets found.</Text>
                        </Center> :
                        <Box h="100%">
                            {
                                tweets.map((tweet, index) => {
                                    return <Tweet tweet={tweet} key={index} />
                                })
                            }
                        </Box>
                    }
                </Box>
            </Box>
        </Flex>
    )
}

export default NewsTweetResult