package com.losalgarrobos.socioclub.controller.api;

import com.losalgarrobos.socioclub.model.Inscripcion;
import com.losalgarrobos.socioclub.service.ActividadService;
import com.losalgarrobos.socioclub.service.InscripcionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de inscripciones (RF6, RF9).
 * No verifica RN1 a RN5: eso ya lo hace InscripcionService, el mismo que usa la secretaría.
 * Si una regla rechaza la operación, ManejadorErroresApi convierte la excepción en un 409.
 */
@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionRestController {

    private final InscripcionService inscripcionService;
    private final ActividadService actividadService;

    public InscripcionRestController(InscripcionService inscripcionService, ActividadService actividadService) {
        this.inscripcionService = inscripcionService;
        this.actividadService = actividadService;
    }

    @PostMapping
    public ResponseEntity<Inscripcion> inscribir(@RequestBody SolicitudInscripcion solicitud) {
        // No es una regla de negocio: es revisar que la petición esté completa. Sin esto, buscar
        // en la base con un id null terminaría en un error confuso.
        if (solicitud.getSocioId() == null || solicitud.getActividadId() == null) {
            throw new IllegalArgumentException("Debe indicar socioId y actividadId");
        }
        Inscripcion creada = inscripcionService.inscribir(solicitud.getSocioId(), solicitud.getActividadId());
        actividadService.completarCupo(creada.getActividad());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /** Es PUT y no DELETE porque la baja es lógica: la fila queda y solo cambia su estado a BAJA. */
    @PutMapping("/{id}/baja")
    public ResponseEntity<Inscripcion> darDeBaja(@PathVariable Long id) {
        Inscripcion baja = inscripcionService.darDeBaja(id);
        actividadService.completarCupo(baja.getActividad());
        return ResponseEntity.ok(baja);
    }
}
