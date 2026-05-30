package io.github.ndr2084.api.service;

import io.github.ndr2084.api.model.UserEntity;
import io.github.ndr2084.api.repository.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRepository userRepository;
    private final OidcUserService delegate = new OidcUserService();

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = delegate.loadUser(userRequest);
        String id = oidcUser.getAttribute("sub");
        userRepository.findById(id).orElseGet(() -> {
            UserEntity user = new UserEntity();
            user.setName(oidcUser.getAttribute("name"));
            user.setId(id);
            user.setUsername(oidcUser.getAttribute("email"));
            user.setEmail(oidcUser.getAttribute("email"));
            user.setAvatarUrl(oidcUser.getAttribute("picture"));
            return userRepository.save(user);
        });
        return oidcUser;
    }
}
