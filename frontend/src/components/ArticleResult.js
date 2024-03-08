import { Badge, Box, HStack, Heading, Text, IconButton, Flex } from "@chakra-ui/react"
import { FaSquareXTwitter } from "react-icons/fa6";
import { IoHappyOutline } from "react-icons/io5";
import { IoSadOutline } from "react-icons/io5";
import { IoHeartSharp } from "react-icons/io5";
import { IoMdClose } from "react-icons/io";


const ArticleResult = (props) => {
    return (
        <Box>
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
        </Box>
    )
}

export default ArticleResult