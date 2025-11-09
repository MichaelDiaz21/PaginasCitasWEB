package com.paginas.citas.PaginasCitasWEB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.paginas.citas.PaginasCitasWEB.service.UsuarioService;

@Controller
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar todos los usuarios
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        return "usuarios"; // Devuelve la plantilla usuarios.html
    }

    // Eliminar usuario
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios"; // Vuelve a la lista después de eliminar
    }
}
