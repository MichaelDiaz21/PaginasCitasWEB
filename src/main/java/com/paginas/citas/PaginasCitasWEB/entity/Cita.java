package com.paginas.citas.PaginasCitasWEB.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fecha y hora de la cita
    private LocalDateTime fechaHora;

    // Tipo o descripción de la cita
    @Column(nullable = false)
    private String tipo;

    // Centro médico donde se toma la cita
    @Column(nullable = false)
    private String centroMedico;


    // Estado de la cita: DISPONIBLE, RESERVADA, CANCELADA
    @Column(nullable = false)
    private String estado = "DISPONIBLE";

    // Usuario que reservó la cita (si aplica)
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Constructor vacío
    public Cita() {}

    // Constructor con parámetros
    public Cita(LocalDateTime fechaHora, String tipo, Usuario usuario) {
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getCentroMedico() { return centroMedico; }
    public void setCentroMedico(String centroMedico) { this.centroMedico = centroMedico; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public boolean isDisponible() {
        return "DISPONIBLE".equalsIgnoreCase(this.estado) && this.usuario == null;
    }
}
