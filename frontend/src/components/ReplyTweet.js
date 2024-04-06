import { Box, Divider, HStack, Text, Badge, Avatar } from "@chakra-ui/react"

const SENTIMENT_MAP = {
    "1": "Positive",
    "-1": "Negative",
    "0": "Neutral"
}

const SENTIMENT_COLOUR_MAP = {
    "1": "green",
    "-1": "red",
    "0": "gray"
}

const ReplyTweet = (props) => {
    return (
        <Box>
            <Box p={3}>
                <HStack>
                    <Avatar src={props.tweet.profileImage} size="sm"/>
                    <Text><b>{props.tweet.fullName}</b> • @{props.tweet.username}</Text>
                </HStack>
                <Text mt={1}>{props.tweet.fullText}</Text>
            </Box>
            <HStack pl={3} spacing={4} mb={1}>
                <Text><b>{props.tweet.retweetCount}</b> Retweets</Text>
                <Text><b>{props.tweet.quoteCount}</b> Quotes</Text>
                <Text><b>{props.tweet.likeCount}</b> Likes</Text>
                <Text><b>{props.tweet.replyCount}</b> Replies</Text>
                <Text><b>{props.tweet.viewCount}</b> Views</Text>
                <Badge colorScheme={SENTIMENT_COLOUR_MAP[props.tweet.sentiment]}>{SENTIMENT_MAP[props.tweet.sentiment]}</Badge>
            </HStack>
            <Divider />
        </Box>
    )
}

export default ReplyTweet;