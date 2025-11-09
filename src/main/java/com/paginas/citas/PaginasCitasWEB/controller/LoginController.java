package com.paginas.citas.PaginasCitasWEB.controller;

import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import com.paginas.citas.PaginasCitasWEB.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
            @RequestParam String contrasena,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        Usuario usuario = usuarioService.buscarPorCorreoYContrasena(correo, contrasena);

        if (usuario != null) {
            // Guardar datos en sesión
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("rol", usuario.getRol());
            session.setAttribute("usuarioNombre", usuario.getNombre());

            // Redirigir según rol
            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/usuarios"; // admin
            } else {
                return "redirect:/home"; // usuario normal
            }

        } else {
            redirectAttributes.addFlashAttribute("error", "Correo o contraseña incorrectos");
            return "redirect:/login";
        }
    }

    @GetMapping("/usuario/perfil")
    public String verPerfil(HttpSession session, org.springframework.ui.Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }
}
