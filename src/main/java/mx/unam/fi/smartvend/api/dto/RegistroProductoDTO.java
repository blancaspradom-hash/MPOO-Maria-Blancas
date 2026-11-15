package mx.unam.fi.smartvend.api.dto;

import java.time.LocalDate;

public class RegistroProductoDTO {
    private String codigo;
    private String nombre;
    private LocalDate fechaCaducidad;
    private String categoria;
    private double precio;

    public RegistroProductoDTO() {}

    public RegistroProductoDTO(String codigo, String nombre, LocalDate fechaCaducidad, String categoria, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaCaducidad = fechaCaducidad;
        this.categoria = categoria;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }

    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setPrecio(double precio) { this.precio = precio; }
}