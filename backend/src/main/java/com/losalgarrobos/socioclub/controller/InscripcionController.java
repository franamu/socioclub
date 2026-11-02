package com.losalgarrobos.socioclub.controller;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.model.Inscripcion;
import com.losalgarrobos.socioclub.service.ActividadService;
import com.losalgarrobos.socioclub.service.InscripcionService;
import com.losalgarrobos.socioclub.service.RecursoNoEncontradoException;
import com.losalgarrobos.socioclub.service.ReglaDeNegocioException;
import com.losalgarrobos.socioclub.service.SocioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Formulario de inscripción de secretaría (RF6) y bajas (RF9).
 * Observar que el controlador NO verifica cupo, duplicados, edad, etc.:
 * eso lo hace InscripcionService. Acá solo se captura la excepción y se
 * muestra el mensaje en la vista.
 */
@Controller
@RequestMapping("/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final SocioService socioService;
    private final ActividadService actividadService;

    public InscripcionController(InscripcionService inscripcionService,
                                 SocioService socioService,
                                 ActividadService actividadService) {
        this.inscripcionService = inscripcionService;
        this.socioService = socioService;
        this.actividadService = actividadService;
    }

    /** Formulario: elegir socio y actividad. Se puede preseleccionar con ?socioId= o ?actividadId= */
    @GetMapping("/nueva")
    public String nueva(@RequestParam(required = false) Long socioId,
                        @RequestParam(required = false) Long actividadId,
                        Model model) {
        cargarCombos(model);
        model.addAttribute("socioId", socioId);
        model.addAttribute("actividadId", actividadId);
        return "inscripciones/formulario";
    }

    @PostMapping
    public String inscribir(@RequestParam Long socioId, @RequestParam Long actividadId,
                            Model model, RedirectAttributes redirect) {
        try {
            Inscripcion inscripcion = inscripcionService.inscribir(socioId, actividadId);
            redirect.addFlashAttribute("mensaje",
                    inscripcion.getSocio().getNombreCompleto() + " fue inscripto/a en "
                            + inscripcion.getActividad().getNombre());
            return "redirect:/actividades/" + actividadId + "/inscriptos";
        } catch (ReglaDeNegocioException e) {
            // La regla rechazó la operación: volvemos al formulario con el mensaje de la regla
            model.addAttribute("error", e.getMessage());
            model.addAttribute("regla", e.getRegla());
        } catch (RecursoNoEncontradoException e) {
            model.addAttribute("error", e.getMessage());
        }
        cargarCombos(model);
        model.addAttribute("socioId", socioId);
        model.addAttribute("actividadId", actividadId);
        return "inscripciones/formulario";
    }

    /** Baja lógica desde secretaría. Vuelve a la página indicada en "volverA". */
    @PostMapping("/{id}/baja")
    public String darDeBaja(@PathVariable Long id,
                            @RequestParam(defaultValue = "/actividades") String volverA,
                            RedirectAttributes redirect) {
        try {
            Inscripcion inscripcion = inscripcionService.darDeBaja(id);
            redirect.addFlashAttribute("mensaje",
                    "Se dio de baja a " + inscripcion.getSocio().getNombreCompleto()
                            + " de " + inscripcion.getActividad().getNombre());
        } catch (ReglaDeNegocioException | RecursoNoEncontradoException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + volverA;
    }

    private void cargarCombos(Model model) {
        model.addAttribute("socios", socioService.listarTodos());
        Map<Actividad, Integer> cupos = new LinkedHashMap<>();
        for (Actividad a : actividadService.listarTodas()) {
            cupos.put(a, actividadService.cupoDisponible(a));
        }
        model.addAttribute("cupos", cupos);
    }
}
