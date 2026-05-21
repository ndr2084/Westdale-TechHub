package io.github.ndr2084.api.configuration;

import io.github.ndr2084.api.model.UserEntity;
import io.github.ndr2084.api.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
    private final LoginService loginService;

    WebSecurityConfig(LoginService loginService) {
        this.loginService = loginService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2

                        /*CUSTOM SUCCESS HANDLER BEGINS*/
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                            log.info("OAuth2 login success - user: {}", oauthUser);
                            assert oauthUser != null; UserEntity userEntity = loginService.findOrCreateUser(oauthUser);
                            log.info("findOrCreateUser result: {}", userEntity);
                            new SimpleUrlAuthenticationSuccessHandler("http://localhost:4200/hero")
                                    .onAuthenticationSuccess(request, response, authentication);
                        })
                        /*CUSTOM SUCCESS HANDLER ENDS*/
                        .failureUrl("http://localhost:4200/login?error")
                );
        // @formatter:on

        return http.build();
    }
}

/*Oauth2User authentication holds 3 things {principal, credentials, authorities}, which are all stored in the SecurityContext for the duration of the session*/
