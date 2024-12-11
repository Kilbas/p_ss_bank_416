package com.bank.account.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server.url}")
    private String serverUrl;

    @Value("${swagger.api.description}")
    private String apiDescription;

    @Value("${swagger.api.version}")
    private String apiVersion;

    @Value("${swagger.server.description}")
    private String serversDescription;

    @Value("${swagger.api.title}")
    private String apiTitle;

    @Bean
    public OpenAPI apiAccountDetails() {
        return new OpenAPI()
                .servers(List.of(new Server()
                        .url(serverUrl)
                        .description(serversDescription)))
                .info(new Info()
                        .title(apiTitle)
                        .description(apiDescription)
                        .version(apiVersion));
    }
}
