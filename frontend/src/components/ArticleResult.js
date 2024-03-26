import { Badge, Box, HStack, Heading, Text, IconButton, Flex, VStack, Center } from "@chakra-ui/react"
import { FaSquareXTwitter } from "react-icons/fa6";
import { IoHappyOutline } from "react-icons/io5";
import { IoSadOutline } from "react-icons/io5";
import { IoHeartSharp } from "react-icons/io5";
import { IoMdClose } from "react-icons/io";
import { useState } from "react";
import Tweet from "./Tweet";


const ArticleResult = (props) => {
    const [tweets, setTweets] = useState([1,1,1,1,1,1,1,1,1])
    return (
        <Flex direction={"column"}>
            <Flex>
                <Heading>{props.articleTitle}</Heading>
                <IconButton icon={<IoMdClose size={25} />} variant="ghost" onClick={props.handleClose}/>
            </Flex>
            <Box color="#505357" fontSize={18} mt={3}>
                <HStack>
                    <FaSquareXTwitter size={25} />
                    <Text><b>Total Tweets:</b> 342</Text>
                </HStack>
                <HStack>
                    <IoHappyOutline size={25}/>
                    <Text><b>Positive:</b> 51%</Text>
                </HStack>
                <HStack>
                    <IoSadOutline size={25}/>
                    <Text><b>Negative:</b> 49%</Text>
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

export default ArticleResult