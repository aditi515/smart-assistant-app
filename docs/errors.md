# Standard Error Response Format

All API errors in this project must follow a predictable JSON schema.  
This ensures consistency across the backend and allows the frontend to reliably parse error responses.

---

## Error JSON Schema

Every error response must contain:

- **status** — HTTP status code (number)
- **code** — internal error code (string)
- **message** — readable explanation of the error
- **timestamp** — ISO timestamp string
- **path** — request path
- **details** — optional array for validation errors

---

# Exception → HTTP Mapping (Canonical Contract)

Each exception type must map to a predictable HTTP status and internal error code.  
A JSON example is included for each mapping.

---

## 1) Validation Error (400)

**Exception:** `MethodArgumentNotValidException`  
**HTTP Status:** 400  
**code:** `VALIDATION_ERROR`  
**message:** `"Invalid request payload"`  
**details:** field-level validation messages

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

## 2) Unauthorized (401)

**Exception:** `UnauthorizedException` or JWT validation failure  
**HTTP Status:** 401  
**code:** `UNAUTHORIZED`  
**message:** `"Authentication token missing or invalid"`

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

## 3) Forbidden (403)

**Exception:** `ForbiddenException`  
**HTTP Status:** 403  
**code:** `FORBIDDEN`  
**message:** `"You do not have permission to access this resource"`

```json
{
  "status": 403,
  "code": "FORBIDDEN",
  "message": "You do not have permission to access this resource",
  "timestamp": "2025-12-05T10:22:00Z",
  "path": "/api/admin"
}
```

---

## 4) Not Found (404)

**Exception:** `NotFoundException`  
**HTTP Status:** 404  
**code:** `NOT_FOUND`  
**message:** `"Resource not found"`

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

## 5) Conflict (409)

**Exception:** `ConflictException`  
**HTTP Status:** 409  
**code:** `CONFLICT`  
**message:** `"A conflict occurred"`  
(*e.g., email already exists*)

```json
{
  "status": 409,
  "code": "CONFLICT",
  "message": "A conflict occurred",
  "timestamp": "2025-12-05T10:27:00Z",
  "path": "/api/users"
}
```

---

## 6) Internal Server Error (500)

**Exception:** unhandled `Exception`  
**HTTP Status:** 500  
**code:** `INTERNAL_ERROR`  
**message:** `"An unexpected error occurred"`

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

- Validation errors **must** include a `details` array.
- Do **not** expose internal errors, stack traces, or implementation details.
- Authentication/token errors → **401**
- Authorization/permission errors → **403**
- All timestamps must follow ISO-8601 format.

