package mx.unam.fi.smartvend.infrastructure;

import org.springframework.stereotype.Component;

import mx.unam.fi.smartvend.domain.EstadoCobro;
import mx.unam.fi.smartvend.domain.Monedero;
import mx.unam.fi.smartvend.domain.ResultadoCobro;

@Component("monederoTarjeta")
public class MonederoTarjeta implements Monedero {
    private double fondos;

    public MonederoTarjeta() {
        this.fondos = 0.0;
    }

    @Override
    public double consultarFondos() {
        return fondos;
    }

    @Override
    public ResultadoCobro cobrar(double precio, double cantidadRecibida) {
        if (cantidadRecibida < precio) return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, 0.0);
        fondos += precio;
        return new ResultadoCobro(EstadoCobro.COBRO_REALIZADO, 0.0);
    }
}