package mx.unam.fi.smartvend.api.dto;

import java.time.LocalDate;

import mx.unam.fi.smartvend.domain.CategoriaProducto;

public class RegistroProductoDTO {
    private String codigo;
    private String nombre;
    private LocalDate fechaCaducidad;
    private CategoriaProducto categoria;
    private double precio;

    public RegistroProductoDTO() {}

    public RegistroProductoDTO(String codigo, String nombre, LocalDate fechaCaducidad, CategoriaProducto categoria, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaCaducidad = fechaCaducidad;
        this.categoria = categoria;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public CategoriaProducto getCategoria() { return categoria; }
    public double getPrecio() { return precio; }

    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
    public void setCategoria(CategoriaProducto categoria) { this.categoria = categoria; }
    public void setPrecio(double precio) { this.precio = precio; }
}