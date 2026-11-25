package com.losalgarrobos.socioclub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS: permite que la app React consuma la API.
 * El navegador considera que localhost:3000 (React) y localhost:8080 (backend) son orígenes
 * distintos porque cambia el puerto. Por seguridad no deja que una página lea respuestas de
 * otro origen, salvo que el servidor lo autorice con la cabecera Access-Control-Allow-Origin.
 * Sin esta clase la petición falla en el navegador, aunque con curl o Postman funcione.
 */
@Configuration
public class ConfiguracionCors implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // solo la API: el backoffice se usa desde su propio origen
                // 3001 es el puerto alternativo de la app cuando el 3000 está ocupado
                .allowedOrigins("http://localhost:3000", "http://localhost:3001")
                // OPTIONS: antes de un PUT, DELETE o POST con JSON el navegador pregunta si puede (preflight)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
