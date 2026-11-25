package com.losalgarrobos.socioclub.controller.api;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.service.ActividadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST de actividades.
 * @RestController = @Controller + @ResponseBody: lo que devuelve cada método no es el nombre
 * de una vista Thymeleaf, sino un objeto que Jackson convierte a JSON.
 * Usa el mismo servicio que el backoffice, así las validaciones no se escriben dos veces.
 */
@RestController
@RequestMapping("/api/actividades")
public class ActividadRestController {

    private final ActividadService actividadService;

    public ActividadRestController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    /** GET /api/actividades o /api/actividades?disciplina=Yoga. Sin el parámetro devuelve todas. */
    @GetMapping
    public ResponseEntity<List<Actividad>> listar(@RequestParam(required = false) String disciplina) {
        return ResponseEntity.ok(actividadService.listarConCupo(disciplina));
    }

    /** Si no existe, el servicio lanza RecursoNoEncontradoException y ManejadorErroresApi responde 404. */
    @GetMapping("/{id}")
    public ResponseEntity<Actividad> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(actividadService.buscarConCupo(id));
    }

    /** @RequestBody: Jackson arma el objeto Actividad con el JSON que llega en el cuerpo. */
    @PostMapping
    public ResponseEntity<Actividad> crear(@RequestBody Actividad actividad) {
        // POST siempre crea: si el cuerpo trae un id, save() pisaría la actividad que ya lo tiene
        actividad.setId(null);
        Actividad creada = actividadService.guardar(actividad);
        return ResponseEntity.status(HttpStatus.CREATED).body(actividadService.completarCupo(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actividad> actualizar(@PathVariable Long id, @RequestBody Actividad actividad) {
        Actividad actualizada = actividadService.actualizar(id, actividad);
        return ResponseEntity.ok(actividadService.completarCupo(actualizada));
    }

    /** 204 No Content: se eliminó y no hay nada para devolver en el cuerpo. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        actividadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
