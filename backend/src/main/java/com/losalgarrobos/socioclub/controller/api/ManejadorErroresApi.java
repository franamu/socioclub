package com.losalgarrobos.socioclub.controller.api;

import com.losalgarrobos.socioclub.service.RecursoNoEncontradoException;
import com.losalgarrobos.socioclub.service.ReglaDeNegocioException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Convierte en JSON las excepciones que escapan de los controladores REST.
 * annotations = RestController.class: no interviene en los controladores Thymeleaf.
 * @Order: ManejadorErroresMvc abarca el paquete "controller" y por lo tanto también este
 * subpaquete; sin la prioridad, un 404 de la API podría devolver su página HTML.
 */
@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ManejadorErroresApi {

    /** 409 Conflict: la petición está bien formada, pero una regla de negocio la rechaza. */
    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<Map<String, String>> reglaDeNegocio(ReglaDeNegocioException e) {
        Map<String, String> cuerpo = cuerpoDeError(e.getMessage());
        cuerpo.put("regla", e.getRegla());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RecursoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpoDeError(e.getMessage()));
    }

    /** Validaciones de datos de los servicios (nombre obligatorio, cupo mínimo, etc.). */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> datosInvalidos(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(cuerpoDeError(e.getMessage()));
    }

    /**
     * La URL trae un texto donde va un número (por ejemplo /api/actividades/abc).
     * Spring falla al convertirlo a Long antes de entrar al controlador. Sin este método la
     * tomaría datosInvalidos (su causa es un IllegalArgumentException), con el mensaje de Java en inglés.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> idNoNumerico(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body(cuerpoDeError(
                "El valor '" + e.getValue() + "' no es válido para '" + e.getName() + "': debe ser un número"));
    }

    /** La base rechazó la operación. No se devuelve e.getMessage() porque expone el SQL. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> integridad(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpoDeError(
                "La base de datos rechazó la operación: falta un dato obligatorio o hay registros relacionados."));
    }

    /** El cuerpo no es un JSON válido o un valor no es del tipo esperado. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> jsonMalFormado(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(cuerpoDeError("El cuerpo de la petición no es un JSON válido"));
    }

    // LinkedHashMap mantiene el orden en que se agregan los campos: primero "error" y después "regla"
    private Map<String, String> cuerpoDeError(String mensaje) {
        Map<String, String> cuerpo = new LinkedHashMap<>();
        cuerpo.put("error", mensaje);
        return cuerpo;
    }
}
