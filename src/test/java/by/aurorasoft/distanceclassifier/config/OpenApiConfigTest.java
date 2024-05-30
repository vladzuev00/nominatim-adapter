package by.aurorasoft.distanceclassifier.config;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static by.aurorasoft.distanceclassifier.util.UrlUtil.append;
import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.APIKEY;
import static org.junit.Assert.assertEquals;

public final class OpenApiConfigTest extends AbstractSpringBootTest {

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.api.url}")
    private String apiUrl;

    @Value("${server.servlet.context-path}")
    private String baseUrl;

    @Autowired
    private OpenAPI openAPI;

    @Test
    public void openApiShouldBeCreated() {
        final OpenAPI expected = new OpenAPI()
                .info(
                        new Info()
                                .title("Aurora API mileage classifier documentation")
                                .version(appVersion)
                )
                .addServersItem(new Server().url(append(apiUrl, baseUrl)))
                .addSecurityItem(new SecurityRequirement().addList("Api-Key-authorization"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "Api-Key-authorization",
                                        new SecurityScheme()
                                                .type(APIKEY)
                                                .in(HEADER)
                                                .name("apiKey")
                                )
                ).paths(new Paths());

        assertEquals(expected, openAPI);
    }
}
