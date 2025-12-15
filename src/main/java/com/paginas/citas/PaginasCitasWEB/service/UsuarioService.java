package com.paginas.citas.PaginasCitasWEB.service;

import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import com.paginas.citas.PaginasCitasWEB.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ✅ REGISTRO (encripta siempre)
    public void registrarUsuario(Usuario usuario) {
        usuario.setCorreo(usuario.getCorreo().trim().toLowerCase());
        usuario.setDocumento(usuario.getDocumento().trim());

        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioRepository.save(usuario);
    }

    // ✅ RESET/UPDATE PASSWORD (encripta siempre)
    public void actualizarContrasena(String correo, String contrasenaPlana) {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);
        if (usuario == null) return;

        usuario.setContrasena(passwordEncoder.encode(contrasenaPlana));
        usuarioRepository.save(usuario);
    }
    
    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public boolean existePorCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    public boolean existePorDocumento(String documento) {
        return usuarioRepository.existsByDocumento(documento);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    
    public Usuario buscarPorCorreoYContrasena(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent() && passwordEncoder.matches(contrasena, usuarioOpt.get().getContrasena())) {
            return usuarioOpt.get();
        }
        return null;
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    // Reencriptar contraseñas planas a BCrypt
    public void actualizarContraseñas() {
        List<Usuario> usuarios = listarUsuarios();
        for (Usuario u : usuarios) {
            if (!u.getContrasena().startsWith("$2a$")) { // si no está en BCrypt
                u.setContrasena(passwordEncoder.encode(u.getContrasena()));
                actualizarUsuario(u);
            }
        }
    }
}
