package com.losalgarrobos.socioclub.controller;

import com.losalgarrobos.socioclub.service.RecursoNoEncontradoException;
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
}
