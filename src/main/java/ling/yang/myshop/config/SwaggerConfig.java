package ling.yang.myshop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger Config
 *
 * @author yangling
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        String[] paths = { "/**" };
        String[] packagedToMatch = { "ling.yang" };
        return GroupedOpenApi.builder()
                             .group("myshop")
                             .pathsToMatch(paths)
                             .packagesToScan(packagedToMatch)
                             .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        contact.setName("ling.yang");

        return new OpenAPI().info(new Info().title("My Shop")
                                            .description("My Shop API")
                                            .contact(contact)
                                            .version("1.0"));
    }
}
