## Problem Statement	

1. Data should be persisted through process restarts (stored on disk).  
2. The application should support PUT and GET APIs. While the data does not need to be exposed over a network, this is a language agnostic example to illustrate how the application could be called:
   - `curl -X PUT http://localhost:8080 -H 'Content-Type: application/json' -d '{"key": "mykey", "value": "myvalue", "timestamp" : 1673524092123456}'`
   - `curl -X GET http://localhost:8080 -H 'Content-Type: application/json' -d '{"key":"mykey", "timestamp": 1673524092123456}'` # returns "myvalue"
3. Lookups should consider the key-timestamp combination.  
   For example, given the stored sequence:  
   `[{key: "mykey", timestamp: 100, value: "value1"}, {key: "mykey", timestamp: 101, value: "value2"}]`  
   - A request for "mykey" at timestamp 99 should return nothing  
   - A request at timestamp 100 should return "value1"  
   - A request at timestamp 101 or higher should return "value2"
4. Your solution should have defined outcomes for concurrent API calls.  
5. Document how to build and run your solution from the command line. For example: `"go run main.go"`


## Solution Overview

Techstack:

	1.Java 17
	2.H2 DB

Framework:

	1.Spring Boot
	
Libraries (apart from Spring libraries):

	1.Lombok : For automatic Getter-Setter creation.
	
Description:

1. The application exposes two endpoints, GET and PUT to exchange data with calling clients.
2. The data is persisted in a file based H2 DB to persist the data after application restart.The db file is presented in root of application.
3. Following concurrency issues are assumed and handled:
	- For GET API, the DB transaction is annonated with @Read-Only property which makes sure, even though same record is being updated via PUT operation, the GET would fetch latest saved copy.
	- For PUT API, if two concurrent calls are triggered from different client, data will be saved.

## How to build/run App

1. Clone repo in local folder:
   https://github.com/samkiitjain/Code-Challenge

2. Open cmd/bash
3. Change directory to go in the root directory of project. Tip: you should see pom.xml file.
4. Build command :
	mvn clean package
5. run command:
   mvn spring-boot:run



