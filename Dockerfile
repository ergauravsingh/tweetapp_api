FROM openjdk:8-jdk-alpine
COPY ./target/TweetApp.jar TweetApp.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/TweetApp.jar"]