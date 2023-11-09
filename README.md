# home-task-app

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements

For building and running the application you need:

- [JDK 1.1](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)
-  Add Plugin Lombok 

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.upwork.hometask.demo.DemoApplication` class from your IDE.

Open an internet editor using  hidden window, call 
```shell
http://localhost:8080/ not https://localhost:8080/
```


* You can check test coverage percentage
```shell
target/site/jacoco/index.html
```

* You can continue your transactions using the interface 
* You can continue using swagger-ui
```shell
http://localhost:8080/swagger-ui/index.html
```
* You can continue using postman

## Example Request and Response
* 
```shell
curl curl -X GET "http://localhost:8080/api/qrCode/listCurrentActivity?qrCode=21b52153-7b48-470d-87bb-f22dd9bf9ed9&studentId=1" -H "accept: application/json"
or
Get Request URL:  http://localhost:8080/api/qrCode/listCurrentActivity?qrCode=21b52153-7b48-470d-87bb-f22dd9bf9ed9&studentId=1
Resp:
{
  "activities": [
    {
      "scheduleId": 5,
      "classroom": "Class 1",
      "lecture": "Lecture (1,22)",
      "startTime": "2023-11-09 22:00:00",
      "endTime": "2023-11-09 22:50:00"
    },
    {
      "scheduleId": 6,
      "classroom": "Class 1",
      "lecture": "Lecture (1,23)",
      "startTime": "2023-11-09 23:00:00",
      "endTime": "2023-11-09 23:50:00"
    }
  ],
  "correlationID": "b01fb40d-b30e-4e3e-9278-4f926531877d"
}
```

* Check In

```shell
curl -X PUT "http://localhost:8080/api/qrCode/checkIn" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"correlationID\": \"7b223a7b-6380-4cae-918e-9d7697d74536\", \"scheduleId\": 4, \"studentId\": 1}"
or
Put Request URL
http://localhost:8080/api/qrCode/checkIn
Req :
{
  "correlationID": "7b223a7b-6380-4cae-918e-9d7697d74536",
  "scheduleId": 4,
  "studentId": 1
}
Resp :
{

}


## Copyright

Written By Abdurrehim Dağdelen Git Hub Address: https://github.com/adagdelen25/upwork