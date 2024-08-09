# QR Code Scanner Rest API

### Description
QR Scanner is a school application for managing student registration.
This is an application build with Spring Boot. It is a server for a QR code scanner front-end app. It is compatible with
all kind of application that can consume Rest API like: React, PHP, JSP...

### Capabilities
* Register student
* Manage the profile picture (creation, read, update, delete)
* Generate the student's QR Code
* Log in with user in different Role
* Manage the user (if logged with admin account)
* View the log of the operations

## Installation with docker
1. Build the application with Maven
```
mvn clean install
```
After the build process is successful, the app jar file is in the ***target*** folder.

2. Build and run the docker image
```
docker-compose -f compose.yml up --build
```

3. Run the docker service in detached mode
```
docker-compose -f compose.yml up -d
```

### Usage
Access the API in Postman and start your apps. 😝