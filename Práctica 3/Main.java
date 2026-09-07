import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            CuentaBancariaService bancarioService = new CuentaBancariaService();
            int opcion = 0;

            do {
                System.out.println("\n========== SISTEMA BANCARIO ==========");
                System.out.println("1. Transferir dinero");
                System.out.println("2. Depositar dinero");
                System.out.println("3. Retirar dinero");
                System.out.println("4. Salir");
                System.out.print("\nSeleccione una opción: ");

                if (scanner.hasNextInt()) {
                    opcion = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer
                } else {
                    System.out.println("Opción inválida.");
                    scanner.nextLine();
                    continue;
                }

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Ingrese número de cuenta origen: ");
                        String origen = scanner.nextLine();
                        System.out.print("Ingrese número de cuenta destino: ");
                        String destino = scanner.nextLine();
                        System.out.print("Ingrese monto a transferir: ");
                        double montoTransfer = scanner.nextDouble();

                        if (bancarioService.transferir(origen, destino, montoTransfer)) {
                            System.out.println("¡Transferencia realizada con éxito!");
                        } else {
                            System.out.println("Fallo la transferencia.");
                        }
                    }

                    case 2 -> {
                        System.out.print("Ingrese número de cuenta: ");
                        String ctaDep = scanner.nextLine();
                        System.out.print("Ingrese monto a depositar: ");
                        double montoDep = scanner.nextDouble();

                        if (bancarioService.depositar(ctaDep, montoDep)) {
                            System.out.println("¡Depósito realizado con éxito!");
                        } else {
                            System.out.println("Fallo el depósito.");
                        }
                    }

                    case 3 -> {
                        System.out.print("Ingrese número de cuenta: ");
                        String ctaRet = scanner.nextLine();
                        System.out.print("Ingrese monto a retirar: ");
                        double montoRet = scanner.nextDouble();

                        if (bancarioService.retirar(ctaRet, montoRet)) {
                            System.out.println("¡Retiro realizado con éxito!");
                        } else {
                            System.out.println("Fallo el retiro.");
                        }
                    }

                    case 4 -> {
                        System.out.println("Saliendo del sistema...");
                    }

                    default -> {
                        System.out.println("Opción no válida. Intente de nuevo.");
                    }
                }
            } while (opcion != 4);
        }
    }
}