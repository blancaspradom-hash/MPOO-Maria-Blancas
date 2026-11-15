package mx.unam.fi.smartvend.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import mx.unam.fi.smartvend.domain.EstadoCobro;
import mx.unam.fi.smartvend.domain.Monedero;
import mx.unam.fi.smartvend.domain.ResultadoCobro;

@Component("monederoEfectivo")
public class MonederoEfectivo implements Monedero {
    private double fondos;

    public MonederoEfectivo(@Value("${smartvend.fondos.iniciales:500.0}") double fondosIniciales) {
        this.fondos = fondosIniciales;
    }

    @Override
    public double consultarFondos() {
        return fondos;
    }

    @Override
    public ResultadoCobro cobrar(double precio, double cantidadRecibida) {
        if (cantidadRecibida < precio) return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, 0.0);
        double cambio = cantidadRecibida - precio;
        if (fondos < cambio) return new ResultadoCobro(EstadoCobro.SIN_FONDOS_SUFICIENTES, 0.0);
        fondos += precio;
        return new ResultadoCobro(EstadoCobro.COBRO_REALIZADO, cambio);
    }
}