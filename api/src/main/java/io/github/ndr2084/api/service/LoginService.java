package io.github.ndr2084.api.service;

import io.github.ndr2084.api.model.UserEntity;
import io.github.ndr2084.api.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findOrCreateUser(OAuth2User oauthUser) {
        Long githubId = ((Number) oauthUser.getAttribute("id")).longValue();
        return userRepository.findByGithubId(githubId)
                .orElseGet(() -> {
                    UserEntity user = new UserEntity();
                    user.setGithubId(githubId);
                    user.setUsername(oauthUser.getAttribute("login"));
                    user.setEmail(oauthUser.getAttribute("email"));
                    user.setAvatarUrl(oauthUser.getAttribute("avatar_url"));
                    return userRepository.save(user);
                });
    }
}
