package com.losalgarrobos.socioclub.service;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.model.EstadoInscripcion;
import com.losalgarrobos.socioclub.model.Inscripcion;
import com.losalgarrobos.socioclub.model.Socio;
import com.losalgarrobos.socioclub.repository.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Corazón del sistema: acá viven las reglas de negocio RN1 a RN5.
 *
 * Están en la capa de servicio (y no en el controlador ni en la vista) para que
 * se escriban una sola vez y se apliquen igual sin importar por dónde entre la
 * inscripción: el formulario Thymeleaf de Marta o, más adelante, la API REST
 * que usa la app React de los socios.
 */
@Service
public class InscripcionService {

    /** RN5: máximo de inscripciones activas por socio. */
    public static final int MAXIMO_ACTIVIDADES_POR_SOCIO = 3;

    private final InscripcionRepository inscripcionRepository;
    private final SocioService socioService;
    private final ActividadService actividadService;

    public InscripcionService(InscripcionRepository inscripcionRepository,
                              SocioService socioService,
                              ActividadService actividadService) {
        this.inscripcionRepository = inscripcionRepository;
        this.socioService = socioService;
        this.actividadService = actividadService;
    }

    /**
     * RF6: inscribe un socio a una actividad aplicando RN1 a RN5.
     * Si alguna regla no se cumple lanza ReglaDeNegocioException con el mensaje
     * exacto que pide el enunciado. Como el método es @Transactional, si algo
     * falla no queda nada a medias en la base.
     */
    @Transactional
    public Inscripcion inscribir(Long socioId, Long actividadId) {
        Socio socio = socioService.buscarPorId(socioId);          // 404 si no existe
        Actividad actividad = actividadService.buscarPorId(actividadId);

        // RN4 - Cuota al día: un MOROSO no puede inscribirse (sí consultar y darse de baja)
        if (socio.isMoroso()) {
            throw new ReglaDeNegocioException("RN4", "Debe regularizar su cuota en secretaría");
        }

        // RN2 - Sin duplicados: no puede tener dos inscripciones ACTIVAS en la misma actividad
        boolean yaInscripto = inscripcionRepository.existsBySocioIdAndActividadIdAndEstado(
                socioId, actividadId, EstadoInscripcion.ACTIVA);
        if (yaInscripto) {
            throw new ReglaDeNegocioException("RN2", "El socio ya está inscripto en esta actividad");
        }

        // RN3 - Edad: calculada a la fecha de hoy, dentro del rango de la actividad
        int edad = socio.getEdad();
        if (!actividad.esAptaParaEdad(edad)) {
            throw new ReglaDeNegocioException("RN3",
                    "La edad del socio (" + edad + " años) está fuera del rango permitido: "
                            + actividad.getRangoEdades());
        }

        // RN5 - Máximo 3 actividades activas por socio
        long activasDelSocio = inscripcionRepository.countBySocioIdAndEstado(socioId, EstadoInscripcion.ACTIVA);
        if (activasDelSocio >= MAXIMO_ACTIVIDADES_POR_SOCIO) {
            throw new ReglaDeNegocioException("RN5",
                    "Alcanzó el máximo de " + MAXIMO_ACTIVIDADES_POR_SOCIO + " actividades");
        }

        // RN1 - Cupo: las inscripciones ACTIVAS no pueden superar el cupo máximo
        long activasEnActividad = inscripcionRepository.contarActivasPorActividad(actividadId);
        if (activasEnActividad >= actividad.getCupoMaximo()) {
            throw new ReglaDeNegocioException("RN1", "La actividad no tiene cupo disponible");
        }

        // Todas las reglas pasaron: se persiste la inscripción
        Inscripcion inscripcion = new Inscripcion(socio, actividad);
        return inscripcionRepository.save(inscripcion);
    }

    /**
     * RF9: baja lógica. La fila no se borra, cambia su estado a BAJA.
     * Un socio moroso sí puede darse de baja (RN4 solo bloquea nuevas inscripciones).
     */
    @Transactional
    public Inscripcion darDeBaja(Long inscripcionId) {
        Inscripcion inscripcion = buscarPorId(inscripcionId);
        if (!inscripcion.isActiva()) {
            throw new ReglaDeNegocioException("BAJA", "La inscripción ya estaba dada de baja");
        }
        inscripcion.darDeBaja();
        return inscripcionRepository.save(inscripcion);
    }

    @Transactional(readOnly = true)
    public Inscripcion buscarPorId(Long id) {
        return inscripcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la inscripción con id " + id));
    }

    /** RF7: inscriptos activos de una actividad. */
    @Transactional(readOnly = true)
    public List<Inscripcion> listarActivasPorActividad(Long actividadId) {
        return inscripcionRepository.buscarPorActividadYEstado(actividadId, EstadoInscripcion.ACTIVA);
    }

    /** RF8: inscripciones activas de un socio. */
    @Transactional(readOnly = true)
    public List<Inscripcion> listarActivasPorSocio(Long socioId) {
        socioService.buscarPorId(socioId); // valida que exista
        return inscripcionRepository.buscarActivasPorSocio(socioId);
    }

    @Transactional(readOnly = true)
    public List<Inscripcion> listarTodas() {
        return inscripcionRepository.findAll();
    }
}
