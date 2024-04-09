import { Box, Divider, HStack, Text, Badge, Avatar } from "@chakra-ui/react"

const SENTIMENT_MAP = {
    2: "Positive",
    0: "Negative",
    1: "Neutral"
}

const SENTIMENT_COLOUR_MAP = {
    2: "green",
    0: "red",
    1: "gray"
}

const EMOTION_COLOUR_MAP = {
    "shocked": "gray",
    "joy": "yellow",
    "disgust": "teal"
}

const ReplyTweet = (props) => {
    return (
        <Box>
            <Box p={3}>
                <HStack>
                    <Avatar src={props.tweet.profileImage} size="sm"/>
                    <Text><b>{props.tweet.fullName}</b> • @{props.tweet.userName}</Text>
                </HStack>
                <Text mt={1}>{props.tweet.fullText}</Text>
            </Box>
            <HStack pl={3} spacing={4} mb={1}>
                <Text><b>{props.tweet.retweetCount}</b> Retweets</Text>
                <Text><b>{props.tweet.quoteCount}</b> Quotes</Text>
                <Text><b>{props.tweet.likeCount}</b> Likes</Text>
                <Text><b>{props.tweet.replyCount}</b> Replies</Text>
                <Text><b>{props.tweet.viewCount}</b> Views</Text>
            </HStack>
            <HStack pl={3} spacing={4} mb={2} mt={2}>
                <Badge colorScheme={SENTIMENT_COLOUR_MAP[props.tweet.sentiment]}>{SENTIMENT_MAP[props.tweet.sentiment]}</Badge>
                {props.tweet.sarcasm === 1 && <Badge colorScheme="yellow">Sarcastic</Badge>}
                <Badge colorScheme={EMOTION_COLOUR_MAP[props.tweet.sentimentDetail]}>{props.tweet.sentimentDetail}</Badge>
            </HStack>
            <Divider />
        </Box>
    )
}

export default ReplyTweet;