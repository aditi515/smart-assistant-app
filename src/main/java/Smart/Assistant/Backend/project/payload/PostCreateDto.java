package Smart.Assistant.Backend.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateDto {

    @NotBlank(message = "title is required")
    @Size(max = 100, message = "title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "content is required")
    @Size(max = 5000, message = "content must not exceed 5000 characters")
    private String content;
}

