package com.losalgarrobos.socioclub.controller;

import com.losalgarrobos.socioclub.service.RecursoNoEncontradoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Captura las excepciones que escapan de los controladores Thymeleaf y
 * muestra una página de error amigable en vez del "Whitelabel Error Page".
 * (Para la API REST, en la semana 3, se hará un @RestControllerAdvice aparte que devuelva JSON.)
 */
@ControllerAdvice(basePackages = "com.losalgarrobos.socioclub.controller")
public class ManejadorErroresMvc {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noEncontrado(RecursoNoEncontradoException e, Model model) {
        model.addAttribute("mensaje", e.getMessage());
        return "error/404";
    }

    /**
     * La base rechazó la operación (por ejemplo, borrar una fila que otras referencian).
     * Los servicios ya validan estos casos; esto evita el "Whitelabel Error Page" si algo se escapa.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String integridad(DataIntegrityViolationException e, Model model) {
        model.addAttribute("mensaje",
                "La operación no se puede realizar porque hay datos relacionados que dependen de este registro.");
        return "error/operacion";
    }
}
