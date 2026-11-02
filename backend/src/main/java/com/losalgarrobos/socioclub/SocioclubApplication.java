package com.losalgarrobos.socioclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación.
 * @SpringBootApplication activa la configuración automática y el escaneo de
 * componentes (@Controller, @Service, @Repository) en este paquete y sus subpaquetes.
 */
@SpringBootApplication
public class SocioclubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocioclubApplication.class, args);
    }

}
