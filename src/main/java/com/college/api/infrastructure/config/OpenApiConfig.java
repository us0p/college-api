package com.college.api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("College Institution API")
                        .description("REST API for college institution management. " +
                                "Handles users, roles, permission objects, posts, and AI-powered document storage with vector embeddings.")
                        .version("1.0.0"));
    }
}
