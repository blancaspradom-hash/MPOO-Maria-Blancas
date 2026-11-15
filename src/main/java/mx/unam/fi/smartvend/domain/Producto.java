package mx.unam.fi.smartvend.domain;

import java.time.LocalDate;

public class Producto {
    private final String codigo;
    private final String nombre;
    private final LocalDate fechaCaducidad;
    private final String categoria;

    public Producto(Builder builder) {
        this.codigo = builder.codigo;
        this.nombre = builder.nombre;
        this.fechaCaducidad = builder.fechaCaducidad;
        this.categoria = builder.categoria;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public String getCategoria() { return categoria; }

    public static class Builder {
        private String codigo;
        private String nombre;
        private LocalDate fechaCaducidad;
        private String categoria;

        public Builder codigo(String codigo) { this.codigo = codigo; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder fechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; return this; }
        public Builder categoria(String categoria) { this.categoria = categoria; return this; }

        public Producto build() {
            return new Producto(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}