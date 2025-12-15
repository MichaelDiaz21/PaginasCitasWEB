package com.paginas.citas.PaginasCitasWEB.model;

import java.util.List;

public class CentroMedicoView {

    private String nombre;
    private String direccion;
    private String telefono;
    private List<String> especialidades;
    private String distancia;
    private String imagen;

    public CentroMedicoView() {}

    public CentroMedicoView(String nombre, String direccion, String telefono,
                            List<String> especialidades,
                            String distancia, String imagen) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.especialidades = especialidades;
        this.distancia = distancia;
        this.imagen = imagen;
    }

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public List<String> getEspecialidades() { return especialidades; }
    public String getDistancia() { return distancia; }
    public String getImagen() { return imagen; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEspecialidades(List<String> especialidades) { this.especialidades = especialidades; }
    public void setDistancia(String distancia) { this.distancia = distancia; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
