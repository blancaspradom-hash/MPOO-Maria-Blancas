package mx.unam.fi.smartvend.api.dto;

import mx.unam.fi.smartvend.domain.CategoriaProducto;
import java.time.LocalDate;

public class ProductoResponseDTO {
    private final String codigo;
    private final String nombre;
    private final LocalDate fechaCaducidad;
    private final CategoriaProducto categoria;
    private final double precio;
    private final int existencias;

    public ProductoResponseDTO(String codigo, String nombre, LocalDate fechaCaducidad, CategoriaProducto categoria, double precio, int existencias) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaCaducidad = fechaCaducidad;
        this.categoria = categoria;
        this.precio = precio;
        this.existencias = existencias;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public CategoriaProducto getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public int getExistencias() { return existencias; }
}