package by.aurorasoft.distanceclassifier.config;

import by.aurorasoft.distanceclassifier.util.UrlUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class OpenApiConfig {

    @Value("${app.version}")
    private String appVersion;

    @Bean
    public OpenAPI openApi(@Value("${app.api.url}") String apiUrl,
                           @Value("${server.servlet.context-path}") String baseUrl) {

        var schema = new Schema<LocalDateTime>();
        schema.example(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).type("string");
        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime.class, schema);

        return new OpenAPI()
                .info(new Info()
                        .title("Aurora API mileage classifier documentation")
                        .version(appVersion))
                .addServersItem(new Server().url(UrlUtil.append(apiUrl, baseUrl)))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Api-Key-authorization"))
                .components(new Components()
                        .addSecuritySchemes("Api-Key-authorization", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("apiKey")
                        )
                );
    }
}
