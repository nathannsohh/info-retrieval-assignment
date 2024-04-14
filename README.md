# Elon Search

## Dependencies
Make sure that the following dependencies are installed on your system before following the steps below:
- NodeJS - [Installation Link](https://nodejs.org/en/download)
- Solr 9.5.0 - [Installation Link](https://solr.apache.org/downloads.html)
- Java JDK 17 & above - [Installation Link](https://www.oracle.com/sg/java/technologies/downloads/)
- Maven - [Installation Link](https://maven.apache.org/download.cgi)

## Getting Started

### Solr Instance
To run the Solr instance:
1. Navigate to the `/solr-9.5.0` directory on your machine.
```
$ cd path/to/solr/solr-9.5.0
```
2. Copy the `news_tweet`, `reply_tweet` folders and the `solr.xml` file in the repository's `Solr` directory and paste them into your `solr-9.5.9/server/solr/` directory.

2. Run the Solr instance by running the following command:
```
$ bin/solr start
```

You should see a start-up message after start the Solr instance.

### Backend
To start the backend server, the Solr instance has to be running. The steps are:
1. Navigate to the `Backend` directory in the repository
```
$ cd path/to/repository/info-retrieval-assignment/Backend
```
2. Compile the project with Maven
```
$ mvn clean package
```
3. Run the Java Springboot application:
```
$ java -jar target/backend-0.0.1-SNAPSHOT.jar
```
4. Wait for the message "Reply Tweets committed" before making any API calls. The server uses port 8080.

### Frontend
To start the frontend:
1. Navigate to the `frontend` directory
```
$ cd path/to/repository/info-retrieval-assignment/frontend
```
2. Install frontend dependencies
```
$ npm i
```
3. Run the application
```
$ npm start
```

Happy Searching!