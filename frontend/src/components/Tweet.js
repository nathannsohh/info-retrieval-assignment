import { Box, Divider, HStack, Text, Badge } from "@chakra-ui/react"

const Tweet = (props) => {
    return (
        <Box>
            <Box p={3}>
                <Text><b>John Doe</b> • @johndoe</Text>
                <Text>This is an example of a potential tweet talking about Elon Musk or something somewhat related to him and the topic at hand.</Text>
            </Box>
            <HStack pl={3} spacing={4} mb={1}>
                <Text><b>0</b> Retweets</Text>
                <Text><b>0</b> Quotes</Text>
                <Text><b>0</b> Likes</Text>
                <Text><b>0</b> Replies</Text>
                <Badge colorScheme="green">Positive</Badge>
            </HStack>
            <Divider />
        </Box>
    )
}

export default Tweet;