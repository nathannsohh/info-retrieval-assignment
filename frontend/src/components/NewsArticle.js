import { Box, Center, HStack, Image, Text, Tooltip } from "@chakra-ui/react"
import { FaSourcetree } from "react-icons/fa";
import { useState } from "react";

const NewsArticle = (props) => {

    const [hovering, setHovering] = useState(false)

    const formatDateString = (dateString) => {
        const currentDate = new Date();
        const inputDate = new Date(dateString);

        const timeDifference = currentDate - inputDate;
        const seconds = Math.floor(timeDifference / 1000);
        const minutes = Math.floor(seconds / 60);
        const hours = Math.floor(minutes / 60);
        const days = Math.floor(hours / 24);
        const months = Math.floor(days / 30.44);
        const years = Math.floor(months / 12);

        if (years > 0) {
            return `${years} ${years === 1 ? 'year' : 'years'} ago`;
        } else if (months > 0) {
            return `${months} ${months === 1 ? 'month' : 'months'} ago`;
        } else if (days > 0) {
            return `${days} ${days === 1 ? 'day' : 'days'} ago`;
        } else if (hours > 0) {
            return `${hours} ${hours === 1 ? 'hour' : 'hours'} ago`;
        } else if (minutes > 0) {
            return `${minutes} ${minutes === 1 ? 'minute' : 'minutes'} ago`;
        } else {
            return `${seconds} ${seconds === 1 ? 'second' : 'seconds'} ago`;
        }
    }

    const formatContentString = (contentString) => { 
        return contentString.replace(/\[.*?\]/g, '')
    }

    const onMouseEnterHandler = () => {
        setHovering(true)
    }

    const onMouseLeaveHandler = () => {
        setHovering(false)
    }

    const titleOnClickHandler = () => {
        window.location.href = props.article.url
    }

    return (
        <Box mb={6} onMouseEnter={onMouseEnterHandler} onMouseLeave={onMouseLeaveHandler} _hover={{cursor: "pointer"}}>
            <HStack>
                <Box width={props.article.urlToImage ? "80%" : "100%"}>
                    <Tooltip label={`Navigate to ${props.article.url}`} openDelay={500} placement='bottom-start'>
                        <Box onClick={titleOnClickHandler}>
                            <HStack mb={1}>
                                <FaSourcetree />
                                <Text color="#27282A" fontSize={13} fontWeight="400">{props.article.source.name}</Text>
                            </HStack>
                            <Text color="#180EA4" fontSize={18} as={hovering ? "u" : null}>{props.article.title}</Text>
                        </Box>
                    </Tooltip>
                    <Box onClick={() => props.handleSelected(props.index)}>
                        <Text color="#505357" fontSize={14} fontWeight="450" mb={2}>{formatContentString(props.article.content)}</Text>
                        <HStack>
                            <Text color="#505357" fontSize={14}>{formatDateString(props.article.publishedAt)}</Text> 
                            {hovering && <Text color="#505357" fontSize={14}  ml={3}>View related tweets and sentiment of news ▶</Text>}
                        </HStack>
                    </Box>
                </Box>
                {props.article.urlToImage && <Center width="20%">
                    <Image src={props.article.urlToImage} boxSize="60%" objectFit="cover" borderRadius="15px"/>
                </Center>}
            </HStack>
        </Box>
    )
}

export default NewsArticle