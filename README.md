# Bookify

## Table of Contents

- [Endpoints](#endpoints)
    - [GET Endpoints](#get-endpoints)
    - [POST Endpoints](#post-endpoints)
    - [DELETE Endpoints](#delete-endpoints)
    - [PUT Endpoints](#put-endpoints)
    - [PATCH Endpoints](#patch-endpoints)
- [Views](#views)
- [Database](#database)
    - [Entity-Relationship Diagram](#entity-relationship-diagram)

## Endpoints

Swagger is available at: `/swagger-ui/index.html`

### GET Endpoints

```mermaid
flowchart TD
    client -->|GET /books| server
    client -->|GET /books?page=0&size=10&sort=title,desc| server
    client -->|GET /books/100| server
```

```mermaid
flowchart TD
    client --> controller["BookRestController (controller)"]
    controller --> service["BookRetriever (service)"]
    service --> repository["BookRepository (repository)"]
    repository --> database
```

### POST Endpoints

```mermaid
flowchart TD
    client -->|POST /books body JSON| server
```

```mermaid
flowchart TD
    client --> controller["BookRestController (controller)"]
    controller --> service["BookAdder (service)"]
    service --> repository["BookRepository (repository)"]
    repository --> database
```

### DELETE Endpoints

```mermaid
flowchart TD
    client -->|DELETE /books/1| server
    client -->|DELETE /books?id=1| server
```

```mermaid
flowchart TD
    client --> controller["BookRestController (controller)"]
    controller --> service["BookDeleter (service)"]
    service --> repository["BookRepository (repository)"]
    repository --> database
```

### PUT Endpoints

`PUT` replaces the entire resource with the data provided in the request.

```mermaid
flowchart TD
    client -->|PUT /books/1 body JSON| server
```

```mermaid
flowchart TD
    client --> controller["BookRestController (controller)"]
    controller --> service["BookUpdater (service)"]
    service --> repository["BookRepository (repository)"]
    repository --> database
```

### PATCH Endpoints

`PATCH` applies partial updates to a resource, sending only the fields that need to be changed.

```mermaid
flowchart TD
    client -->|PATCH /books/1 body JSON| server
```

```mermaid
flowchart TD
    client --> controller["BookRestController (controller)"]
    controller --> service["BookUpdater (service)"]
    service --> repository["BookRepository (repository)"]
    repository --> database
```

## Views

- homepage: `/home.html`
- books: `/view/books`

## Database

Table `book_authors`:

- book_id
- author_id

### Entity-Relationship Diagram

![ERD](./erd.svg)