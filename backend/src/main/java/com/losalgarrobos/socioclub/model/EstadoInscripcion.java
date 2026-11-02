package com.losalgarrobos.socioclub.model;

/**
 * Estado de una inscripción. La baja es lógica: nunca se borra la fila,
 * solo pasa de ACTIVA a BAJA (RF9).
 */
public enum EstadoInscripcion {
    ACTIVA,
    BAJA
}
