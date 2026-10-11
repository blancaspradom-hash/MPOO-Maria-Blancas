import java.io.*;
import java.text.*;
import java.util.*;

class Result {

    public enum TipoVehiculo {
        MOTOCICLETA(15.0, 100.0),
        AUTOMOVIL(25.0, 180.0),
        CAMIONETA(35.0, 250.0),
        ELECTRICO(20.0, 150.0);

        private final double tarifaHora;
        private final double maxBloque;

        TipoVehiculo(double tarifaHora, double maxBloque) {
            this.tarifaHora = tarifaHora;
            this.maxBloque = maxBloque;
        }

        public double getTarifaHora() {
            return tarifaHora;
        }

        public double getMaxBloque() {
            return maxBloque;
        }
    }

    public enum TipoEstancia {
        NORMAL, NOCTURNA, FIN_SEMANA, MIXTA
    }

    /*
     * Complete the 'calcularEstancia' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts following parameters:
     *  1. STRING tipoVehiculo
     *  2. STRING fechaEntrada
     *  3. STRING horaEntrada
     *  4. STRING fechaSalida
     *  5. STRING horaSalida
     */

    public static String calcularEstancia(String tipoVehiculo, String fechaEntrada, String horaEntrada, String fechaSalida, String horaSalida) {
        try {
            TipoVehiculo tipo = null;
            if (tipoVehiculo != null) {
                String tipoLimpio = tipoVehiculo.trim().toUpperCase();
                for (TipoVehiculo tv : TipoVehiculo.values()) {
                    if (tv.name().equals(tipoLimpio)) {
                        tipo = tv;
                        break;
                    }
                }
            }
            if (tipo == null) return "INVALID";

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            sdf.setLenient(false);

            Calendar calEntrada = Calendar.getInstance();
            calEntrada.setTime(sdf.parse(fechaEntrada + " " + horaEntrada));

            Calendar calSalida = Calendar.getInstance();
            calSalida.setTime(sdf.parse(fechaSalida + " " + horaSalida));

            if (!calSalida.after(calEntrada)) {
                return "INVALID";
            }

            long diffMillis = calSalida.getTimeInMillis() - calEntrada.getTimeInMillis();
            double horasExactas = diffMillis / (1000.0 * 60 * 60);
            long horasCobradas = (long) Math.ceil(horasExactas);

            double costoBase = 0.0;
            long horasRestantes = horasCobradas;

            while (horasRestantes > 0) {
                long horasEnBloque = Math.min(horasRestantes, 24);
                double costoBloqueSinTope = horasEnBloque * tipo.getTarifaHora();
                double costoBloque = Math.min(costoBloqueSinTope, tipo.getMaxBloque());
                costoBase += costoBloque;
                horasRestantes -= horasEnBloque;
            }

            int diaEntrada = calEntrada.get(Calendar.DAY_OF_WEEK);
            int diaSalida = calSalida.get(Calendar.DAY_OF_WEEK);
            boolean esFinSemana = (diaEntrada == Calendar.SATURDAY || diaEntrada == Calendar.SUNDAY) ||
                                  (diaSalida == Calendar.SATURDAY || diaSalida == Calendar.SUNDAY);

            int horaEntradaInt = calEntrada.get(Calendar.HOUR_OF_DAY);
            int horaSalidaInt = calSalida.get(Calendar.HOUR_OF_DAY);
            boolean mismaFecha = (calEntrada.get(Calendar.YEAR) == calSalida.get(Calendar.YEAR)) &&
                                 (calEntrada.get(Calendar.DAY_OF_YEAR) == calSalida.get(Calendar.DAY_OF_YEAR));

            boolean esNocturna = (horaEntradaInt >= 20) || (horaSalidaInt < 6) || !mismaFecha;
            boolean esElectrico = (tipo == TipoVehiculo.ELECTRICO);

            TipoEstancia clasificacion;
            if (esFinSemana && esNocturna) {
                clasificacion = TipoEstancia.MIXTA;
            } else if (esFinSemana) {
                clasificacion = TipoEstancia.FIN_SEMANA;
            } else if (esNocturna) {
                clasificacion = TipoEstancia.NOCTURNA;
            } else {
                clasificacion = TipoEstancia.NORMAL;
            }

            double costoFinal = costoBase;

            if (esFinSemana) {
                costoFinal *= 1.20;
            }
            if (esNocturna) {
                costoFinal *= 1.15;
            }
            if (esElectrico) {
                costoFinal *= 0.90;
            }

            return String.format(Locale.US, "%d %.2f %s", horasCobradas, costoFinal, clasificacion.name());

        } catch (ParseException | IllegalArgumentException | NullPointerException e) {
            return "INVALID";
        }
    }

}

public class TarifaDinamicaEstacionamiento {
    public static void main(String[] args) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Ingresa los datos (tipoVehiculo, fechaEntrada, horaEntrada, fechaSalida, horaSalida):");
            String tipoVehiculo = bufferedReader.readLine();
            String fechaEntrada = bufferedReader.readLine();
            String horaEntrada = bufferedReader.readLine();
            String fechaSalida = bufferedReader.readLine();
            String horaSalida = bufferedReader.readLine();

            String result = Result.calcularEstancia(tipoVehiculo, fechaEntrada, horaEntrada, fechaSalida, horaSalida);

            System.out.println("\nResultado de la estancia:");
            System.out.println(result);
        }
    }
}