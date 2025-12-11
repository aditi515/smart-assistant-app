package Smart.Assistant.Backend.project.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String now() {
        return Instant.now().toString();
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String code, String message, String path, List<FieldErrorDetail> details) {
        ErrorResponse err = new ErrorResponse(status.value(), code, message, now(), path, details);
        return ResponseEntity.status(status).body(err);
    }

    // Validation on DTOs: @Valid -> MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult br = ex.getBindingResult();
        List<FieldErrorDetail> details = new ArrayList<>();
        for (FieldError fe : br.getFieldErrors()) {
            details.add(new FieldErrorDetail(fe.getField(), fe.getDefaultMessage()));
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Invalid request payload", request.getRequestURI(), details);
    }

    // Validation on method params: @Validated -> ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        List<FieldErrorDetail> details = new ArrayList<>();
        for (ConstraintViolation<?> v : violations) {
            String field = v.getPropertyPath() != null ? v.getPropertyPath().toString() : null;
            details.add(new FieldErrorDetail(field, v.getMessage()));
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Invalid request payload", request.getRequestURI(), details);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), request.getRequestURI(), null);
    }

    // Spring Security: authentication failures (401)
    @ExceptionHandler({UnauthorizedException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage() != null ? ex.getMessage() : "Authentication token missing or invalid", request.getRequestURI(), null);
    }

    // Spring Security: access denied (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "FORBIDDEN", "You do not have permission to access this resource", request.getRequestURI(), null);
    }

    // Catch-all fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        // log the exception server-side (stacktrace) — do not expose to clients
        ex.printStackTrace(); // replace with logger in real app
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred", request.getRequestURI(), null);
    }
}
