package com.paginas.citas.PaginasCitasWEB.service;

import com.paginas.citas.PaginasCitasWEB.entity.Cita;
import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import com.paginas.citas.PaginasCitasWEB.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public void guardarCita(Cita cita) {
        citaRepository.save(cita);
    }

    public List<Cita> obtenerTodas() {
        return citaRepository.findAll();
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id).orElse(null);
    }

    public void eliminarCita(Long id) {
    citaRepository.deleteById(id);
}

    public List<Cita> buscarPorUsuario(Usuario usuario) {
        return citaRepository.findByUsuario(usuario);
    }

    public List<Cita> obtenerCitasDisponibles() {
        return citaRepository.findByUsuarioIsNull();
    }

    public List<Cita> obtenerCitasPorTipoYFecha(String tipo, LocalDate fecha) {
    return citaRepository.findByTipoAndFechaHoraBetweenAndEstadoNot(
            tipo,
            fecha.atStartOfDay(),
            fecha.atTime(LocalTime.MAX),
            "CANCELADA"
    );
    }
}
