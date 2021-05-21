# Start with Base Image (Java Runtime)
FROM openjdk:8-jdk-alpine

LABEL maintainer="whyaneel@gmail.com"
VOLUME /tmp
EXPOSE 8080

ARG APP_NAME=book-a-meeting-room
ARG JAR_FILE=build/libs/${APP_NAME}-0.0.1-SNAPSHOT.jar

# Add Jar to Container
ADD ${JAR_FILE} ${APP_NAME}-app.jar

# Run Application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/book-a-meeting-room-app.jar"]