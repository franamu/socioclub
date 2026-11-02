package com.losalgarrobos.socioclub.repository;

import com.losalgarrobos.socioclub.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    /** Método derivado: SELECT * FROM actividades WHERE disciplina = ? */
    List<Actividad> findByDisciplina(String disciplina);

    /** Listado ordenado por nombre para los combos y el catálogo. */
    List<Actividad> findAllByOrderByNombreAsc();
}
