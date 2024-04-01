import { Avatar, Box, Center, Divider, HStack, Image, Link, Text, Tooltip } from "@chakra-ui/react"
import { useState, useEffect } from "react";

const NewsTweet = (props) => {

    const [hovering, setHovering] = useState(false)
    const [articleLink, setArticleLink] = useState("")
    const [fullText, setFullText] = useState("")

    const formatCreatedAt = (createdAt) => {
        const months = ["January","February","March","April","May","June","July","August","September","October","November","December"];
        const dateObj = new Date(createdAt);
        const hours = dateObj.getHours();
        const minutes = dateObj.getMinutes();
        const ampm = hours >= 12 ? 'PM' : 'AM';
        const formattedHours = hours % 12 === 0 ? 12 : hours % 12;
        const formattedMinutes = minutes < 10 ? '0' + minutes : minutes;
        const month = months[dateObj.getMonth()];
        const day = dateObj.getDate();
        const year = dateObj.getFullYear();
        return `${formattedHours}:${formattedMinutes} ${ampm} • ${month} ${day}, ${year}`;
      }

      const formatCount = (countString) => {
        const count = parseInt(countString);
        if (count >= 1000000000) {
          return (count / 1000000000).toFixed(1) + 'B';
        } else if (count >= 1000000) {
          return (count / 1000000).toFixed(1) + 'M';
        } else if (count >= 1000) {
          return (count / 1000).toFixed(1) + 'K';
        } else {
          return count.toString();
        }
      }

    const formatContentString = (fullText) => { 
        const linkRegex = /(https?:\/\/[^\s]+)/;
        const match = fullText.match(linkRegex);
        if (match) {
            const link = match[0];
            setArticleLink(link)
            setFullText(fullText.replace(link, '').trim())
            return;
        }
        setFullText(fullText)
    }

    useEffect(() => {
        formatContentString(props.tweet.fullText) 
    }, [])

    const onMouseEnterHandler = () => {
        setHovering(true)
    }

    const onMouseLeaveHandler = () => {
        setHovering(false)
    }

    return (
        <Box onMouseEnter={onMouseEnterHandler} onMouseLeave={onMouseLeaveHandler} _hover={{cursor: "pointer"}} pl={16} pt={4} pr={4} onClick={() => {props.handleSelected(props.index)}} bg={props.isSelected ? "#E7E7E7" : hovering ? "#F7F7F7" : "" }>
            <HStack>
                <Avatar src={props.tweet.profileImage} mr={2}/>
                <Box>
                    <Text fontWeight="bold">{props.tweet.fullName}</Text>
                    <Text color="#616566">@{props.tweet.username}</Text>
                </Box>
            </HStack>
            <Text mt={3} mb={3} fontSize={17} color="black">{fullText} <Link href={articleLink} isExternal color='blue.700'>{articleLink}</Link></Text>
            <Text as="span" fontWeight={500} color="#616566">{formatCreatedAt(props.tweet.createdAt)} • <Text as="span" color="black" fontWeight="bold">{formatCount(props.tweet.viewCount)}</Text> Views</Text>
            <HStack mt={3} spacing={5} fontWeight={500} color="#616566" fontSize={16} mb={3}>
                <Text as="span"><Text as="span" fontWeight="bold" color="black">{formatCount(props.tweet.retweetCount)}</Text> Retweets</Text>
                <Text as="span"><Text as="span" fontWeight="bold" color="black">{formatCount(props.tweet.quoteCount)}</Text> Quotes</Text>
                <Text as="span"><Text as="span" fontWeight="bold" color="black">{formatCount(props.tweet.likeCount)}</Text> Likes</Text>
                <Text as="span"><Text as="span" fontWeight="bold" color="black">{formatCount(props.tweet.replyCount)}</Text> Replies</Text>
                {hovering && <Text color="#505357" fontSize={14}  ml={3}>View replies and sentiment of news ▶</Text>}
            </HStack>
            <Divider />
        </Box>
    )
}

export default NewsTweet