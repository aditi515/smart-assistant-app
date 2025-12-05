# Security Decisions & Operational Guidelines

This document defines how authentication, authorization, secrets, and related security behaviors work in the Smart Personal Assistant backend. All future security-related code must follow these rules.

---

## 1. Authentication Model (Planned)
We use two authentication methods:
1. **OAuth2 (Google Login)** – User logs in through Google.
2. **JWT (JSON Web Token)** – After Google login, the backend issues a JWT that the client sends with each request.

### Token details:
- Algorithm: HS256 (HMAC using a secret)
- Claims: `sub` (userId), `iat`, `exp`, optional `email` or `roles`
- Token lifetime: 15–60 minutes
- Refresh tokens: Not implemented yet (optional future step)
- Signing secret: `JWT_SECRET` (must come from environment variables)

**In simple words:** Google login gives us the user; JWT lets the user stay signed in for future API calls.

---

## 2. OAuth2 → JWT Flow
1. User signs in with Google.
2. OAuth2 provider returns user info to `CustomOAuth2UserService`.
3. App creates or updates the user in the local database.
4. `CustomOAuth2SuccessHandler` generates a JWT for that user.
5. JWT is returned to the frontend (via cookie or header).
6. Future requests include this JWT and are authenticated by `JwtAuthenticationFilter`.

**Important:**  
The place where JWT is stored (cookie vs Authorization header) must match what `JwtAuthenticationFilter` looks for.

---

## 3. Password Handling
If username/password login is added in the future:
- All passwords must be hashed using **bcrypt** or **argon2**.
- Never store plain text passwords.
- Never log passwords or tokens.

**In simple words:** Store only hashed passwords; never show secrets.

---

## 4. Secrets & Environment Variables
- No secrets can be committed to the repository.
- All secrets must come from `.env` or environment variables.
- `.env.example` lists required variables but contains placeholders.
- In production, use a secrets manager (AWS Secret Manager, etc.).

### Required environment variables:
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `JWT_SECRET`
- `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `GOOGLE_REDIRECT_URI`
- `SERVER_PORT`

---

## 5. Authorization Policy

### Public Endpoints:
- `GET /api/posts`
- `GET /api/posts/{id}`

### Protected Endpoints (require JWT):
- `POST /api/posts`
- `PUT /api/posts/{id}`
- `DELETE /api/posts/{id}`
- Any user-specific endpoints (reminders, notes, todos, etc.)

**In simple words:** Reading is public; modifying anything requires being logged in.

---

## 6. Validation & Error Handling
- All DTOs must use validation annotations.
- Validation failures must return HTTP 400 with the error format in `docs/errors.md`.
- All API errors must follow the schema in `docs/errors.md`.

---

## 7. SQL & DB Safety
- Always use parameterized SQL queries.
- Never build SQL by concatenating user input.
- Avoid automatic JPA relationships; use explicit queries (`@Query`) for joins.

---

## 8. Rate Limiting (Design)
- Public endpoints: max ~60 requests per minute per IP.
- Sensitive endpoints: stricter limits.
- Heavy endpoints (AI, long tasks): authenticated users only.

---

## 9. Logging Guidelines
- Logs must never contain passwords, tokens, or raw OAuth secrets.
- Use structured logs with: timestamp, level, message, requestId, userId.
- Use log levels:
    - `INFO` for normal operations
    - `ERROR` for failures
    - `DEBUG` only for local development

---

## 10. Security Change Log
Whenever a security decision changes, update this document and record the change in `docs/security-changelog.md`.
