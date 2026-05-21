package io.github.ndr2084.api.controller;

import io.github.ndr2084.api.model.UserEntity;
import io.github.ndr2084.api.service.LoginService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/api/me")
    public UserEntity getCurrentUser(@AuthenticationPrincipal OAuth2User oauthUser) {
        return loginService.findOrCreateUser(oauthUser);
    }
}
