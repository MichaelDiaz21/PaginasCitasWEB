package com.paginas.citas.PaginasCitasWEB.controller;

import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import com.paginas.citas.PaginasCitasWEB.model.UsuarioEditDTO;
import com.paginas.citas.PaginasCitasWEB.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        return "usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String mostrarEditarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) return "redirect:/admin/usuarios";

        UsuarioEditDTO dto = new UsuarioEditDTO();
        dto.setDocumento(usuario.getDocumento());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setRol(usuario.getRol());

        model.addAttribute("usuarioId", id);
        model.addAttribute("usuarioEditDTO", dto);
        return "editarusuario";
    }

    @PostMapping("/usuarios/editar/{id}")
    public String guardarEditarUsuario(@PathVariable Long id,
            @ModelAttribute("usuarioEditDTO") UsuarioEditDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) return "redirect:/admin/usuarios";

        usuario.setDocumento(dto.getDocumento());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setRol(dto.getRol());

        usuarioService.actualizarUsuario(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuarios";
    }
}
