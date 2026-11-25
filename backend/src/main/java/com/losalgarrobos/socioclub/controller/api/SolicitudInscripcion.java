package com.losalgarrobos.socioclub.controller.api;

/**
 * Cuerpo de POST /api/inscripciones: { "socioId": 3, "actividadId": 1 }.
 * No se recibe la entidad Inscripcion para que el cliente solo pueda elegir socio y
 * actividad: la fecha y el estado los pone el servidor.
 */
public class SolicitudInscripcion {

    // Long y no long: si el dato no viene en el JSON queda null y se puede detectar
    private Long socioId;
    private Long actividadId;

    public Long getSocioId() {
        return socioId;
    }

    public void setSocioId(Long socioId) {
        this.socioId = socioId;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(Long actividadId) {
        this.actividadId = actividadId;
    }
}
