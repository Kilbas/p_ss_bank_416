package com.bank.publicinfo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiPublicInfo() {
        return new OpenAPI()
                .info(new Info().title("Public-info API")
                        .version("1.0")
                        .description("Операции с банковской публичной информацией"))
                .servers(List.of(new Server().url("http://localhost:8091/api/public-info")));
    }
}