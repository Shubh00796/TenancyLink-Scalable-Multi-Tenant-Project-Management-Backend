package com.Multi.tenant_SaaS_Project_Management_System.Configs;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Multi-tenant SaaS Application API")
                        .version("v1.0.0")
                        .description("API documentation for the Multi-tenant SaaS Application. Covers all tenant-aware and core service endpoints.")
                        .termsOfService("N/A") // You can set a real link later if needed
                        .contact(new Contact()
                                .name("Developer")
                                .email("shubhamdongare9696@gmail.com")) // Use your personal email or leave blank
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Additional Developer Documentation"));
    }
}
