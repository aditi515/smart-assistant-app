package Smart.Assistant.Backend.project.payload;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NoteRequest {
    @Size(max = 5000, message = "content must not exceed 5000 characters")
    private String content;
}
