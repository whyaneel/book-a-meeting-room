# book-a-meeting-room
Meeting Room Booking Facility with WebSockets in Spring Boot App

To enable lombok:

1. File -> Settings -> Build, Execution, Deployment -> Annotation Processors -> Enable Annotation Processing;
2. Intellij IDEA -> Preferences -> Plugins ->Browse Repositories-> Search for "Lombok"-> install plugin -> Apply and restart IDEA

The Application is implemented in DSL integration flow using H2

Application Should be Running at http://localhost:8080/

- Book Meeting Room App
    
    POST http://localhost:8080/api/bot/booking
    Request:
    {
    	"userInfo": {
    		"empId" : "101",
    		"email" : "xx@xxx.com"
    	},
    	"roomInfo": {
    		"roomId" : "01",
    		"timeId" : "101"
    	}
    }
    Response:
    {
        "bookingId": "20191105564329850"
    }
    
- Retrieve Available Rooms Info
    
    GET http://localhost:8080/api/rooms
    
H2 Console:
http://localhost:8080/api/h2/
        