package mx.unam.fi.smartvend.infrastructure;

public class MonederoTarjeta {
    private double fondos;

    public MonederoTarjeta() {
        this.fondos = 0.0;
    }

    public double consultarFondos() {
        return fondos;
    }

    public ResultadoCobro cobrar(double precio, double cantidadRecibida) {
        if (cantidadRecibida < precio) return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, 0.0);
        fondos += precio;
        return new ResultadoCobro(EstadoCobro.COBRO_REALIZADO, 0.0);
    }

    public enum EstadoCobro {
        MONTO_INVALIDO,
        COBRO_REALIZADO
    }

    public static class ResultadoCobro {
        private final EstadoCobro estado;
        private final double cambio;

        public ResultadoCobro(EstadoCobro estado, double cambio) {
            this.estado = estado;
            this.cambio = cambio;
        }

        public EstadoCobro getEstado() {
            return estado;
        }

        public double getCambio() {
            return cambio;
        }
    }
}
