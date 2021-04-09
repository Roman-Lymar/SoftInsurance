package com.springboot.cassandraproject.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@OpenAPIDefinition
@EnableSwagger2
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicCatalogApi() {
        return GroupedOpenApi.builder()
                .group("CATALOG")
                .pathsToExclude("/clients/**")
                .pathsToMatch("/catalog/**").build();
    }

    @Bean
    public GroupedOpenApi publicClientApi() {
        return GroupedOpenApi.builder()
                .group("CLIENTS")
                .pathsToExclude("/catalog/**")
                .pathsToMatch("/clients/**").build();
    }

    @Bean
    public GroupedOpenApi publicAuthApi() {
        return GroupedOpenApi.builder()
                .group("AUTH")
                .pathsToExclude("/catalog/**")
                .pathsToExclude("/clients/**")
                .pathsToMatch("/users/**").build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("BearerToken", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .description("Enter JWT Bearer token only")
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization")
                        ))
                .info(new Info().title("SOFTINSURANCE REST API")
                        .description("This is a sample Softinsurance server " +
                                "provided by ITA KH-048 Java/Node Apigee team (@since 01.03.2021)")
                        .contact(new Contact()
                                .name("KH-048 Java/Node Apigee")
                                .url("http://www.example.com/support")
                                .email("support@gmail.com"))
                        .version("1.0.0")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
