package mx.unam.fi.smartvend.api.dto;

import mx.unam.fi.smartvend.domain.MetodoPago;

public class CompraDTO {
    private String codigoProducto;
    private MetodoPago metodoPago;
    private double cantidadRecibida;

    public CompraDTO() {}

    public CompraDTO(String codigoProducto, MetodoPago metodoPago, double cantidadRecibida) {
        this.codigoProducto = codigoProducto;
        this.metodoPago = metodoPago;
        this.cantidadRecibida = cantidadRecibida;
    }

    public String getCodigoProducto() { 
        return codigoProducto; 
    }
    
    public MetodoPago getMetodoPago() { 
        return metodoPago; 
    }
    
    public double getCantidadRecibida() { 
        return cantidadRecibida; 
    }

    public void setCodigoProducto(String codigoProducto) { 
        this.codigoProducto = codigoProducto; 
    }
    
    public void setMetodoPago(MetodoPago metodoPago) { 
        this.metodoPago = metodoPago; 
    }
    
    public void setCantidadRecibida(double cantidadRecibida) { 
        this.cantidadRecibida = cantidadRecibida; 
    }
}