package com.losalgarrobos.socioclub.service;

/**
 * Se lanza cuando se pide un socio, actividad o inscripción que no existe.
 * En la API REST se traducirá a 404 Not Found.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
