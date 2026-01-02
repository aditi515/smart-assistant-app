package Smart.Assistant.Backend.project.exception;

import java.util.List;

public class ErrorResponse {
    private int status;
    private String code;
    private String message;
    private String timestamp;
    private String path;
    private List<FieldErrorDetail> details;

    public ErrorResponse() {}

    public ErrorResponse(int status, String code, String message, String timestamp, String path, List<FieldErrorDetail> details) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.details = details;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public List<FieldErrorDetail> getDetails() { return details; }
    public void setDetails(List<FieldErrorDetail> details) { this.details = details; }
}

