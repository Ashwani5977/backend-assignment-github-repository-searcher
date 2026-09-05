# GitHub Repository Searcher

A Spring Boot REST API that searches GitHub repositories using the GitHub REST API, stores repository details in PostgreSQL, and provides an API to retrieve stored repositories with optional filtering and sorting.

## Overview

This project implements a backend application for searching GitHub repositories and storing the returned repository information in a PostgreSQL database.

The application provides two REST APIs:

- Search GitHub repositories using repository name, programming language, and sorting criteria.
- Retrieve stored repositories using optional language, minimum stars, and sorting filters.

When a repository already exists in the database, its details are updated instead of creating a duplicate record.

## Features

- Search GitHub repositories by repository name.
- Support partial or full repository name matching.
- Filter GitHub search results by programming language.
- Sort GitHub search results by stars, forks, or updated date.
- Store GitHub repository details in PostgreSQL.
- Use the GitHub repository ID as the primary key.
- Update existing repository records instead of creating duplicates.
- Retrieve stored repositories from PostgreSQL.
- Filter stored repositories by programming language.
- Filter stored repositories by minimum stars.
- Sort stored repositories by stars, forks, or updated date.
- Default sorting by stars in descending order.
- Request validation using Jakarta Validation.
- Centralized exception handling.
- Handle invalid request values.
- Handle GitHub API failures and rate-limit responses.
- Handle empty search results.
- Unit tests using JUnit 5 and Mockito.

## Technologies

| Technology | Purpose |
|------------|---------|
| Java 21 | Programming language |
| Spring Boot 4.1.1 | Application framework |
| Spring MVC | REST API development |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| PostgreSQL | Relational database |
| Maven | Build and dependency management |
| GitHub REST API | External repository search |
| JUnit 5 | Unit testing |
| Mockito | Mocking and unit testing |

## Architecture

The application follows a layered architecture with clear separation of responsibilities.

```text
Client
   |
   v
Controller
   |
   v
Service
   |
   +----------------------+
   |                      |
   v                      v
Repository            GitHub API Client
   |                      |
   v                      v
PostgreSQL            GitHub REST API
Package Structure
src
├── main
│   ├── java
│   │   └── com.ashwani.githubrepositorysearcher
│   │       ├── client
│   │       ├── config
│   │       ├── controller
│   │       ├── dto
│   │       │   ├── external
│   │       │   ├── request
│   │       │   └── response
│   │       ├── entity
│   │       ├── enums
│   │       ├── exception
│   │       ├── repository
│   │       └── service
│   │           └── impl
│   └── resources
│       └── application.properties
│
└── test
    └── java
        └── com.ashwani.githubrepositorysearcher
            ├── client
            ├── controller
            └── service
Database

The application uses PostgreSQL.

Create the database before starting the application:

CREATE DATABASE github_searcher_db;

The application uses the following table:

github_repositories

The stored repository information includes:

Repository ID
Repository name
Description
Owner name
Programming language
Stars count
Forks count
Last updated date

The GitHub repository ID is used as the primary key. This allows an existing repository to be updated instead of creating duplicate records.

Configuration

Database configuration is defined in:

src/main/resources/application.properties

The database password is supplied through the DB_PASSWORD environment variable.

Example configuration:

spring.application.name=github-repository-searcher

spring.datasource.url=jdbc:postgresql://localhost:5432/github_searcher_db
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

Set the PostgreSQL password before starting the application.

Windows Command Prompt
set DB_PASSWORD=your_postgresql_password
Windows PowerShell
$env:DB_PASSWORD="your_postgresql_password"
Running the Application
1. Clone the Repository
git clone https://github.com/Ashwani5977/backend-assignment-github-repository-searcher.git

Move into the project directory:

cd backend-assignment-github-repository-searcher
2. Start PostgreSQL

Make sure PostgreSQL is running and the database github_searcher_db exists.

3. Configure the Database Password

Set the DB_PASSWORD environment variable.

4. Run the Application

Using the Maven Wrapper:

Windows
mvnw.cmd spring-boot:run
Linux / macOS
./mvnw spring-boot:run

The application runs on:

http://localhost:8080
API Documentation

Base URL:

http://localhost:8080

Content type:

application/json
1. Search GitHub Repositories
Endpoint
POST /api/github/search
Description

Searches GitHub repositories using the GitHub repository search API, saves the returned repositories to PostgreSQL, and returns the saved repository details.

Request Body
{
    "query": "spring boot",
    "language": "Java",
    "sort": "stars"
}
Request Parameters
Field	Required	Description
query	Yes	Repository name search text
language	No	Programming language filter
sort	No	Sorting option
Supported Sort Values
stars
forks
updated
Example Request
POST http://localhost:8080/api/github/search
Content-Type: application/json
{
    "query": "spring boot",
    "language": "Java",
    "sort": "stars"
}
Example Response
{
    "message": "Repositories fetched and saved successfully",
    "repositories": [
        {
            "id": 6296790,
            "name": "spring-boot",
            "description": "Spring Boot helps you to create Spring-powered, production-grade applications and services with absolute minimum fuss.",
            "owner": "spring-projects",
            "language": "Java",
            "stars": 81410,
            "forks": 42087,
            "lastUpdated": "2026-09-05T06:00:36Z"
        }
    ]
}

GitHub search requests are sent to:

https://api.github.com/search/repositories
2. Retrieve Stored Repositories
Endpoint
GET /api/github/repositories
Description

Retrieves repository records stored in PostgreSQL.

Optional Request Parameters
Parameter	Required	Description
language	No	Filters repositories by programming language
minStars	No	Filters repositories by minimum star count
sort	No	Sorts repositories by stars, forks, or updated date

When sort is not specified, repositories are sorted by stars in descending order.

Example Request
GET http://localhost:8080/api/github/repositories?language=Java&minStars=100&sort=stars
Example Response
{
    "repositories": [
        {
            "id": 6296790,
            "name": "spring-boot",
            "description": "Spring Boot helps you to create Spring-powered, production-grade applications and services with absolute minimum fuss.",
            "owner": "spring-projects",
            "language": "Java",
            "stars": 81410,
            "forks": 42087,
            "lastUpdated": "2026-09-05T06:00:36Z"
        }
    ]
}
Sorting Examples

Sort by stars:

GET /api/github/repositories?sort=stars

Sort by forks:

GET /api/github/repositories?sort=forks

Sort by updated date:

GET /api/github/repositories?sort=updated
Filtering Examples

Filter by programming language:

GET /api/github/repositories?language=Java

Filter by minimum stars:

GET /api/github/repositories?minStars=10000

Combine multiple filters:

GET /api/github/repositories?language=Java&minStars=10000&sort=stars
Expected Behavior
Existing Repository

If a GitHub repository already exists in the database, the existing record is updated rather than inserting a duplicate.

The repository ID received from GitHub is used to identify the record.

Empty Search Results

When the GitHub search does not return any repositories, the API returns an empty repository list.

Example:

{
    "message": "Repositories fetched and saved successfully",
    "repositories": []
}
Invalid Request

Invalid input is handled through the global exception handler.

For example, an empty query:

{
    "query": "",
    "language": "Java",
    "sort": "stars"
}

returns:

{
    "status": 400,
    "message": "Validation Failed",
    "timestamp": "2026-09-05T16:20:05.4784305",
    "path": "/api/github/search",
    "errors": {
        "query": "Query must not be blank"
    }
}
Invalid Sort Value

An unsupported sort value returns a 400 Bad Request.

Example:

GET /api/github/repositories?sort=wrong

Response:

{
    "status": 400,
    "message": "Invalid value for request parameter: sort",
    "timestamp": "2026-09-05T16:21:07.0353863",
    "path": "/api/github/repositories",
    "errors": {}
}
Error Handling

The application uses a centralized global exception handler to provide consistent error responses.

The application handles:

Validation errors
Invalid request body values
Invalid query parameter values
GitHub API failures
GitHub API rate-limit responses
Unexpected server errors
Empty search results
Testing

The project contains unit tests for the service, controller, and GitHub API client layers.

Service Tests

The service tests cover:

GitHub repository search
Repository mapping
Repository persistence
Default star sorting
Language filtering
Minimum star filtering
Sorting by stars
Sorting by forks
Sorting by updated date
Controller Tests

The controller tests cover:

Successful GitHub repository search
Blank query validation
Invalid POST sort value
Successful stored repository retrieval
Invalid GET sort value
GitHub API Client Tests

The client tests cover:

Client creation
GitHub API exception handling
Exception cause handling
Run Tests
Windows
mvnw.cmd test
Linux / macOS
./mvnw test
Postman Testing

The APIs can be tested using Postman.

Search GitHub Repositories
POST http://localhost:8080/api/github/search

Request body:

{
    "query": "spring boot",
    "language": "Java",
    "sort": "stars"
}
Retrieve Stored Repositories
GET http://localhost:8080/api/github/repositories
Retrieve with Filters
GET http://localhost:8080/api/github/repositories?language=Java&minStars=10000&sort=stars
Test Sorting
GET http://localhost:8080/api/github/repositories?sort=stars
GET http://localhost:8080/api/github/repositories?sort=forks
GET http://localhost:8080/api/github/repositories?sort=updated
Test Validation
POST http://localhost:8080/api/github/search
{
    "query": "",
    "language": "Java",
    "sort": "stars"
}
Test Invalid Sort
GET http://localhost:8080/api/github/repositories?sort=wrong
Requirements

Before running the project, make sure the following are installed:

Java 21
PostgreSQL
Git

The project includes the Maven Wrapper, so Maven does not need to be installed separately.

Author

Ashwani Chaudhary

GitHub:

https://github.com/Ashwani5977
