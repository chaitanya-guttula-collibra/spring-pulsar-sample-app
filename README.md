# spring-pulsar-sample-app
Spring pulsar sample app to test producer performance between spring pulsar template and native pulsar producer.
The module has two endpoint to send message to pulsar topic using spring pulsar template.
- `/send/exampleMessage` - does not have schema and is a basic string msg.
- `/send/exampleMessageWithSchema` - has schema that produces message of 2KB.

## spring pulsar template module
`app`  is the module that has the spring boot application for spring pulsar template.

## Native pulsar producer module
`native-pulsar-prodcuer` is the module that has the spring boot application for native pulsar producer.
The module has one endpoint (`/send/example`) to send message to pulsar topic using native pulsar producer.


## Gatling module
`gatling-spring-pulsar-test` is the module that has the gatling test to conduct the performance test of message production.

### Steps to perform the test:
- Start a pulsar instance.
- Update the pulsar broker url & authentication token if any in the application.yaml file of the application module(app/native-pulsar-producer).
- Clean & Build the application module(app/native-pulsar-producer). 

    `./gradlew app:build app:clean` or `./gradlew native-pulsar-producer:build native-pulsar-producer:clean`
- Run the application module(app/native-pulsar-producer). 

    `./gradlew app:bootRun` or `./gradlew native-pulsar-producer:bootRun`
- Clean & Build the gatling test module(gatling-spring-pulsar-test).

    `./gradlew gatling-spring-pulsar-test:clean gatling-spring-pulsar-test:build`

- Run the gatling test.

    `./gradlew gatling-spring-pulsar-test:gatlingRun -PrepeatCount=1000 -PrampUpDuration=10 -PuserCount=100 -PbaseUrl=http://localhost:7070 -Pendpoint=/send/example`

    - rampUpDuration - time to ramp up the user count
    - repeatCount - number of times the request should be repeated
    - userCount - number of users to simulate
    - baseUrl - base url of the application module(app/native-pulsar-producer)
    - endpoint - endpoint to test

