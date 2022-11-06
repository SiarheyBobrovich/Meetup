FROM openjdk:11-jdk-slim
MAINTAINER Bobrovich

COPY target/meetup-0.0.1-SNAPSHOT.jar meetup.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/meetup.jar"]