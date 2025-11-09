package com.paginas.citas.PaginasCitasWEB.controller;

import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import com.paginas.citas.PaginasCitasWEB.model.UsuarioDTO;
import com.paginas.citas.PaginasCitasWEB.model.UsuarioEditDTO;
import com.paginas.citas.PaginasCitasWEB.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "registro";
    }

    // Registrar usuario
    @PostMapping("/registrar")
    public String registrarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
            BindingResult result,
            Model model) {

        // Validar que las contraseñas coincidan
        if (!usuarioDTO.contrasenasCoinciden()) {
            result.rejectValue("confirmarContrasena", "error.usuarioDTO", "Las contraseñas no coinciden");
        }

        // Validar documento y correo únicos
        if (usuarioService.existePorDocumento(usuarioDTO.getDocumento())) {
            result.rejectValue("documento", "error.usuarioDTO", "El documento ya está registrado");
        }
        if (usuarioService.existePorCorreo(usuarioDTO.getCorreo())) {
            result.rejectValue("correo", "error.usuarioDTO", "El correo ya está registrado");
        }

        // Si hay errores, volver al formulario
        if (result.hasErrors()) {
            return "registro";
        }

        // Crear entidad Usuario a partir del DTO
        Usuario usuario = new Usuario(
                usuarioDTO.getDocumento(),
                usuarioDTO.getNombre(),
                usuarioDTO.getApellido(),
                usuarioDTO.getCorreo(),
                usuarioDTO.getContrasena(),
                "USER" // Por defecto todos los nuevos usuarios son USER
        );

        usuarioService.registrarUsuario(usuario);

        model.addAttribute("mensaje", "Usuario registrado correctamente. Inicia sesión.");
        return "login";
    }

    // Mostrar formulario de edición
    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            return "redirect:/usuarios"; // usuario no encontrado
        }

        UsuarioEditDTO usuarioEditDTO = new UsuarioEditDTO();
        usuarioEditDTO.setDocumento(usuario.getDocumento());
        usuarioEditDTO.setNombre(usuario.getNombre());
        usuarioEditDTO.setApellido(usuario.getApellido());
        usuarioEditDTO.setCorreo(usuario.getCorreo());
        usuarioEditDTO.setRol(usuario.getRol());

        model.addAttribute("usuarioEditDTO", usuarioEditDTO);
        model.addAttribute("usuarioId", id);
        return "editarUsuario";
    }

    // Guardar cambios
    @PostMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id,
            @Valid @ModelAttribute("usuarioEditDTO") UsuarioEditDTO usuarioEditDTO,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuarioId", id);
            return "editarUsuario";
        }

        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            return "redirect:/usuarios";
        }

        usuario.setDocumento(usuarioEditDTO.getDocumento());
        usuario.setNombre(usuarioEditDTO.getNombre());
        usuario.setApellido(usuarioEditDTO.getApellido());
        usuario.setCorreo(usuarioEditDTO.getCorreo());
        usuario.setRol(usuarioEditDTO.getRol());
        // La contraseña NO se modifica

        usuarioService.actualizarUsuario(usuario);

        return "redirect:/usuarios";
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
@GetMapping("/perfil")
public String mostrarPerfil(HttpSession session, Model model) {
    Long usuarioId = (Long) session.getAttribute("usuarioId");
    if (usuarioId == null) {
        return "redirect:/login"; // no hay sesión
    }

    Usuario usuario = usuarioService.buscarPorId(usuarioId);
    model.addAttribute("usuario", usuario);
    return "perfil";
}

}
