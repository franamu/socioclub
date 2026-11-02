package com.losalgarrobos.socioclub;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.model.EstadoCuota;
import com.losalgarrobos.socioclub.model.EstadoInscripcion;
import com.losalgarrobos.socioclub.model.Inscripcion;
import com.losalgarrobos.socioclub.model.Socio;
import com.losalgarrobos.socioclub.repository.InscripcionRepository;
import com.losalgarrobos.socioclub.service.ActividadService;
import com.losalgarrobos.socioclub.service.InscripcionService;
import com.losalgarrobos.socioclub.service.ReglaDeNegocioException;
import com.losalgarrobos.socioclub.service.SocioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de las reglas RN1 a RN5 sin base de datos:
 * los repositorios y servicios de apoyo se reemplazan por "mocks" de Mockito.
 * Se ejecutan con: mvn test
 */
class InscripcionServiceTest {

    private InscripcionRepository inscripcionRepository;
    private SocioService socioService;
    private ActividadService actividadService;
    private InscripcionService servicio;

    private Socio socioAlDia;
    private Actividad yoga;

    @BeforeEach
    void preparar() {
        inscripcionRepository = Mockito.mock(InscripcionRepository.class);
        socioService = Mockito.mock(SocioService.class);
        actividadService = Mockito.mock(ActividadService.class);
        servicio = new InscripcionService(inscripcionRepository, socioService, actividadService);

        socioAlDia = new Socio("30111222", "Juan", "Gómez", LocalDate.now().minusYears(41),
                "juan@mail.com", "351", EstadoCuota.AL_DIA);
        socioAlDia.setId(1L);

        yoga = new Actividad("Yoga", "Bienestar", "Inés Arguello", "Mar y Jue", "09:00",
                16, 99, 15, new BigDecimal("17000"));
        yoga.setId(8L);

        when(socioService.buscarPorId(1L)).thenReturn(socioAlDia);
        when(actividadService.buscarPorId(8L)).thenReturn(yoga);

        // Escenario base: sin inscripciones previas, con cupo
        when(inscripcionRepository.existsBySocioIdAndActividadIdAndEstado(anyLong(), anyLong(), eq(EstadoInscripcion.ACTIVA)))
                .thenReturn(false);
        when(inscripcionRepository.countBySocioIdAndEstado(anyLong(), eq(EstadoInscripcion.ACTIVA))).thenReturn(0L);
        when(inscripcionRepository.contarActivasPorActividad(anyLong())).thenReturn(0L);
        when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void inscribeCuandoTodasLasReglasSeCumplen() {
        Inscripcion resultado = servicio.inscribir(1L, 8L);
        assertNotNull(resultado);
        assertEquals(EstadoInscripcion.ACTIVA, resultado.getEstado());
        assertEquals(yoga, resultado.getActividad());
    }

    @Test
    void rn1_rechazaSiNoHayCupo() {
        when(inscripcionRepository.contarActivasPorActividad(8L)).thenReturn(15L);
        ReglaDeNegocioException ex = assertThrows(ReglaDeNegocioException.class, () -> servicio.inscribir(1L, 8L));
        assertEquals("RN1", ex.getRegla());
        assertEquals("La actividad no tiene cupo disponible", ex.getMessage());
    }

    @Test
    void rn2_rechazaSiYaEstaInscripto() {
        when(inscripcionRepository.existsBySocioIdAndActividadIdAndEstado(1L, 8L, EstadoInscripcion.ACTIVA)).thenReturn(true);
        ReglaDeNegocioException ex = assertThrows(ReglaDeNegocioException.class, () -> servicio.inscribir(1L, 8L));
        assertEquals("RN2", ex.getRegla());
        assertEquals("El socio ya está inscripto en esta actividad", ex.getMessage());
    }

    @Test
    void rn3_rechazaSiLaEdadEstaFueraDeRango() {
        socioAlDia.setFechaNacimiento(LocalDate.now().minusYears(10)); // 10 años, yoga es de 16 a 99
        ReglaDeNegocioException ex = assertThrows(ReglaDeNegocioException.class, () -> servicio.inscribir(1L, 8L));
        assertEquals("RN3", ex.getRegla());
    }

    @Test
    void rn4_rechazaSiElSocioEsMoroso() {
        socioAlDia.setEstadoCuota(EstadoCuota.MOROSO);
        ReglaDeNegocioException ex = assertThrows(ReglaDeNegocioException.class, () -> servicio.inscribir(1L, 8L));
        assertEquals("RN4", ex.getRegla());
        assertEquals("Debe regularizar su cuota en secretaría", ex.getMessage());
    }

    @Test
    void rn5_rechazaSiYaTieneTresActividades() {
        when(inscripcionRepository.countBySocioIdAndEstado(1L, EstadoInscripcion.ACTIVA)).thenReturn(3L);
        ReglaDeNegocioException ex = assertThrows(ReglaDeNegocioException.class, () -> servicio.inscribir(1L, 8L));
        assertEquals("RN5", ex.getRegla());
        assertEquals("Alcanzó el máximo de 3 actividades", ex.getMessage());
    }

    @Test
    void bajaLogicaCambiaElEstadoSinBorrar() {
        Inscripcion inscripcion = new Inscripcion(socioAlDia, yoga);
        inscripcion.setId(50L);
        when(inscripcionRepository.findById(50L)).thenReturn(java.util.Optional.of(inscripcion));

        Inscripcion resultado = servicio.darDeBaja(50L);
        assertEquals(EstadoInscripcion.BAJA, resultado.getEstado());
    }
}
