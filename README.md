# Flight Planner

The Flight Planner is a straightforward flight planning service implemented using the Java Spring Boot framework and a PostgreSQL database. This API allows users to create, search, and delete flight and airport data, offering two storage options: in-memory storage using Java data structures and a PostgreSQL database storage option.

## Running the API

To run the Flight Planner API, follow these steps:

1. Clone this repository using the ``git clone`` command.
2. Build the project.
3. Run the application.
---
## Configuration options

### Selecting storage option

In the ``src/main/resources/applications.properties`` file, you can set the `version` variable to `database` or `in-memory` to choose your preferred storage option.

```
flightplanner.service.version=database
```

By default, all database tables are dropped and recreated every time the application is launched. If you want to persist data in the database, set `spring.liquibase.drop-first` to `false` in the `application.properties` file.

```
spring.liquibase.drop-first=true
```
---

## Endpoints

The Flight Planner provides a set of endpoints catering to different user roles:

### GET

#### /admin-api/flights/{id}

#### /admin-api/airports

#### /api/flights/{id}

### PUT

#### /admin-api/flights

### POST

#### /testing-api/clear

#### /testing-api/clear

### DELETE

#### /admin-api/flights/{id}


---
