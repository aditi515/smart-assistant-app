package Smart.Assistant.Backend.project.controller;

import Smart.Assistant.Backend.project.entity.AppUser;
import Smart.Assistant.Backend.project.payload.MeResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/me")
    public MeResponse getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        AppUser user = (AppUser) authentication.getPrincipal(); // If you're returning AppUser in JwtAuthFilter

        return new MeResponse(user.getName(), user.getEmail(),user.getRole(), user.getPictureUrl());

    }
}
