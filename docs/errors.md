# Standard Error Response Format

All API errors in this project must follow a predictable JSON schema.  
This ensures consistency across the backend and allows the frontend to reliably parse error responses.

---

## Error JSON Schema

Every error response must contain:

- **status** — HTTP status code (number)
- **code** — internal error code (string)
- **message** — readable message explaining the error
- **timestamp** — ISO timestamp string
- **path** — request path
- **details** — (optional) array of field error objects for validation issues

---

## Examples

### 1) Validation Error (400)

```json
{
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "Invalid request payload",
  "timestamp": "2025-12-05T10:15:30Z",
  "path": "/api/posts",
  "details": [
    { "field": "title", "message": "title is required" },
    { "field": "content", "message": "content is too long" }
  ]
}
```

---

### 2) Unauthorized (401)

```json
{
  "status": 401,
  "code": "UNAUTHORIZED",
  "message": "Authentication token missing or invalid",
  "timestamp": "2025-12-05T10:20:00Z",
  "path": "/api/posts"
}
```

---

### 3) Not Found (404)

```json
{
  "status": 404,
  "code": "NOT_FOUND",
  "message": "Resource not found",
  "timestamp": "2025-12-05T10:25:00Z",
  "path": "/api/posts/999"
}
```

---

### 4) Internal Server Error (500)

```json
{
  "status": 500,
  "code": "INTERNAL_ERROR",
  "message": "An unexpected error occurred",
  "timestamp": "2025-12-05T10:30:00Z",
  "path": "/api/posts"
}
```

---

## Notes
- Validation errors **must include** a `details` array.
- Server errors **must**
