package Smart.Assistant.Backend.project.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeResponse {
    private String name;

    private String email;
    String role;
    String pictureUrl;
}
