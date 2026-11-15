package mx.unam.fi.smartvend.api.dto;

import java.time.LocalDate;

public class ProductoResponseDTO {
    private String codigo;
    private String nombre;
    private LocalDate fechaCaducidad;
    private String categoria;
    private double precio;
    private int existencias;

    public ProductoResponseDTO(String codigo, String nombre, LocalDate fechaCaducidad, String categoria, double precio, int existencias) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaCaducidad = fechaCaducidad;
        this.categoria = categoria;
        this.precio = precio;
        this.existencias = existencias;
    }

    public ProductoResponseDTO(String codigo, String nombre, LocalDate fechaCaducidad, Enum<?> categoria, double precio, int existencias) {
        this(codigo, nombre, fechaCaducidad, categoria.name(), precio, existencias);
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public int getExistencias() { return existencias; }
}