package com.losalgarrobos.socioclub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA para la tabla "actividades".
 */
@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 40)
    private String disciplina;

    @Column(nullable = false, length = 80)
    private String profesor;

    @Column(nullable = false, length = 40)
    private String dias;

    @Column(nullable = false, length = 10)
    private String horario;

    @Column(name = "edad_minima", nullable = false)
    private Integer edadMinima;

    @Column(name = "edad_maxima", nullable = false)
    private Integer edadMaxima;

    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;

    @Column(name = "cuota_mensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal cuotaMensual;

    /** Lado "uno" de Actividad 1 --- * Inscripcion. */
    @OneToMany(mappedBy = "actividad")
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Actividad() {
    }

    public Actividad(String nombre, String disciplina, String profesor, String dias, String horario,
                     Integer edadMinima, Integer edadMaxima, Integer cupoMaximo, BigDecimal cuotaMensual) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.profesor = profesor;
        this.dias = dias;
        this.horario = horario;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.cupoMaximo = cupoMaximo;
        this.cuotaMensual = cuotaMensual;
    }

    /** ¿Una persona de esta edad puede anotarse? (RN3) */
    public boolean esAptaParaEdad(int edad) {
        return edad >= edadMinima && edad <= edadMaxima;
    }

    public String getRangoEdades() {
        return edadMinima + " a " + edadMaxima + " años";
    }

    // ---- Getters y setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Integer getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(Integer edadMinima) {
        this.edadMinima = edadMinima;
    }

    public Integer getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(Integer edadMaxima) {
        this.edadMaxima = edadMaxima;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }
}
