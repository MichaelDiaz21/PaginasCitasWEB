package com.paginas.citas.PaginasCitasWEB.controller;

import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import com.paginas.citas.PaginasCitasWEB.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/reset-password")
    public String mostrarFormularioReset() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String procesarCorreo(@RequestParam String correo, Model model) {
        if (!usuarioService.existePorCorreo(correo)) {
            model.addAttribute("error", "El correo no está registrado");
            return "reset-password";
        }
        model.addAttribute("correo", correo);
        return "nueva-contrasena";
    }

    @PostMapping("/reset-password/update")
    public String actualizarContrasena(@RequestParam String correo,
                                       @RequestParam String contrasena,
                                       @RequestParam String confirmarContrasena,
                                       Model model) {
        if (!contrasena.equals(confirmarContrasena)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("correo", correo);
            return "nueva-contrasena";
        }

        Usuario usuario = usuarioService.buscarPorCorreo(correo);
        if (usuario == null) {
            model.addAttribute("error", "El correo no existe");
            return "reset-password";
        }

        usuario.setContrasena(contrasena);

        model.addAttribute("mensaje", "Contraseña actualizada correctamente. Ahora puedes iniciar sesión.");
        return "login";
    }
}
