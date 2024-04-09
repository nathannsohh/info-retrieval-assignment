import { Badge, Box, HStack, Heading, Text, IconButton, Flex, Center, Spacer, Spinner } from "@chakra-ui/react"
import { FaSquareXTwitter } from "react-icons/fa6";
import { CiFaceSmile } from "react-icons/ci";
import { CiFaceFrown } from "react-icons/ci";
import { CiFaceMeh } from "react-icons/ci";
import { IoHeartSharp } from "react-icons/io5";
import { IoMdClose } from "react-icons/io";
import { useEffect, useState } from "react";
import ReplyTweet from "./ReplyTweet";
import axios from "axios";

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

const NewsTweetResult = (props) => {
    const [tweets, setTweets] = useState([])
    const [totalReplies, setTotalReplies] = useState()
    const [negativeReplies, setNegativeReplies] = useState()
    const [positiveReplies, setPositiveReplies] = useState()
    const [neutralReplies, setNeutralReplies] = useState()
    const [sentiment, setSentiment] = useState()
    const [page, setPage] = useState(1)
    const [loading, setLoading] = useState(false)
    const [stillHasTweets, setStillHasTweets] = useState(true)

    useEffect(() => {
        getReplyTweets().then((replies) => {

            setPositiveReplies(Math.round(replies.positive * 10) / 10)
            setNegativeReplies(Math.round(replies.negative * 10) / 10)
            setNeutralReplies(Math.round(replies.neutral * 10) / 10)
            setTotalReplies(Math.round(replies.totalReplyTweets * 10) / 10)
    
            if (replies.positive > replies.negative && replies.positive > replies.neutral) {
                setSentiment(2)
            } else if (replies.negative > replies.positive && replies.negative > replies.neutral) {
                setSentiment(0)
            } else {
                setSentiment(1)
            }
    
            setTweets(replies.replyTweets)
            setStillHasTweets(true)
            setPage(1)
        })
    }, [props.tweetId])

    const getReplyTweets = async () => {
        try {
            const replies = await axios.get("http://localhost:8080/api/reply-tweets/search", {
                params: {
                    replyTo: props.tweetId,
                    start: 0,
                    rows: props.replyCount,
                    sort: "score desc"
                }
            })
            console.log(replies)
            return replies.data
        } catch (e) {
            console.error(e)
        }
    }

    const handleScroll = (e) => {
        const target = e.target

        if (target.scrollHeight - target.scrollTop === target.clientHeight && stillHasTweets) {
            getMoreTweets(page)
            setPage(prevPage => prevPage + 1)
        }
    }

    const getMoreTweets = async (page) => {
        setLoading(true)
        try {
            const replyTweetsData = await axios.get("http://localhost:8080/api/reply-tweets/search", {
            params: {
                replyTo: props.tweetId,
                start: page*10,
                rows: 10,
                sort: "score desc"
            }
            })
            if (replyTweetsData.data.replyTweets.length === 0) {
                setStillHasTweets(false)
            } else {
                setTweets(prevNewsTweets => prevNewsTweets.concat(replyTweetsData.data.replyTweets))
            }
        } catch (e) {
            console.error(e)
        } finally {
            setLoading(false)
        }
    }

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
                    <Text><b>Total Replies:</b> {totalReplies}</Text>
                </HStack>
                <HStack mb={1}>
                    <CiFaceSmile size={25}/>
                    <Text><b>Positive Replies:</b> {positiveReplies}%</Text>
                </HStack>
                <HStack mb={1}>
                    <CiFaceFrown size={25}/>
                    <Text><b>Negative Replies:</b> {negativeReplies}%</Text>
                </HStack>
                <HStack mb={1}>
                    <CiFaceMeh size={25}/>
                    <Text><b>Neutral Replies:</b> {neutralReplies}%</Text>
                </HStack>
                <HStack>
                    <IoHeartSharp size={25}/>
                    <Text><b>Overall Sentiment:</b> <Badge colorScheme={SENTIMENT_COLOUR_MAP[sentiment]}>{SENTIMENT_MAP[sentiment]}</Badge></Text>
                </HStack>
            </Box>
            <Heading mt={6} mb={2} fontSize={25}>Replies</Heading>
            <Box flex={1}>
                <Box borderColor="lightgray" borderRadius={15} borderWidth={1} height={{ base: "400px", xl: "400px", "2xl": "500px" }} overflowY="scroll" onScroll={handleScroll}>
                    {
                        tweets.length === 0 ? 
                        <Center h="100%">
                            <Text>No replies found.</Text>
                        </Center> :
                        <Box h="100%">
                            {
                                tweets.map((tweet, index) => {
                                    return <ReplyTweet tweet={tweet} key={index} />
                                })
                            }
                            {loading && stillHasTweets && <Center><Spinner /></Center>}
                            {!loading && !stillHasTweets && <Center pt={2} pb={2}><Text color="#505357">No more replies.</Text></Center>}
                        </Box>
                    }
                </Box>
            </Box>
        </Flex>
    )
}

export default NewsTweetResult