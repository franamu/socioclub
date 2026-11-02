package com.losalgarrobos.socioclub.repository;

import com.losalgarrobos.socioclub.model.EstadoInscripcion;
import com.losalgarrobos.socioclub.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    /**
     * Consulta JPQL explícita con @Query: cuenta las inscripciones ACTIVAS de una
     * actividad. Se usa para calcular el cupo disponible (RF7) y para RN1.
     * Nota: se escribe en JPQL (sobre entidades), no en SQL (sobre tablas).
     */
    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.actividad.id = :actividadId "
            + "AND i.estado = com.losalgarrobos.socioclub.model.EstadoInscripcion.ACTIVA")
    long contarActivasPorActividad(@Param("actividadId") Long actividadId);

    /** Inscripciones activas de un socio, con la actividad cargada en la misma consulta. */
    @Query("SELECT i FROM Inscripcion i JOIN FETCH i.actividad WHERE i.socio.id = :socioId "
            + "AND i.estado = com.losalgarrobos.socioclub.model.EstadoInscripcion.ACTIVA")
    List<Inscripcion> buscarActivasPorSocio(@Param("socioId") Long socioId);

    /** Inscriptos (activos) de una actividad con el socio ya cargado, para el listado de secretaría. */
    @Query("SELECT i FROM Inscripcion i JOIN FETCH i.socio s WHERE i.actividad.id = :actividadId "
            + "AND i.estado = :estado ORDER BY s.apellido, s.nombre")
    List<Inscripcion> buscarPorActividadYEstado(@Param("actividadId") Long actividadId,
                                                @Param("estado") EstadoInscripcion estado);

    // ---- Métodos derivados del nombre ----

    /** Cantidad de inscripciones de un socio en un estado dado (RN5). */
    long countBySocioIdAndEstado(Long socioId, EstadoInscripcion estado);

    /** ¿Existe ya una inscripción de este socio a esta actividad en este estado? (RN2) */
    boolean existsBySocioIdAndActividadIdAndEstado(Long socioId, Long actividadId, EstadoInscripcion estado);

    /** Cantidad de inscripciones activas de una actividad (versión derivada, equivalente a la @Query). */
    long countByActividadIdAndEstado(Long actividadId, EstadoInscripcion estado);
}
