package com.losalgarrobos.socioclub.service;

import com.losalgarrobos.socioclub.model.Socio;
import com.losalgarrobos.socioclub.repository.SocioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Lógica de negocio de los socios (RF5, RF8).
 */
@Service
public class SocioService {

    public static final int TAMANIO_PAGINA = 10;

    private final SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    /**
     * Listado paginado con búsqueda por apellido. Pageable le indica a Spring Data
     * qué página, de qué tamaño y con qué orden; el repositorio arma el LIMIT/OFFSET.
     */
    @Transactional(readOnly = true)
    public Page<Socio> listar(String apellido, int pagina) {
        Pageable pageable = PageRequest.of(pagina, TAMANIO_PAGINA, Sort.by("apellido", "nombre"));
        String filtro = apellido == null ? "" : apellido.trim();
        // Si el filtro está vacío, "Containing" con "" devuelve todos
        return socioRepository.findByApellidoContainingIgnoreCase(filtro, pageable);
    }

    @Transactional(readOnly = true)
    public List<Socio> listarTodos() {
        return socioRepository.findAll(Sort.by("apellido", "nombre"));
    }

    @Transactional(readOnly = true)
    public Socio buscarPorId(Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el socio con id " + id));
    }

    @Transactional(readOnly = true)
    public Socio buscarPorDni(String dni) {
        return socioRepository.findByDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un socio con DNI " + dni));
    }

    @Transactional
    public Socio guardar(Socio socio) {
        validar(socio);
        if (socioRepository.existsByDni(socio.getDni())) {
            throw new IllegalArgumentException("Ya existe un socio con el DNI " + socio.getDni());
        }
        return socioRepository.save(socio);
    }

    @Transactional
    public Socio actualizar(Long id, Socio datos) {
        Socio existente = buscarPorId(id);
        validar(datos);
        // Si cambia el DNI, verificamos que no lo tenga otro socio
        if (!existente.getDni().equals(datos.getDni()) && socioRepository.existsByDni(datos.getDni())) {
            throw new IllegalArgumentException("Ya existe un socio con el DNI " + datos.getDni());
        }
        existente.setDni(datos.getDni());
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setFechaNacimiento(datos.getFechaNacimiento());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setEstadoCuota(datos.getEstadoCuota());
        return socioRepository.save(existente);
    }

    private void validar(Socio s) {
        if (s.getDni() == null || !s.getDni().matches("\\d{7,8}")) {
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 dígitos numéricos, sin puntos");
        }
        if (s.getNombre() == null || s.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (s.getApellido() == null || s.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (s.getFechaNacimiento() == null || s.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria y no puede ser futura");
        }
        if (s.getEstadoCuota() == null) {
            throw new IllegalArgumentException("El estado de cuota es obligatorio");
        }
    }
}
