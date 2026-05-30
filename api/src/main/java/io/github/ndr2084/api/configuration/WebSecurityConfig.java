package io.github.ndr2084.api.configuration;

import io.github.ndr2084.api.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    private final LoginService loginService;

    WebSecurityConfig(LoginService loginService) {
        this.loginService = loginService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        OidcClientInitiatedLogoutSuccessHandler logoutHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        logoutHandler.setPostLogoutRedirectUri("http://localhost:4200/login");

        // @formatter:off
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(loginService))
                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("http://localhost:4200/hero"))
                        .failureUrl("http://localhost:4200/login?error")
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(logoutHandler)
                );
        // @formatter:on

        return http.build();
    }
}

/*Oauth2User authentication holds 3 things {principal, credentials, authorities}, which are all stored in the SecurityContext for the duration of the session*/
