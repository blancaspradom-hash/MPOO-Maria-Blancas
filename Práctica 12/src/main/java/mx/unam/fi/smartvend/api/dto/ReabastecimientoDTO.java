package mx.unam.fi.smartvend.api.dto;

public class ReabastecimientoDTO {
    private int cantidad;

    public ReabastecimientoDTO() {}

    public ReabastecimientoDTO(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}