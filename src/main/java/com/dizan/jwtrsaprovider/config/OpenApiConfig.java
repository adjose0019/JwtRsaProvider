package com.dizan.jwtrsaprovider.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para personalizar la documentación OpenAPI (Swagger UI) de la aplicación.
 * <p>
 * Define la información principal del API expuesta, incluyendo:
 * <ul>
 *     <li>Título de la API.</li>
 *     <li>Versión.</li>
 *     <li>Descripción.</li>
 *     <li>Datos de contacto.</li>
 * </ul>
 *
 * Esta configuración se utiliza automáticamente por Springdoc OpenAPI para generar la documentación
 * accesible vía Swagger UI.
 *
 * @author Jose Antonio Diaz
 * GitHub adjose0019
 */
@Configuration
public class OpenApiConfig {

    /**
     * Define la configuración personalizada de la especificación OpenAPI.
     *
     * @return una instancia de {@link OpenAPI} con la información básica del API configurada.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OAuth2 JWT Token Provider API")
                        .version("v1.0.0")
                        .description("API para generación de tokens JWT mediante client_credentials grant type")
                        .contact(new Contact()
                                .name("Jose Antonio Diaz")
                                .email("adjose0019@gmail.com")));
    }
}
