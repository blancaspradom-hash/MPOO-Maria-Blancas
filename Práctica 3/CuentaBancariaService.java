public class CuentaBancariaService {
    private final CuentaBancaria[] cuentas;

    public CuentaBancariaService() {
        // Inicialización con cuentas de prueba
        this.cuentas = new CuentaBancaria[] {
            new CuentaBancaria("001", "Blancas Maria", 5000.0, true),
            new CuentaBancaria("002", "Pacheco Susana", 1500.0, true),
            new CuentaBancaria("003", "Prado Jose", 300.0, false) // Cuenta inactiva
        };
    }

    private CuentaBancaria buscarCuenta(String numeroCuenta) {
        if (numeroCuenta == null) return null;
        for (CuentaBancaria cuenta : cuentas) {
            if (cuenta != null && cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }

    public boolean depositar(String numeroCuenta, double cantidad) {
        CuentaBancaria cuenta = buscarCuenta(numeroCuenta);
        if (cuenta == null) {
            System.out.println("Error: Cuenta no encontrada.");
            return false;
        }
        return cuenta.depositar(cantidad);
    }

    public boolean retirar(String numeroCuenta, double cantidad) {
        CuentaBancaria cuenta = buscarCuenta(numeroCuenta);
        if (cuenta == null) {
            System.out.println("Error: Cuenta no encontrada.");
            return false;
        }
        return cuenta.retirar(cantidad);
    }

    public boolean transferir(String cuentaOrigen, String cuentaDestino, double cantidad) {
        if (cuentaOrigen == null || cuentaDestino == null || cuentaOrigen.equals(cuentaDestino)) {
            System.out.println("Error: Cuentas origen y destino deben ser distintas y válidas.");
            return false;
        }

        CuentaBancaria origen = buscarCuenta(cuentaOrigen);
        CuentaBancaria destino = buscarCuenta(cuentaDestino);

        if (origen == null || destino == null) {
            System.out.println("Error: Una o ambas cuentas no existen.");
            return false;
        }

        // Intentar retirar de la cuenta de origen
        if (origen.retirar(cantidad)) {
            // Si el retiro es exitoso, depositar en destino
            if (destino.depositar(cantidad)) {
                return true;
            } else {
                // Revertir fondos si falla el depósito en destino
                origen.depositar(cantidad);
                System.out.println("Error: No se pudo abonar a la cuenta destino.");
                return false;
            }
        } else {
            System.out.println("Error: Fondos insuficientes o cuenta origen inactiva.");
            return false;
        }
    }
}