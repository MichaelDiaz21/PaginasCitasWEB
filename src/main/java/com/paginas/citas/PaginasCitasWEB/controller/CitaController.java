package com.paginas.citas.PaginasCitasWEB.controller;

import com.paginas.citas.PaginasCitasWEB.entity.Cita;
import com.paginas.citas.PaginasCitasWEB.service.CitaService;
import com.paginas.citas.PaginasCitasWEB.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar todas las citas
    @GetMapping
    public String listarCitas(Model model) {
        List<Cita> citas = citaService.obtenerTodas();
        model.addAttribute("citas", citas);
        return "admin/listarCitas";
    }

    // Mostrar formulario para crear nueva cita
    @GetMapping("/nueva")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        return "admin/crearCita";
    }

    // Guardar cita nueva
    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute("cita") Cita cita) {
        citaService.guardarCita(cita);
        return "redirect:/admin/citas";
    }

    // Editar cita
    @GetMapping("/editar/{id}")
    public String editarCita(@PathVariable Long id, Model model) {
        Cita cita = citaService.buscarPorId(id);
        if (cita == null) {
            return "redirect:/admin/citas";
        }
        model.addAttribute("cita", cita);
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        return "admin/editarCita";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCita(@PathVariable Long id, @ModelAttribute("cita") Cita cita) {
        Cita existente = citaService.buscarPorId(id);
        if (existente != null) {
            existente.setFechaHora(cita.getFechaHora());
            existente.setTipo(cita.getTipo());
            existente.setUsuario(cita.getUsuario());
            citaService.guardarCita(existente);
        }
        return "redirect:/admin/citas";
    }

    // Eliminar cita
    @GetMapping("/eliminar/{id}")
    public String eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return "redirect:/admin/citas";
    }
}
