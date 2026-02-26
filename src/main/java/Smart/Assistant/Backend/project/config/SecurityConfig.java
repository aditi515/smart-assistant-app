package Smart.Assistant.Backend.project.config;


import Smart.Assistant.Backend.project.repository.UserRepository;
import Smart.Assistant.Backend.project.security.CustomOAuth2SuccessHandler;
import Smart.Assistant.Backend.project.security.JwtAuthenticationFilter;
import Smart.Assistant.Backend.project.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import Smart.Assistant.Backend.project.security.CustomOAuth2UserService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,JwtUtil jwtUtil, UserRepository userRepository) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // your app won’t check if requests are coming from trusted sources
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers( "/login**", "/oauth2/**", "/__test/**").permitAll()//this endpoint does NOT require authentication
                                .anyRequest().authenticated() //For every other request, require authentication.
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(user -> user
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(new CustomOAuth2SuccessHandler( jwtUtil))
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
