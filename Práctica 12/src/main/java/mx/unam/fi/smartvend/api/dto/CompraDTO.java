package mx.unam.fi.smartvend.api.dto;

public class CompraDTO {
    private String codigoProducto;
    private String metodoPago;
    private double cantidadRecibida;

    public CompraDTO() {}

    public CompraDTO(String codigoProducto, String metodoPago, double cantidadRecibida) {
        this.codigoProducto = codigoProducto;
        this.metodoPago = metodoPago;
        this.cantidadRecibida = cantidadRecibida;
    }

    public String getCodigoProducto() { return codigoProducto; }
    public String getMetodoPago() { return metodoPago; }
    public double getCantidadRecibida() { return cantidadRecibida; }

    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public void setCantidadRecibida(double cantidadRecibida) { this.cantidadRecibida = cantidadRecibida; }
}