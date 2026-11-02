package com.losalgarrobos.socioclub.controller;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.service.ActividadService;
import com.losalgarrobos.socioclub.service.SocioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Pantalla de inicio del sistema de secretaría: resumen de ocupación.
 */
@Controller
public class HomeController {

    private final ActividadService actividadService;
    private final SocioService socioService;

    public HomeController(ActividadService actividadService, SocioService socioService) {
        this.actividadService = actividadService;
        this.socioService = socioService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        Map<Actividad, Integer> ocupacion = new LinkedHashMap<>();
        for (Actividad a : actividadService.listarTodas()) {
            ocupacion.put(a, actividadService.cupoDisponible(a));
        }
        model.addAttribute("ocupacion", ocupacion);
        model.addAttribute("cantidadSocios", socioService.listarTodos().size());
        return "inicio";
    }
}
