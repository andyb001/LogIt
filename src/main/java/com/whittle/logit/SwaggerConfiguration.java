package com.whittle.logit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {


    @Bean
    public Docket api() throws Exception {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(selectors())
                .build()
                .apiInfo(metaData());
    }

    private Predicate<RequestHandler> selectors() {
        return RequestHandlerSelectors.basePackage("com.whittle.logit");
    }

    private ApiInfo metaData() throws Exception {
        String version = "1.0.0";
        return new ApiInfo(
                "Andy Admin REST API",
                "Spring Boot REST API for Andy Admin",
                version,
                "Terms of service",
                new Contact("Andy Whittle", "https://wiki-commerce.lsdg.com/display/~andy.whittle", "andrewbwhittle@gmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0");
    }
}
