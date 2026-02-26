package Smart.Assistant.Backend.project.security;

import Smart.Assistant.Backend.project.entity.AppUser;
//import Smart.Assistant.Backend.project.entity.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import Smart.Assistant.Backend.project.repository.UserRepository;

import java.util.Map;
import java.util.Optional;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {

        OAuth2User oAuth2User = super.loadUser(request);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");


        userRepository.getUserByEmail(email).orElseGet(() -> {
            AppUser newUser = AppUser.builder()
                    .email(email)
                    .name(name)
                    .provider("GOOGLE")
                    .role("USER")
                    .pictureUrl(picture)
                    .build();
            return userRepository.save(newUser);
        });

        return oAuth2User;
    }
}
