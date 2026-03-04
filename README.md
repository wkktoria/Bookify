# Bookify

## Table of Contents

- [Endpoints](#endpoints)
    - [GET Endpoints](#get-endpoints)
    - [POST Endpoints](#post-endpoints)
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
    client -->|POST /books| server
```

## Views

- homepage: `/home.html`
- books: `/view/books`