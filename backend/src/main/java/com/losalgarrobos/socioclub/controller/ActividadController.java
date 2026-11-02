package com.losalgarrobos.socioclub.controller;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.service.ActividadService;
import com.losalgarrobos.socioclub.service.InscripcionService;
import com.losalgarrobos.socioclub.service.ReglaDeNegocioException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ABM de actividades para la secretaría (RF4, RF7).
 * El controlador solo recibe la petición, llama al servicio y elige la vista.
 * NUNCA toca el repositorio directamente ni contiene reglas de negocio.
 */
@Controller
@RequestMapping("/actividades")
public class ActividadController {

    private final ActividadService actividadService;
    private final InscripcionService inscripcionService;

    public ActividadController(ActividadService actividadService, InscripcionService inscripcionService) {
        this.actividadService = actividadService;
        this.inscripcionService = inscripcionService;
    }

    /** Listado con cupo disponible. */
    @GetMapping
    public String listar(Model model) {
        Map<Actividad, Integer> cupos = new LinkedHashMap<>();
        for (Actividad a : actividadService.listarTodas()) {
            cupos.put(a, actividadService.cupoDisponible(a));
        }
        model.addAttribute("cupos", cupos);
        return "actividades/lista";
    }

    /** Formulario de alta (objeto vacío para th:object). */
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("actividad", new Actividad());
        model.addAttribute("titulo", "Nueva actividad");
        return "actividades/formulario";
    }

    /** Procesa el alta. @ModelAttribute arma el objeto con los campos del formulario (th:field). */
    @PostMapping
    public String crear(@ModelAttribute("actividad") Actividad actividad, Model model,
                        RedirectAttributes redirect) {
        try {
            actividadService.guardar(actividad);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Nueva actividad");
            return "actividades/formulario";
        }
        redirect.addFlashAttribute("mensaje", "Actividad \"" + actividad.getNombre() + "\" creada correctamente");
        return "redirect:/actividades";
    }

    /** Formulario de edición, precargado. */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("actividad", actividadService.buscarPorId(id));
        model.addAttribute("titulo", "Editar actividad");
        return "actividades/formulario";
    }

    /** Procesa la edición. */
    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("actividad") Actividad actividad,
                             Model model, RedirectAttributes redirect) {
        try {
            actividadService.actualizar(id, actividad);
        } catch (IllegalArgumentException e) {
            actividad.setId(id);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Editar actividad");
            return "actividades/formulario";
        }
        redirect.addFlashAttribute("mensaje", "Actividad actualizada correctamente");
        return "redirect:/actividades";
    }

    /** Baja (los formularios HTML solo soportan GET/POST, por eso es POST y no DELETE). */
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            actividadService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Actividad eliminada");
        } catch (ReglaDeNegocioException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/actividades";
    }

    /** RF7: inscriptos activos de una actividad y cupo disponible. */
    @GetMapping("/{id}/inscriptos")
    public String inscriptos(@PathVariable Long id, Model model) {
        Actividad actividad = actividadService.buscarPorId(id);
        model.addAttribute("actividad", actividad);
        model.addAttribute("inscripciones", inscripcionService.listarActivasPorActividad(id));
        model.addAttribute("cupoDisponible", actividadService.cupoDisponible(actividad));
        return "actividades/inscriptos";
    }
}
