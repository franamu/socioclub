package com.losalgarrobos.socioclub.controller;

import com.losalgarrobos.socioclub.model.EstadoCuota;
import com.losalgarrobos.socioclub.model.Socio;
import com.losalgarrobos.socioclub.service.InscripcionService;
import com.losalgarrobos.socioclub.service.SocioService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Alta, modificación y listado paginado de socios con búsqueda por apellido (RF5).
 */
@Controller
@RequestMapping("/socios")
public class SocioController {

    private final SocioService socioService;
    private final InscripcionService inscripcionService;

    public SocioController(SocioService socioService, InscripcionService inscripcionService) {
        this.socioService = socioService;
        this.inscripcionService = inscripcionService;
    }

    /**
     * Listado paginado. Ejemplo: /socios?apellido=per&pagina=1
     * @RequestParam toma los parámetros de la URL; si no vienen usa los valores por defecto.
     */
    @GetMapping
    public String listar(@RequestParam(defaultValue = "") String apellido,
                         @RequestParam(defaultValue = "0") int pagina,
                         Model model) {
        Page<Socio> pageSocios = socioService.listar(apellido, pagina);
        model.addAttribute("pagina", pageSocios);
        model.addAttribute("apellido", apellido);
        return "socios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("socio", new Socio());
        model.addAttribute("titulo", "Nuevo socio");
        model.addAttribute("estados", EstadoCuota.values());
        return "socios/formulario";
    }

    @PostMapping
    public String crear(@ModelAttribute("socio") Socio socio, Model model, RedirectAttributes redirect) {
        try {
            socioService.guardar(socio);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Nuevo socio");
            model.addAttribute("estados", EstadoCuota.values());
            return "socios/formulario";
        }
        redirect.addFlashAttribute("mensaje", "Socio " + socio.getNombreCompleto() + " creado correctamente");
        return "redirect:/socios";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("socio", socioService.buscarPorId(id));
        model.addAttribute("titulo", "Editar socio");
        model.addAttribute("estados", EstadoCuota.values());
        return "socios/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("socio") Socio socio,
                             Model model, RedirectAttributes redirect) {
        try {
            socioService.actualizar(id, socio);
        } catch (IllegalArgumentException e) {
            socio.setId(id);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Editar socio");
            model.addAttribute("estados", EstadoCuota.values());
            return "socios/formulario";
        }
        redirect.addFlashAttribute("mensaje", "Socio actualizado correctamente");
        return "redirect:/socios";
    }

    /** Ficha del socio con sus inscripciones activas. */
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Socio socio = socioService.buscarPorId(id);
        model.addAttribute("socio", socio);
        model.addAttribute("inscripciones", inscripcionService.listarActivasPorSocio(id));
        return "socios/detalle";
    }
}
