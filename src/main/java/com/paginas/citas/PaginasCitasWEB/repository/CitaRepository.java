package com.paginas.citas.PaginasCitasWEB.repository;

import com.paginas.citas.PaginasCitasWEB.entity.Cita;
import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Buscar todas las citas de un usuario específico
    List<Cita> findByUsuario(Usuario usuario);

    // Buscar todas las citas que aún no han sido reservadas
    List<Cita> findByUsuarioIsNull();

    // Buscar citas por tipo y rango de fecha (solo disponibles)
    List<Cita> findByTipoAndFechaHoraBetweenAndUsuarioIsNull(String tipo, LocalDateTime inicio, LocalDateTime fin);

    long countByUsuarioAndEstado(Usuario usuario, String estado);

    long countByUsuarioAndEstadoIn(Usuario usuario, java.util.List<String> estados);

    java.util.List<Cita> findTop3ByUsuarioAndEstadoAndFechaHoraAfterOrderByFechaHoraAsc(
            Usuario usuario, String estado, java.time.LocalDateTime ahora);

    List<Cita> findByTipoAndFechaHoraBetweenAndEstadoNot(
        String tipo,
        LocalDateTime inicio,
        LocalDateTime fin,
        String estado
);

}
