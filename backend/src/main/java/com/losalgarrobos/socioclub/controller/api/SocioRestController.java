package com.losalgarrobos.socioclub.controller.api;

import com.losalgarrobos.socioclub.model.Inscripcion;
import com.losalgarrobos.socioclub.model.Socio;
import com.losalgarrobos.socioclub.service.ActividadService;
import com.losalgarrobos.socioclub.service.InscripcionService;
import com.losalgarrobos.socioclub.service.SocioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST de socios: ingreso por DNI a la app "Mi Club" y consulta de sus actividades (RF8).
 */
@RestController
@RequestMapping("/api/socios")
public class SocioRestController {

    private final SocioService socioService;
    private final InscripcionService inscripcionService;
    private final ActividadService actividadService;

    public SocioRestController(SocioService socioService,
                               InscripcionService inscripcionService,
                               ActividadService actividadService) {
        this.socioService = socioService;
        this.inscripcionService = inscripcionService;
        this.actividadService = actividadService;
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<Socio> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(socioService.buscarPorDni(dni));
    }

    /** Inscripciones activas del socio, cada una con su actividad embebida (JOIN FETCH del repositorio). */
    @GetMapping("/{id}/inscripciones")
    public ResponseEntity<List<Inscripcion>> inscripciones(@PathVariable Long id) {
        List<Inscripcion> activas = inscripcionService.listarActivasPorSocio(id);
        for (Inscripcion i : activas) {
            actividadService.completarCupo(i.getActividad());
        }
        return ResponseEntity.ok(activas);
    }
}
