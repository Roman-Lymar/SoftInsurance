package com.springboot.cassandraproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String BASE_PACKAGE_PATH = "com.springboot.cassandraproject.controllers";
    private static final String PATH_SELECTOR_PATTERN = "/api/v0/catalog/**";
    private static final String TITLE = "CATALOG REST API";
    private static final String DESCRIPTION = "SAMPLE REST API FOR SOFTINSURANCE CATALOG";
    private static final String VERSION = "1.0.0";
    private static final String TERMS = "Terms of service";
    private static final String CONTACT_NAME = "Kh-048-Java/Apigee";
    private static final String CONTACT_URL = "http://www.google.com/";
    private static final String EMAIL = "team@dgoogle.com";
    private static final String LICENSE = "Apache v2.0";
    private static final String LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE_PATH))
                .paths(PathSelectors.ant(PATH_SELECTOR_PATTERN))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                TITLE,
                DESCRIPTION,
                VERSION,
                TERMS,
                new Contact(CONTACT_NAME, CONTACT_URL, EMAIL),
                LICENSE,
                LICENSE_URL,
                Collections.emptyList());
    }

}
