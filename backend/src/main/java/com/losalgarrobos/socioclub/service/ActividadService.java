package com.losalgarrobos.socioclub.service;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.model.EstadoInscripcion;
import com.losalgarrobos.socioclub.repository.ActividadRepository;
import com.losalgarrobos.socioclub.repository.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio de las actividades (RF4, RF7).
 * @Service la registra como bean: Spring crea una única instancia y la inyecta
 * en los controladores que la declaren en su constructor.
 */
@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;

    // Inyección de dependencias por constructor
    public ActividadService(ActividadRepository actividadRepository,
                            InscripcionRepository inscripcionRepository) {
        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    @Transactional(readOnly = true)
    public List<Actividad> listarTodas() {
        return actividadRepository.findAllByOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public List<Actividad> listarPorDisciplina(String disciplina) {
        if (disciplina == null || disciplina.isBlank()) {
            return listarTodas();
        }
        return actividadRepository.findByDisciplina(disciplina);
    }

    @Transactional(readOnly = true)
    public Actividad buscarPorId(Long id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la actividad con id " + id));
    }

    /** Inscripciones ACTIVAS de la actividad (usa la @Query del repositorio). */
    @Transactional(readOnly = true)
    public long contarInscriptosActivos(Long actividadId) {
        return inscripcionRepository.contarActivasPorActividad(actividadId);
    }

    /** Cupo disponible = cupo máximo - inscripciones activas. */
    @Transactional(readOnly = true)
    public int cupoDisponible(Actividad actividad) {
        long activas = contarInscriptosActivos(actividad.getId());
        return (int) Math.max(0, actividad.getCupoMaximo() - activas);
    }

    @Transactional
    public Actividad guardar(Actividad actividad) {
        validar(actividad);
        return actividadRepository.save(actividad);
    }

    @Transactional
    public Actividad actualizar(Long id, Actividad datos) {
        Actividad existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDisciplina(datos.getDisciplina());
        existente.setProfesor(datos.getProfesor());
        existente.setDias(datos.getDias());
        existente.setHorario(datos.getHorario());
        existente.setEdadMinima(datos.getEdadMinima());
        existente.setEdadMaxima(datos.getEdadMaxima());
        existente.setCupoMaximo(datos.getCupoMaximo());
        existente.setCuotaMensual(datos.getCuotaMensual());
        validar(existente);
        return actividadRepository.save(existente);
    }

    /**
     * Baja de una actividad. Solo se permite si nunca tuvo inscripciones.
     * Las bajas de socios son lógicas (la fila queda con estado BAJA), así que
     * una actividad con historial no se puede borrar sin perder ese registro:
     * la clave foránea de inscripciones.actividad_id lo impide.
     */
    @Transactional
    public void eliminar(Long id) {
        Actividad actividad = buscarPorId(id);
        long activas = inscripcionRepository.countByActividadIdAndEstado(id, EstadoInscripcion.ACTIVA);
        if (activas > 0) {
            throw new ReglaDeNegocioException("BAJA",
                    "No se puede eliminar: la actividad tiene " + activas + " inscripto(s) activo(s)");
        }
        long historicas = inscripcionRepository.countByActividadId(id);
        if (historicas > 0) {
            throw new ReglaDeNegocioException("BAJA",
                    "No se puede eliminar: la actividad tiene historial de " + historicas
                            + " inscripción(es). Se conserva el registro.");
        }
        actividadRepository.delete(actividad);
    }

    /** Validaciones de consistencia de los datos cargados por secretaría. */
    private void validar(Actividad a) {
        if (a.getNombre() == null || a.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (a.getDisciplina() == null || a.getDisciplina().isBlank()) {
            throw new IllegalArgumentException("La disciplina es obligatoria");
        }
        if (a.getEdadMinima() == null || a.getEdadMaxima() == null || a.getEdadMinima() > a.getEdadMaxima()) {
            throw new IllegalArgumentException("La edad mínima no puede ser mayor que la máxima");
        }
        if (a.getCupoMaximo() == null || a.getCupoMaximo() < 1) {
            throw new IllegalArgumentException("El cupo máximo debe ser al menos 1");
        }
        if (a.getCuotaMensual() == null || a.getCuotaMensual().signum() < 0) {
            throw new IllegalArgumentException("La cuota mensual no puede ser negativa");
        }
    }
}
