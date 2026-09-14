public class CuentaBancaria {
    private final String numeroCuenta;
    private final String titular;
    private double saldo;
    private final boolean activa;

    public CuentaBancaria(String numeroCuenta, String titular, double saldo, boolean activa) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.activa = activa;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean isActiva() {
        return activa;
    }

    public boolean depositar(double cantidad) {
        if (cantidad <= 0 || !activa) {
            return false;
        }
        this.saldo += cantidad;
        return true;
    }

    public boolean retirar(double cantidad) {
        if (cantidad <= 0 || cantidad > this.saldo || !activa) {
            return false;
        }
        this.saldo -= cantidad;
        return true;
    }
}