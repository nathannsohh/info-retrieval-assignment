import { Badge, Box, HStack, Heading, Text, IconButton, Flex, Center, Spacer } from "@chakra-ui/react"
import { FaSquareXTwitter } from "react-icons/fa6";
import { CiFaceSmile } from "react-icons/ci";
import { CiFaceFrown } from "react-icons/ci";
import { CiFaceMeh } from "react-icons/ci";
import { IoHeartSharp } from "react-icons/io5";
import { IoMdClose } from "react-icons/io";
import { useEffect, useState } from "react";
import ReplyTweet from "./ReplyTweet";

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

const NewsTweetResult = (props) => {
    const [tweets, setTweets] = useState([])
    const [totalReplies, setTotalReplies] = useState()
    const [negativeReplies, setNegativeReplies] = useState()
    const [positiveReplies, setPositiveReplies] = useState()
    const [neutralReplies, setNeutralReplies] = useState()
    const [sentiment, setSentiment] = useState()

    useEffect(() => {
        const DUMMY_TWEETS = [
            {
                id: "1700491517847101689",
                createdAt: "2023-09-09 12:49:06+00:00",
                fullName: "tha_analogist",
                username: "tha_analogist",
                profileImage: "https://pbs.twimg.com/profile_images/1688959957918527510/wBnaed0p_normal.jpg",
                fullText: "@RussianPropOnYT @BBCWorld Imagine someone telling u how to ran ur business",
                replyTo: "BBCWorld",
                lang: "en",
                quoteCount: "0",
                retweetCount: "0",
                replyCount: "0",
                likeCount: "0",
                viewCount: "7",
                sentiment: "-1"
              },
              {
                id: "1700487572500451328",
                createdAt: "2023-09-09 12:33:25+00:00",
                fullName: "Truth, Not Lies",
                username: "Dickson25809487",
                profileImage: "https://pbs.twimg.com/profile_images/1364486824631050240/H25ZeOVe_normal.jpg",
                fullText: "@BBCWorld Never be a dog of America and you will be safe",
                replyTo: "BBCWorld",
                lang: "en",
                quoteCount: "0",
                retweetCount: "0",
                replyCount: "0",
                likeCount: "0",
                viewCount: "3",
                sentiment: "-1"
              },
              {
                id: "1700486611652300994",
                createdAt: "2023-09-09 12:29:36+00:00",
                fullName: "𝔄𝔯𝔳𝔶𝔡𝔞𝔰 𝔊 🇱🇹🤝🇺🇦",
                username: "arnvidhr",
                profileImage: "https://pbs.twimg.com/profile_images/1509928041677529088/AlO1XNX4_normal.jpg",
                fullText: "People in comments asking for help to understand what happening. Delusional believers of possible peace, stopping of this war. They need explaination.",
                replyTo: undefined,
                lang: "en",
                quoteCount: "0",
                retweetCount: "0",
                replyCount: "0",
                likeCount: "0",
                viewCount: "17",
                sentiment: "-1"
              },
              {
                id: "1700479797971779817",
                createdAt: "2023-09-09 12:02:31+00:00",
                fullName: "Jeffrey Drake",
                username: "JeffreyDrake1",
                profileImage: "https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png",
                fullText: "Russia invaded Ukraine with a crow bar and a shotgun, and when Ukraine picks up a stick to defend itself, Musk takes the stick to prevent “escalation.” Trying to save your life is never “escalation.” This is a full on war, not something that might become a war. Arm 🇺🇦 to win!",
                replyTo: undefined,
                lang: "en",
                quoteCount: "0",
                retweetCount: "0",
                replyCount: "0",
                likeCount: "0",
                viewCount: "24",
                sentiment: "0"
              },
              {
                id: "1700474112584343772",
                createdAt: "2023-09-09 11:39:56+00:00",
                fullName: "John M. Bennett",
                username: "jm_bennett007",
                profileImage: "https://pbs.twimg.com/profile_images/1691872988793126912/0FaI4lOi_normal.jpg",
                fullText: "Who is he to determine anything? Elon, you need an ego check.",
                replyTo: undefined,
                lang: "en",
                quoteCount: "0",
                retweetCount: "0",
                replyCount: "0",
                likeCount: "0",
                viewCount: "6",
                sentiment: "-1"
              },
              {
                id: "1700460606334418949",
                createdAt: "2023-09-09 10:46:16+00:00",
                fullName: "Wehosrm Think",
                username: "wehosrm",
                profileImage: "https://pbs.twimg.com/profile_images/1563422898458505216/3JABs4wX_normal.jpg",
                fullText: "@BBCWorld Here come the blue check simos to defend and oligarch  supporting Russia’s oligarchy.",
                replyTo: "BBCWorld",
                lang: "en",
                quoteCount: "0",
                retweetCount: "0",
                replyCount: "0",
                likeCount: "0",
                viewCount: "12",
                sentiment: "1"
              },
              {
                id: "1700456941166747737",
                createdAt: "2023-09-09 10:31:42+00:00",
                fullName: "jeff",
                username: "jeffone23",
                profileImage: "https://pbs.twimg.com/profile_images/1529085449456832513/fHOTLSML_normal.jpg",
                fullText: "@BBCWorld Elon Musk is a businessman and all he has is for the good of man The Ukrainian have gone back on there words so many times If I was him I would stay out of it and not allow my technology to be miss used",
                replyTo: "BBCWorld",
                lang: "en",
                quoteCount: "0",
                retweetCount: "0",
                replyCount: "0",
                likeCount: "0",
                viewCount: "15",
                sentiment: "1"
              },
              {
                id: "1700456600832479709",
                createdAt: "2023-09-09 10:30:21+00:00",
                fullName: "RS",
                username: "RSstaatsburger",
                profileImage: "https://pbs.twimg.com/profile_images/1503507751305191427/FQnWzctx_normal.jpg",
                fullText: "@BBCWorld This man “thinks he is God??!!”",
                replyTo: "BBCWorld",
                lang: "en",
                quoteCount: "0",
                retweetCount: "0",
                replyCount: "0",
                likeCount: "0",
                viewCount: "3",
                sentiment: "-1"
              },
        ]
        
        let positiveTweets = 0
        let neutralTweets = 0
        let negativeTweets = 0

        for (const tweet of DUMMY_TWEETS) {
            switch (tweet.sentiment) {
                case "0":
                    neutralTweets += 1
                    break
                case "1":
                    positiveTweets += 1
                    break
                case "-1":
                    negativeTweets += 1
                    break
                default:
                    break
            }
        }

        setPositiveReplies(Math.round((positiveTweets/DUMMY_TWEETS.length) * 100))
        setNegativeReplies(Math.round((negativeTweets/DUMMY_TWEETS.length) * 100))
        setNeutralReplies(Math.round((neutralTweets/DUMMY_TWEETS.length) * 100))
        setTotalReplies(DUMMY_TWEETS.length)

        if (positiveTweets > negativeTweets && positiveTweets > neutralTweets) {
            setSentiment("1")
        } else if (negativeTweets > positiveTweets && negativeTweets > neutralTweets) {
            setSentiment("-1")
        } else {
            setSentiment("0")
        }

        setTweets(DUMMY_TWEETS)
    }, [])

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
                <Box borderColor="lightgray" borderRadius={15} borderWidth={1} height={{ base: "400px", xl: "400px", "2xl": "550px" }} overflowY="scroll">
                    {
                        tweets.length === 0 ? 
                        <Center h="100%">
                            <Text>No related tweets found.</Text>
                        </Center> :
                        <Box h="100%">
                            {
                                tweets.map((tweet, index) => {
                                    return <ReplyTweet tweet={tweet} key={index} />
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