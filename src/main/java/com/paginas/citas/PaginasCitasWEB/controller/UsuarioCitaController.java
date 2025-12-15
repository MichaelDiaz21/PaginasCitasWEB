package com.paginas.citas.PaginasCitasWEB.controller;

import com.paginas.citas.PaginasCitasWEB.entity.Cita;
import com.paginas.citas.PaginasCitasWEB.entity.Usuario;
import com.paginas.citas.PaginasCitasWEB.service.CitaService;
import com.paginas.citas.PaginasCitasWEB.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.paginas.citas.PaginasCitasWEB.model.CentroMedicoView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usuario/citas")
public class UsuarioCitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private UsuarioService usuarioService;

    // --- Método privado para obtener usuario logueado ---
    private Usuario obtenerUsuarioLogueado(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null)
            return null;
        return usuarioService.buscarPorId(usuarioId);
    }

    // Panel principal de citas
    @GetMapping
public String panelCitas(HttpSession session, Model model) {
    Usuario usuario = obtenerUsuarioLogueado(session);
    if (usuario == null) return "redirect:/login";
    return "redirect:/usuario/citas/historial"; // ✅ ya no abre panelCitas
}

    // Historial de citas del usuario
    @GetMapping("/historial")
    public String verHistorial(HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioLogueado(session);
        if (usuario == null)
            return "redirect:/login";

        List<Cita> citas = citaService.buscarPorUsuario(usuario);
        model.addAttribute("citas", citas);
        return "usuario/historialCitas";
    }

    // Mostrar formulario para solicitar nueva cita
    @GetMapping("/disponibles")
public String mostrarFormularioSolicitud(HttpSession session, Model model) {
    Usuario usuario = obtenerUsuarioLogueado(session);
    if (usuario == null) return "redirect:/login";

    List<CentroMedicoView> centros = getCentros();

    // ✅ sacar especialidades únicas (para el select de Tipo de Cita)
    List<String> tiposCita = centros.stream()
            .flatMap(c -> c.getEspecialidades().stream())
            .distinct()
            .sorted()
            .toList();

    // ✅ nombres de centros (para el select de Centro Médico)
    List<String> nombresCentros = centros.stream()
            .map(CentroMedicoView::getNombre)
            .distinct()
            .sorted()
            .toList();

    model.addAttribute("tiposCita", tiposCita);
    model.addAttribute("centros", nombresCentros);

    return "usuario/citasDisponibles";
}


    // Obtener horarios disponibles para una fecha y tipo de cita (AJAX)
    @GetMapping("/horarios")
    @ResponseBody
    public List<String> obtenerHorariosDisponibles(@RequestParam String tipoCita,
                                                   @RequestParam String fecha) {
        LocalDate dia = LocalDate.parse(fecha);

        List<String> todosHorarios = List.of("09:00", "10:00", "11:00", "14:00", "15:00");

        List<String> ocupados = citaService.obtenerCitasPorTipoYFecha(tipoCita, dia).stream()
        .filter(c -> c.getFechaHora() != null)
        .map(c -> c.getFechaHora().toLocalTime().toString()) // "09:00"
        .collect(Collectors.toList());
        return todosHorarios.stream()
                .filter(h -> !ocupados.contains(h))
                .collect(Collectors.toList());
    }

    // Procesar solicitud de nueva cita
    @PostMapping("/solicitar")
public String solicitarCita(@RequestParam String tipoCita,
                            @RequestParam String fechaCita,
                            @RequestParam String horario,
                            @RequestParam String centroMedico,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
    Usuario usuario = obtenerUsuarioLogueado(session);
    if (usuario == null)
        return "redirect:/login";

    LocalDateTime fechaHora = LocalDateTime.of(LocalDate.parse(fechaCita), LocalTime.parse(horario));

    boolean ocupado = citaService.obtenerCitasPorTipoYFecha(tipoCita, fechaHora.toLocalDate())
        .stream()
        .anyMatch(c -> c.getFechaHora() != null && c.getFechaHora().equals(fechaHora));


    if (ocupado) {
        redirectAttributes.addFlashAttribute("error", "El horario seleccionado ya está ocupado.");
        return "redirect:/usuario/citas/disponibles";
    }

    // Crear la cita con todos los datos
    Cita cita = new Cita();
    cita.setUsuario(usuario); //Aquí se asocia el usuario
    cita.setTipo(tipoCita);
    cita.setCentroMedico(centroMedico);
    cita.setFechaHora(fechaHora);
    cita.setEstado("RESERVADA"); // Estado correcto

    citaService.guardarCita(cita); // Guarda con usuario_id

    redirectAttributes.addFlashAttribute("mensaje",
            "✅ Cita asignada exitosamente en " + centroMedico +
                    " para el " + fechaCita + " a las " + horario);

    return "redirect:/usuario/citas";
}


    // Cancelar cita
    @GetMapping("/cancelar/{id}")
public String cancelarCita(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
    Usuario usuario = obtenerUsuarioLogueado(session);
    if (usuario == null)
        return "redirect:/login";

    Cita cita = citaService.buscarPorId(id);
    if (cita == null || cita.getUsuario() == null || !cita.getUsuario().getId().equals(usuario.getId())) {
        redirectAttributes.addFlashAttribute("error", "No se puede cancelar esta cita.");
        return "redirect:/usuario/citas/historial";
    }

    citaService.eliminarCita(id); // Elimina completamente la cita de la BD

    redirectAttributes.addFlashAttribute("mensaje", "✅ Cita eliminada correctamente.");
    return "redirect:/usuario/citas/historial";
}



    // Centros médicos (información)
   @GetMapping("/centros")
public String verCentros(HttpSession session, Model model) {
    Usuario usuario = obtenerUsuarioLogueado(session);
    if (usuario == null) return "redirect:/login";

    List<CentroMedicoView> centros = List.of(
    new CentroMedicoView(
        "Centro Médico Norte",
        "Av. Principal #123, Zona Norte",
        "+57 300 123 4567",
        List.of("Cardiología", "Pediatría", "Medicina General"),
        "2.5 km",
        "/img/centros/norte.jpg"
    ),
    new CentroMedicoView(
        "Clínica Central",
        "Calle 45 #67-89, Centro",
        "+57 300 987 6543",
        List.of("Dermatología", "Oftalmología", "Neurología"),
        "3.8 km",
        "/img/centros/central.jpg"
    ),
    
    new CentroMedicoView(
        "Hospital del Sur",
        "Cra 10 #20-30, Zona Sur",
        "+57 301 555 0000",
        List.of("Urgencias", "Medicina General", "Radiología"),
        "6.1 km",
        "/img/centros/sur.jpg"
    ),
    new CentroMedicoView(
        "IPS San José",
        "Cl 12 #34-56, San José",
        "+57 302 444 2222",
        List.of("Consulta General", "Odontología", "Laboratorio"),
        "1.9 km",
        "/img/centros/IPS SAN JOSE.jpg"
    )
);

model.addAttribute("centros", centros);


    model.addAttribute("centros", centros);
    return "usuario/centrosMedicos";
}

private List<CentroMedicoView> getCentros() {
    return List.of(
        new CentroMedicoView(
            "Centro Médico Norte",
            "Av. Principal #123, Zona Norte",
            "+57 300 123 4567",
            List.of("Cardiología", "Pediatría", "Medicina General"),
            "2.5 km",
            "/img/centros/norte.jpg"
        ),
        new CentroMedicoView(
            "Clínica Central",
            "Calle 45 #67-89, Centro",
            "+57 300 987 6543",
            List.of("Dermatología", "Oftalmología", "Neurología"),
            "3.8 km",
            "/img/centros/central.jpg"
        ),
        new CentroMedicoView(
            "Hospital del Sur",
            "Carrera 10 #20-30, Sur",
            "+57 301 555 8899",
            List.of("Medicina General", "Urgencias"),
            "5.1 km",
            "/img/centros/sur.jpg"
        )
    );
}


}
