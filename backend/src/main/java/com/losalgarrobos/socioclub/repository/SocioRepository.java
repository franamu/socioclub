package com.losalgarrobos.socioclub.repository;

import com.losalgarrobos.socioclub.model.Socio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA genera la implementación en tiempo de ejecución.
 * Los métodos "derivados" se arman a partir del nombre: Spring lo parsea
 * (findBy + Apellido + Containing + IgnoreCase) y construye el SQL
 * (WHERE LOWER(apellido) LIKE LOWER('%?%')).
 */
@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {

    /** Búsqueda por apellido (parcial, sin distinguir mayúsculas), paginada. RF5 */
    Page<Socio> findByApellidoContainingIgnoreCase(String apellido, Pageable pageable);

    /** Búsqueda por DNI, usada para el ingreso del socio a la app (RF8). */
    Optional<Socio> findByDni(String dni);

    boolean existsByDni(String dni);
}
