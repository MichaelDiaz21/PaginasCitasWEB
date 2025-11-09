package com.paginas.citas.PaginasCitasWEB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaginasCitasWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaginasCitasWebApplication.class, args);
        System.out.println("✅ Aplicación iniciada correctamente en http://localhost:8080");
    }
}
