# home-task-app

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements

For building and running the application you need:

- [JDK 1.1](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)
-  Add Plugin Lombok 

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.upwork.hometask.demo.DemoApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

Open an internet editor using  hidden window call http://localhost:8080/ not https://localhost:8080/

* You can continue your transactions using the interface 
* You can continue using swagger-ui
```shell
http://localhost:8080/swagger-ui/index.html
```
* You can continue using postman

## Example Request and Response
* Add Ip Rule
```shell
curl -X POST "http://localhost:8080/api/iprule" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"allow\": true, \"destinationEnd\": \"192.168.2.100\", \"destinationStart\": \"192.168.2.1\", \"sourceEnd\": \"192.168.1.100\", \"sourceStart\": \"192.168.1.1\", \"specificName\": \"Test1\"}"
or
Post Request URL:  http://localhost:8080/api/iprule
Req :
{
  "allow": true,
  "destinationEnd": "192.168.2.100",
  "destinationStart": "192.168.2.1",
  "sourceEnd": "192.168.1.100",
  "sourceStart": "192.168.1.1",
  "specificName": "Test1"
}
Resp:
{
  "data": {
    "id": 174
  },
  "errors": [],
  "timestamp": 1645963853977,
  "transactionId": null
}
```

* Add Subnet Ip Rule
```shell
curl -X POST "http://localhost:8080/api/iprule/subnet" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"allow\": true, \"specificName\": \"subNet\", \"subnetDestination\": \"192.168.1.3/11\", \"subnetSource\": \"192.168.1.2/11\"}"
or
Post Request URL:  http://localhost:8080/api/iprule/subnet
Req :
{
  "allow": true,
  "specificName": "subNet",
  "subnetDestination": "192.168.1.3/11",
  "subnetSource": "192.168.1.2/11"
}
Resp:
{
  "data": {
    "id": 175
  },
  "errors": [],
  "timestamp": 1645964519383,
  "transactionId": null
}
```

* Delete Ip Rule

```shell
curl -X DELETE "http://localhost:8080/api/iprule/174" -H "accept: application/json"
or
Delete Request URL
http://localhost:8080/api/iprule/174
Resp: No Content Response 204
```

* Check Ip Pairs

```shell
curl -X GET "http://localhost:8080/api/iprule/check?destination=192.168.2.10&source=192.168.1.10" -H "accept: application/json"
or
Get Request URL
http://localhost:8080/api/iprule/check?destination=192.168.2.10&source=192.168.1.10
Resp :
{
  "data": true,
  "errors": [],
  "timestamp": 1645964608117,
  "transactionId": null
}
```
* List Ip Rules

```shell
curl -X GET "http://localhost:8080/api/iprule" -H "accept: application/json"
or
Get Request URL
http://localhost:8080/api/iprule
Resp :
{
  "data": {
    "data": [
      {
        "id": 138,
        "specificName": "test1232",
        "sourceStart": "192.200.0.0",
        "sourceStartValue": null,
        "sourceEnd": "192.200.0.0",
        "sourceEndValue": null,
        "destinationStart": "192.200.0.0",
        "destinationStartValue": null,
        "destinationEnd": "192.200.0.0",
        "destinationEndValue": null,
        "allow": true,
        "isSubnet": false,
        "subnetSource": null,
        "subnetDestination": null
      },
      {
        "id": 175,
        "specificName": "subNet",
        "sourceStart": null,
        "sourceStartValue": null,
        "sourceEnd": null,
        "sourceEndValue": null,
        "destinationStart": null,
        "destinationStartValue": null,
        "destinationEnd": null,
        "destinationEndValue": null,
        "allow": true,
        "isSubnet": true,
        "subnetSource": "192.168.1.2/11",
        "subnetDestination": "192.168.1.3/11"
      }
    ],
    "pageNumber": 0,
    "pageSize": 1,
    "totalCount": 2
  },
  "errors": [],
  "timestamp": 1645964704180,
  "transactionId": null
}

## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.