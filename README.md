# Spring Boot Project

This is a Spring Boot project built with **Maven** as the build tool.

## About

Team management service for football game. 

## Technologies

- **Spring Boot** - The main framework used for building the application.
- **Maven** - The build and dependency management tool.
- **JPA/Hibernate** - For database interaction.
- **H2** - database.
- **JUnit** - unit testing.
- **SpringBootTest** - integration testing.

## Requirements

Before running the application, make sure you have the following installed:

- **Java 21+**
- **Maven** 3.6.0+
- **IDE (Optional)**: IntelliJ IDEA, Eclipse, or VS Code (for development)

## Setup

To get the project up and running locally, follow these steps:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/gouravtiwari489/teamservice.git
    cd teamservice
    ```

3. **Install dependencies**:
    - Make sure all dependencies are correctly downloaded:
    ```bash
    mvn install
    ```

## Running the Project

### Using Maven:

You can run the Spring Boot application using Maven's `spring-boot:run` command:

```bash
mvn spring-boot:run