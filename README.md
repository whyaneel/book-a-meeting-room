Meeting Room Booking Facility with WebSockets in Spring Boot App

# WebSocket Protocol Setup
## Broker Backed Messaging
- StompEndpoint - `/ws-prototype`
    - StompClient is created with above endpoint, means WebSocket connection is established.
- In Memory MessageBroker
    - Application DestinationPrefixes - `/bot/booking`  
        - Any messages that come to above prefix will be routed to `@MessageMapping(“/message/{EmployeeID}”)` annotated methods
            - @Payload ChatMessage
    - SimpleBroker - `/meetingroom` 
        - `@SendTo("/meetingroom/{EmployeeID}”)` will carry the message (whatever method returns) back to the clients, whoever subscribed to `/meetingroom/{EmployeeID}` 

## WebSocket Event Listeners
- Log when a new client is connected and disconnected
    - Connect
    - Disconnect

# Database and Service
## Database Schema Design
- H2 
- Init Script For Reference Data

## Service Layer Should Do Below
- Available Meeting Rooms
- Available Timings For Selected Meeting Room
- Book Meeting Room
- Release Meeting Room

# Environment Setup
## Dockerizing
- Dockerfile

## Deploy to Elastic BeanStalk
- Simply deploy the jar file or docker image

## Deploy thru AWS CodePipeline
- CodePipeline to pickup from Github





