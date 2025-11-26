package Smart.Assistant.Backend.project.controller;


import Smart.Assistant.Backend.project.entity.AppUser;
import Smart.Assistant.Backend.project.repository.UserRepository;
import Smart.Assistant.Backend.project.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public AdminController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/users")
    public List<AppUser> getUsers(HttpServletRequest request) {
        String email = jwtUtil.getEmailFromToken(
                request.getHeader("Authorization").substring(7)
        );

        AppUser user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Access denied: Admin only");
        }
        return userRepository.findAllUsers();
    }

}
