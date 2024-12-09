package com.bank.publicinfo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiAccountDetails() {
        return new OpenAPI()
                .info(new Info().title("Bank Details API"))
                .servers(List.of(new Server().url("http://localhost:8091/api/public-info")));
    }

    @Bean
    public GroupedOpenApi bankDetailsApi() {
        return GroupedOpenApi.builder()
                .group("Bank Details API")
                .pathsToMatch("/v1/bankDetails/**")
                .build();
    }
}
