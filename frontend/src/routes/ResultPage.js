import { Box, Divider, HStack, Image, Input, Text, InputGroup, InputLeftElement, Button, Flex, Center, Spinner, Popover, PopoverTrigger, PopoverContent, PopoverArrow, PopoverCloseButton, PopoverHeader, PopoverBody, Spacer, useDisclosure } from "@chakra-ui/react"
import LANDSCAPE_LOGO from '../landscape-logo.png'
import { IoIosSearch } from "react-icons/io";
import { useNavigate, useSearchParams, useLoaderData } from "react-router-dom";
import { useEffect, useState } from "react";
import NewsTweet from "../components/NewsTweet";
import NewsTweetResult from "../components/NewsTweetResult";
import axios from "axios";
import { RangeDatepicker } from "chakra-dayzed-datepicker";

const ResultPage = () => {
    const navigate = useNavigate()

    const { isOpen, onToggle, onClose } = useDisclosure()
    const [page, setPage] = useState(1)
    const [searchParams, setSearchParams] = useSearchParams();
    const query = searchParams.get("query")
    const data = useLoaderData();
    const [queryTime, setQueryTime] = useState(data.queryTime)
    const [totalResults, setTotalResults] = useState(data.totalNewsTweets)
    const [newsTweets, setNewsTweets] = useState(data.newsTweets)
    const [inputValue, setInputValue] = useState(searchParams.get("query"))
    const [selected, setSelected] = useState(null)
    const [loading, setLoading] = useState(false)
    const [stillHasTweets, setStillHasTweets] = useState(true)

    const [relevance, setRelevance] = useState(true)
    const [retweets, setRetweets] = useState(null)
    const [likes, setLikes] = useState(null)
    const [sort, setSort] = useState("score desc")
    const [selectedDates, setSelectedDates] = useState([null, null]);
    const [startDate, setStartDate] = useState(null)
    const [endDate, setEndDate] = useState(null)

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            e.target.blur()
            onSearchHandler(inputValue)
        }
    }

    const onChangeHandler = (e) => {
        setInputValue(e.target.value)
    }

    const onSearchHandler = (value) => {
        if (value !== "") {
            const params = {
                query: value
            }
            setSearchParams(params)
            setInputValue(value)
            setSelected(null)
            setStillHasTweets(true)
            setPage(1)
        }
    }

    const handleSelected = (index) => {
        setSelected(prevSelected => {
            if (prevSelected === index) return null
            else return index
        })
    }

    const closeArticleResult = () => {
        setSelected(null)
    }

    const handleScroll = (e) => {
        const target = e.target

        if (target.scrollHeight - target.scrollTop === target.clientHeight && stillHasTweets) {
            getMoreTweets(page, sort, false, startDate, endDate)
            setPage(prevPage => prevPage + 1)
        }
    }

    const getMoreTweets = async (page, sort, init, startDate, endDate) => {
        setLoading(true)
        try {
            let params;
            if (startDate !== null && endDate !== null) {
                params = {
                    query: searchParams,
                    start: page*10,
                    rows: 10,
                    sort: sort,
                    startDate: startDate,
                    endDate: endDate
                }
            } else {
                params = {
                    query: searchParams,
                    start: page*10,
                    rows: 10,
                    sort: sort
                }
            }
            const newTweetsData = await axios.get("http://localhost:8080/api/news-tweets/search", {
                params: params
            })
            if (newTweetsData.data.newsTweets.length === 0) {
                if (init) {
                    setNewsTweets([])
                    setQueryTime(newTweetsData.data.queryTime)
                    setTotalResults(newTweetsData.data.totalNewsTweets)
                } 
                setStillHasTweets(false)
            } else {
                if (init) {
                    setNewsTweets(newTweetsData.data.newsTweets)
                    setQueryTime(newTweetsData.data.queryTime)
                    setTotalResults(newTweetsData.data.totalNewsTweets)
                } else {
                    setNewsTweets(prevNewsTweets => prevNewsTweets.concat(newTweetsData.data.newsTweets))
                }
            }
        } catch (e) {
            console.error(e)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        if (relevance !== null) {
            if (relevance) {
                setSort("score desc");
            } else {
                setSort("score asc");
            }
        } else if (retweets !== null) {
            if (retweets) {
                setSort("retweetCount desc");
            } else {
                setSort("retweetCount asc");
            }
        } else if (likes !== null) {
            if (likes) {
                setSort("likeCount desc");
            } else {
                setSort("likeCount asc");
            }
        }
    }, [relevance, retweets, likes]);

    useEffect(() => {
        getMoreTweets(0, sort, true, startDate, endDate)
    }, [sort])

    const onRelevanceFilterClick = () => {
        setRetweets(null)
        setLikes(null)
        setPage(1)
        setStillHasTweets(true)

        setRelevance(prevRelevance => {
            if (prevRelevance === null) return true
            else return !prevRelevance
        })
    }

    const onRetweetFilterClick = () => {
        setRelevance(null)
        setLikes(null)
        setPage(1)
        setStillHasTweets(true)

        setRetweets(prevRetweets => {
            if (prevRetweets === null) return true
            else return !prevRetweets
        })
        if (retweets) {
            setSort("retweetCount desc")
        } else {
            setSort("retweetCount asc")
        }
    }

    const onLikesFilterClick = () => {
        setRelevance(null)
        setRetweets(null)
        setPage(1)
        setStillHasTweets(true)

        setLikes(prevLikes => {
            if (prevLikes === null) return true
            else return !prevLikes
        })
        if (likes) {
            setSort("likeCount desc")
        } else {
            setSort("likeCount asc")
        }
    }

    const formatDate = (date) => {
        // Extract year, month, and day from the Date object
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Month is zero-based, so add 1
        const day = String(date.getDate()).padStart(2, '0');
    
        // Concatenate the year, month, and day with hyphens to form the desired format
        return `${year}-${month}-${day}`;
    };

    const handleDateChange = () => {
        if (selectedDates[0] != null) {
            setStartDate(formatDate(selectedDates[0]))
            setEndDate(formatDate(selectedDates[1]))
        }
        onClose()
    }

    useEffect(() => {
        // if (startDate !== null && endDate !== null) {
            getMoreTweets(0, sort, true, startDate, endDate)
            setPage(1)
            setStillHasTweets(true)
        // }
    }, [startDate, endDate])

    const handleDateClear = () => {
        setSelectedDates([null, null])
        setStartDate(null)
        setEndDate(null)
        onClose()
    }

    useEffect(() => {
        setNewsTweets(data.newsTweets)
        setQueryTime(data.queryTime)
        setTotalResults(data.totalNewsTweets)
        setRelevance(true)
        setRetweets(null)
        setLikes(null)
        handleDateClear()
    }, [data, query])

    return (
        <Box width="100%">
            <Box backgroundColor="white" position="fixed" zIndex={1000} width="100%" borderBottom="1px" borderColor="#DDDCDC">
                <HStack p={4} pl={16} width="850px">
                    <Image src={LANDSCAPE_LOGO} width="200px" mr={4} onClick={() => navigate("/")} _hover={{cursor: "pointer"}}/>
                        <InputGroup borderColor="#666666" size='md' w="450px">
                        <InputLeftElement>
                            <IoIosSearch size={23} color="#666666"/>
                        </InputLeftElement>
                        <Input borderRadius="20px" borderWidth="1px" borderColor="#666666" w="450px" onKeyUp={handleKeyDown} onChange={onChangeHandler} value={inputValue}/>
                        </InputGroup>
                    <Button colorScheme='blue' fontWeight="450" pl={5} pr={5} borderRadius="18px">Search</Button>
                </HStack>
            </Box>
            <Box pt="90px">
                <Flex>
                    <Box width="60%" height="89vh" overflowY="scroll" onScroll={handleScroll}>
                        <HStack p={3} pl={16} position="fixed" bg="white" zIndex={999} width="60%" borderBottom="1px" borderRight="1px" borderColor="#DDDCDC">
                            <Text mr={3}>Filter by:</Text>
                            <Button borderRadius={20} colorScheme={relevance === null ? "gray" : "blue"} onClick={onRelevanceFilterClick}>Relevance {relevance === null ? "" : relevance ? "▼" : "▲"}</Button>
                            <Button borderRadius={20} colorScheme={retweets === null ? "gray" : "blue"} onClick={onRetweetFilterClick}>Retweets {retweets === null ? "" : retweets ? "▼" : "▲"}</Button>
                            <Button borderRadius={20} colorScheme={likes === null ? "gray" : "blue"} onClick={onLikesFilterClick}>Likes {likes === null ? "" : likes ? "▼" : "▲"}</Button>
                            <Popover placement='bottom-start' isOpen={isOpen} onClose={onClose}>
                                <PopoverTrigger>
                                    <Button borderRadius={20} onClick={onToggle} colorScheme={startDate === null ? "gray" : "blue"}>Date Range: {startDate != null ? startDate + " - " + endDate : "All time"}</Button>
                                </PopoverTrigger>
                                <PopoverContent>
                                    <PopoverArrow />
                                    <PopoverCloseButton />
                                    <PopoverHeader><Text fontWeight={"semibold"}>Set date range:</Text></PopoverHeader>
                                    <PopoverBody>
                                        <RangeDatepicker
                                            selectedDates={selectedDates}
                                            onDateChange={setSelectedDates}
                                        />
                                        <HStack mt={3}>
                                            <Button onClick={handleDateChange}>Filter</Button>
                                            <Spacer />
                                            <Button colorScheme="red" onClick={handleDateClear}>Clear Dates</Button>
                                        </HStack>
                                    </PopoverBody>
                                </PopoverContent>
                            </Popover>
                        </HStack>
                        <Divider />
                        <Box pt="60px">
                            <Box pl={16} pt={2} pr={4} color="#505357">
                                <Text as={"i"}>Results retrieved in {queryTime}ms • {totalResults} results found</Text>
                            </Box>
                            {newsTweets.length === 0 && 
                            <Box pl={16} pt={4} pr={4}>
                                <Text as='span'>No tweets found for this query. {data.spellingSuggestions !== null && data.spellingSuggestions.length > 0 && <Text as="span">Did you mean: <Button variant="link" onClick={() => onSearchHandler(data.spellingSuggestions[0])}>{data.spellingSuggestions[0]}</Button>?</Text>}</Text>
                            </Box>
                            }
                            {newsTweets.length > 0 && newsTweets[0].id !== null && newsTweets.map((article, index) => <NewsTweet key={index} tweet={article} index={index} handleSelected={handleSelected} isSelected={selected === index}/>)}
                            {loading && <Center><Spinner /></Center>}
                            {!loading && !stillHasTweets && <Center pt={2} pb={2}><Text color="#505357">No more results.</Text></Center>}
                        </Box>
                    </Box>
                    <Box width="40%" p={4} height="89vh">
                        {selected !== null && <NewsTweetResult tweetId={newsTweets[selected].id} replies={newsTweets[selected].replyCount} handleClose={closeArticleResult}/>}
                    </Box>
                </Flex>

            </Box>
        </Box>
    )
}

export default ResultPage