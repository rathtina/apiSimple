package org.springboot.authapi.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Professional Spring Boot API")
                        .version("1.0")
                        .description("This is a professional-grade documentation for testing Spring Boot endpoints.")
                        .contact(new Contact().name("API Support").email("ornrathtina123@gmail.com"))
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}
