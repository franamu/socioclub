package com.losalgarrobos.socioclub.model;

/**
 * Estado de la cuota social del socio. Se guarda como texto en la tabla
 * (ver @Enumerated(EnumType.STRING) en Socio) para que sea legible en MySQL.
 */
public enum EstadoCuota {
    AL_DIA,
    MOROSO
}
