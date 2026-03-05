# Bookify

## Table of Contents

- [Endpoints](#endpoints)
    - [GET Endpoints](#get-endpoints)
    - [POST Endpoints](#post-endpoints)
    - [DELETE Endpoints](#delete-endpoints)
    - [PUT Endpoints](#put-endpoints)
- [Views](#views)

## Endpoints

Swagger is available at: `/swagger-ui/index.html`

### GET Endpoints

```mermaid
flowchart TD
    client -->|GET /books| server
    client -->|GET /books/100| server
    client -->|GET /books?id=100| server
    client -->|GET /books header 'requestId: 1000001'| server
```

### POST Endpoints

```mermaid
flowchart TD
    client -->|POST /books body JSON| server
```

### DELETE Endpoints

```mermaid
flowchart TD
    client -->|DELETE /books/1| server
    client -->|DELETE /books?id=1| server
```

### PUT Endpoints

`PUT` replaces the entire resource with the data provided in the request.

```mermaid
flowchart TD
    client -->|PUT /books/1 body JSON| server
```

## Views

- homepage: `/home.html`
- books: `/view/books`