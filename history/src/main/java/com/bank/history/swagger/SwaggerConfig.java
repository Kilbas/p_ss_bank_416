package com.bank.history.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiAccountDetails() {
        return new OpenAPI().servers(List.of(new Server()
                        .url("http://localhost:8088/api/v1/history/")))
                .info(new Info().title("historyDetailsApi"));
    }
}
