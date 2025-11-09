package com.paginas.citas.PaginasCitasWEB.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioEditDTO {

    @NotBlank(message = "Documento es obligatorio")
    private String documento;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "Correo es obligatorio")
    @Email(message = "Correo inválido")
    private String correo;

    @NotBlank(message = "Rol es obligatorio")
    private String rol;

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido(){return apellido;}
    public void setApellido(String apellido){this.apellido = apellido;}

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
