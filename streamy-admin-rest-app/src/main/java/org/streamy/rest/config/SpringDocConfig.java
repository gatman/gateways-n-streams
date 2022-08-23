package org.streamy.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocConfig {

    @Value("${org.streamy.title: Sample Flux API}")
    private String apiTitle;

    @Value("${org.streamy.version:1.0.0}")
    private String appVersion;

    @Bean
    public OpenAPI adminRestAPI() {
        return new OpenAPI().info(new Info().title(apiTitle)
                                            .description("A Sample Flux API.")
                                            .version(appVersion)
                                            .contact(new Contact().name(
                                                    "Sample Flux API")
                                                                  .url("https://localhost")
                                                                  .email("gatman1001@gmail.com"))
                                            .termsOfService("The Terms of service.")
                                            .license(new License().name("00 License to kill.")));

    }
}
