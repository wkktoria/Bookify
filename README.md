# Bookify

## Endpoints

### GET Endpoints

```mermaid
flowchart TD
    client -->|GET /books| server
    client -->|GET /books/100| server
    client -->|GET /books?id=100| server
    client -->|GET /books header 'requestId: 1000001'| server
```