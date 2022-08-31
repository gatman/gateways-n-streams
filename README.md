# Sample WebFlux -> Spring Cloud Stream Request Reply

This sample uses MessagingGateways to make request/response calls to a stream cloud gateway function.
 
- Requires that rabbitmq is running. I'm using the package for ubuntu 20.04
- From root of project: **mvn clean install** 
- From streamy-admin-stream-app: mvn spring-boot:run
- From streamy-admin-rest-app: mvn spring-boot:run
[Swagger UI](http://localhost:8080/swagger-ui)
- From streamy-admin-no-anno-rest-app: mvn spring-boot:run
[Swagger UI](http://localhost:8080/swagger-ui)