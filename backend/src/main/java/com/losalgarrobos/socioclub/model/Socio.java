package com.losalgarrobos.socioclub.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA: cada objeto Socio se corresponde con una fila de la tabla "socios".
 * Hibernate (el ORM) traduce los atributos a columnas y las relaciones a claves foráneas.
 */
@Entity
@Table(name = "socios")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT en MySQL
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String apellido;

    // @DateTimeFormat: el formulario envía "2016-05-10" (input type="date") y Spring lo convierte a LocalDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 120)
    private String email;

    @Column(length = 30)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuota", nullable = false, length = 10)
    private EstadoCuota estadoCuota = EstadoCuota.AL_DIA;

    /**
     * Lado "uno" de la relación Socio 1 --- * Inscripcion.
     * mappedBy = "socio" indica que la clave foránea la tiene el atributo
     * "socio" de la entidad Inscripcion; esta lista no crea columna alguna.
     */
    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    // JPA necesita un constructor sin argumentos
    public Socio() {
    }

    public Socio(String dni, String nombre, String apellido, LocalDate fechaNacimiento,
                 String email, String telefono, EstadoCuota estadoCuota) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.telefono = telefono;
        this.estadoCuota = estadoCuota;
    }

    /** Edad calculada a la fecha de hoy (se usa en RN3). No es una columna. */
    public int getEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }

    public boolean isMoroso() {
        return estadoCuota == EstadoCuota.MOROSO;
    }

    // ---- Getters y setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public EstadoCuota getEstadoCuota() {
        return estadoCuota;
    }

    public void setEstadoCuota(EstadoCuota estadoCuota) {
        this.estadoCuota = estadoCuota;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }
}
