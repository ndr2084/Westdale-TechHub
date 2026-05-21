package io.github.ndr2084.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /*not needed since templates are no longer being used
        * registry.addViewController("/index").setViewName("index");
        * registry.addViewController("/login").setViewName("login");
        */
    }

}
