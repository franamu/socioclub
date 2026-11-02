package com.losalgarrobos.socioclub.service;

/**
 * Excepción que lanza la capa de servicio cuando una regla de negocio (RN1 a RN5)
 * rechaza la operación. Lleva el código de la regla para mostrarlo en la vista
 * y, más adelante, devolverlo en el JSON de la API (409 Conflict).
 */
public class ReglaDeNegocioException extends RuntimeException {

    private final String regla;

    public ReglaDeNegocioException(String regla, String mensaje) {
        super(mensaje);
        this.regla = regla;
    }

    public String getRegla() {
        return regla;
    }
}
