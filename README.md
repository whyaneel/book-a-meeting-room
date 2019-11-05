# Meeting Room Booking Facility with WebSockets in Spring Boot App

## WebSocket Protocol Setup
### Broker Backed Messaging
- StompEndpoint - `/ws-prototype`
    - StompClient is created with above endpoint, means WebSocket connection is established.
- In Memory MessageBroker
    - Application DestinationPrefixes - `/bot/booking`  
        - Any messages that come to above prefix will be routed to `@MessageMapping(“/message/{uuid}”)` annotated methods
            - @Payload ChatMessage
    - SimpleBroker - `/meetingroom` 
        - `@SendTo("/meetingroom/{uuid}”)` will carry the message (whatever method returns) back to the clients, whoever subscribed to `/meetingroom/{uuid}` 

### WebSocket Event Listeners
- Log when a new client is connected and disconnected
    - Connect
    - Disconnect

## Database and Service
### Database Schema Design
- H2 
- Init Script For Reference Data

### Service Layer Should Do Below
- Available Meeting Rooms
- Available Timings For Selected Meeting Room
- Book Meeting Room
- Release Meeting Room

## Environment Setup
### Dockerizing (From Local Machine)
- Create a Bootable Jar
```
cd book-a-meeting-room
./gradlew clean build
```

- Create a Docker Image (from Dockerfile)
```
docker build --no-cache -t book-a-meeting-room-app .
```

- Run The Application
```
docker run -p 9000:8080 book-a-meeting-room-app
```
Application Should be Running at http://localhost:9000/


### Deploy to Elastic BeanStalk
- Simply deploy the jar file or docker image

### Deploy thru AWS CodePipeline
- CodePipeline to pickup from Github





