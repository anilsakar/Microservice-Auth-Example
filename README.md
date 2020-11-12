# Microservice-Auth-Example

It is a RESTful microservice example which was written in Java Spring Boot. With this users can signup and signin. It uses [Java Web Tokens(JWT)](https://jwt.io/). When user login to the system. Service returns [Java Web Tokens(JWT)](https://jwt.io/) to the front end. Also with [Amazon S3 Bucket](https://aws.amazon.com/s3/) users can update their user profiles. In the database part after updating the picture to the [Amazon S3 Bucket](https://aws.amazon.com/s3/) it returns the image URL and saves in the database. If a user creates an account mail will be sent to their sign up mail. After activating the account user can enter its account and receive a token.

If people want to use this RESTful service in local config they need to add their properties. For instance, for their mail and password, for [Amazon S3 Bucket](https://aws.amazon.com/s3/) they need bucket secret keys etc. If people don't want to use local properties they change it to their github account to fetch application properties from github. Because service uses [Spring Cloud Bus](https://cloud.spring.io/spring-cloud-bus/reference/html/) people don't need to shut down services if they want to change something in application properties. They can change it locally or in their github and use bus-refresh to apply those changes online.

Technologies used in this RESTful service:

[Java Web Tokens(JWT)](https://jwt.io/)  
[Amazon S3 Bucket](https://aws.amazon.com/s3/)  
[RabbitMQ](https://www.rabbitmq.com/)  
[Spring Mail](https://www.baeldung.com/spring-email) 
[Spring Boot Microservices](https://spring.io/microservices)  
[Spring Cloud Bus](https://cloud.spring.io/spring-cloud-bus/reference/html/) 
[What is Netflix Eureka](https://dzone.com/articles/netflix-eureka-discovery-microservice#:~:text=What%20Is%20Netflix%20Eureka%3F,Clients%20are%20the%20independent%20microservices.)  
[What is Zuul API Gateway](https://dzone.com/articles/microservices-communication-zuul-api-gateway-1)  





