[![Java CI with Maven](https://github.com/niomwungeri-fabrice/api-rate-limiter/actions/workflows/maven.yml/badge.svg)](https://github.com/niomwungeri-fabrice/api-rate-limiter/actions/workflows/maven.yml)

## API Rate Limiter

## Description

This demo application showcases effective API overload management strategies, tailored for startups with limited server capacity. Utilizing Redis as a temporary storage solution, the application effectively tracks client requests to prevent system overloads. It addresses three critical scenarios:

*Client-Specific Rate Limiting*: Monitors and limits the number of requests from individual clients within a specified time window, ensuring fair resource allocation.

*Monthly Quota Management*: Implements a monthly request quota for each client, preventing excessive usage and maintaining optimal system performance.

*Global Request Throttling*: Oversees the total number of requests across the system, providing a safeguard against overall system overload.

*Flexible Plan Management*: Enables clients to upgrade or change their subscription plans, thus adjusting their daily or monthly request capacity. This feature allows for scalable usage, catering to varying client needs and promoting efficient resource management.

Designed as a practical tool, this application demonstrates a scalable approach to managing API requests, making it an essential resource for startups navigating the challenges of limited server resources and high demand.


## Getting Started

## Dependencies

* Example: Java <=17
* Spring Boot 2.x
* Redis (for rate-limiting features)

## Executing program

Step by step series of examples that tell you how to get a development environment running.

### Clone Repository

```bash
git clone git@github.com:niomwungeri-fabrice/api-rate-limiter.git
cd api-rate-limiter
```

### Run 
How to run the automated tests for this system.

```
./mvnw spring-boot:run
```

## API References

[![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://documenter.getpostman.com/view/11352687/2s9YkuXd3a)

## Running Tests

Explanation how to run the integration tests with examples.

bash

```
./mvnw test
```

## Testing Strategy

Registration Test: Tests if clients are successfully registered.
Upgrade Subscription Test: Tests if clients can upgrade their subscription plan.
Rate Limiting Test: Ensures that rate limiting works correctly under different subscription plans.

## References:
 - [Oltranz](https://www.oltranz.com/)
 - [Irembo](https://irembo.com/)


