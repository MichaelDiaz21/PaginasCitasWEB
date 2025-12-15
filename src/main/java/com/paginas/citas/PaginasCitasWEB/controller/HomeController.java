package com.paginas.citas.PaginasCitasWEB.controller;

import com.paginas.citas.PaginasCitasWEB.entity.Cita;
import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import com.paginas.citas.PaginasCitasWEB.service.CitaService;
import com.paginas.citas.PaginasCitasWEB.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private CitaService citaService;

    private Usuario obtenerUsuarioLogueado(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) return null;
        return usuarioService.buscarPorId(usuarioId);
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioLogueado(session);
        if (usuario == null) return "redirect:/login";

        List<Cita> citas = citaService.buscarPorUsuario(usuario);
        LocalDateTime ahora = LocalDateTime.now();

        long programadas = citas.stream()
                .filter(c -> c.getFechaHora() != null)
                .filter(c -> c.getFechaHora().isAfter(ahora))
                .filter(c -> "RESERVADA".equalsIgnoreCase(c.getEstado()))
                .count();

        // ✅ Realizadas: SOLO las que están marcadas como REALIZADA
        long realizadas = citas.stream()
                .filter(c -> "REALIZADA".equalsIgnoreCase(c.getEstado()))
                .count();

        long recordatorios = citas.stream()
                .filter(c -> c.getFechaHora() != null)
                .filter(c -> "RESERVADA".equalsIgnoreCase(c.getEstado()))
                .filter(c -> !c.getFechaHora().isBefore(ahora))
                .filter(c -> c.getFechaHora().isBefore(ahora.plusDays(3)))
                .count();

        List<Cita> proximas = citas.stream()
                .filter(c -> c.getFechaHora() != null)
                .filter(c -> c.getFechaHora().isAfter(ahora))
                .filter(c -> "RESERVADA".equalsIgnoreCase(c.getEstado()))
                .sorted(Comparator.comparing(Cita::getFechaHora))
                .limit(5)
                .toList();

        model.addAttribute("citasProgramadas", programadas);
        model.addAttribute("consultasRealizadas", realizadas);
        model.addAttribute("recordatorios", recordatorios);
        model.addAttribute("proximasCitas", proximas);

        return "home";
    }
}
