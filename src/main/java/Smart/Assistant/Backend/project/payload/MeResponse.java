package Smart.Assistant.Backend.project.payload;


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
