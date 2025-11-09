package com.paginas.citas.PaginasCitasWEB.repository;

import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por correo
    Optional<Usuario> findByCorreo(String correo);

    // Buscar usuario por documento
    Optional<Usuario> findByDocumento(String documento);

    // Validar existencia por correo
    boolean existsByCorreo(String correo);

    // Validar existencia por documento
    boolean existsByDocumento(String documento);

}
