package ai.afrilab.datavault.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@OpenAPIDefinition(
    info = @Info(
        title = "Knowledge base :: API-Docs ~ V1.0",
        description = "AfrilabAI ~ Data Vault API Documentation",
        contact = @Contact(
            name = "Dev Team ~ AfrilabAI",
            email = "joinus@afrilab.ai",
            url = "https://afrilab.ai"
        ),
        version = "1.0.0"
    )
)
@SecurityScheme(
    name = "BearerAuth",
    description = "JWT Auth Required",
    scheme = "bearer",
    type = HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
