package com.api.NubeSandovalAPI.config.app;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Cloud Sandoval API",
                description = "API REST para la gestión de archivos, directorios y almacenamiento en la nube (Cloud Sandoval)",
                version = "2.1",
                contact = @Contact(
                        name = "Jose Sandoval",
                        email = "soporte@cloudsandoval.com"
                ),
                license = @License(
                        name = "Uso Interno",
                        url = "https://cloudsandoval.com/licencia"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor Local",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Servidor Producción",
                        url = "http://100.115.71.64:8080"
                )
        }
)
public class SwaggerConfig {
}
