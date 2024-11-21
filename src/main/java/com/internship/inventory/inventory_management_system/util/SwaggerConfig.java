package com.internship.inventory.inventory_management_system.util;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Management System")
                        .version("1.0")
                        .description("User, products, supplier and orders management")
                        .contact(new Contact()
                                .name("Manoj Poudel")
                                .email("support@gmail.com")
                        ))
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Local Server")

                ));
    }
}
