import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

class Result {

    public enum TipoLicencia {
        BASICA(1000.00, 1, 0),
        PROFESIONAL(1500.00, 2, 0),
        EMPRESARIAL(2500.00, 3, 0),
        TEMPORAL(600.00, 0, 6);

        private final double costoBase;
        private final int aniosVigencia;
        private final int mesesVigencia;

        TipoLicencia(double costoBase, int aniosVigencia, int mesesVigencia) {
            this.costoBase = costoBase;
            this.aniosVigencia = aniosVigencia;
            this.mesesVigencia = mesesVigencia;
        }

        public double getCostoBase() {
            return costoBase;
        }

        public int getAniosVigencia() {
            return aniosVigencia;
        }

        public int getMesesVigencia() {
            return mesesVigencia;
        }
    }

    public enum EstadoLicencia {
        VIGENTE,
        PROXIMA_A_VENCER,
        VENCIDA,
        BLOQUEADA
    }

    /*
     * Complete the 'evaluarLicencia' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts following parameters:
     *  1. STRING fechaActual
     *  2. STRING fechaVencimiento
     *  3. STRING tipoLicencia
     *  4. INTEGER renovacionesPrevias
     */

    public static String evaluarLicencia(String fechaActual, String fechaVencimiento, String tipoLicencia, int renovacionesPrevias) {
        try {
            TipoLicencia tipo = null;
            if (tipoLicencia != null) {
                String tipoLimpio = tipoLicencia.trim().toUpperCase();
                for (TipoLicencia tl : TipoLicencia.values()) {
                    if (tl.name().equals(tipoLimpio)) {
                        tipo = tl;
                        break;
                    }
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);

            Calendar calActual = Calendar.getInstance();
            calActual.setTime(sdf.parse(fechaActual));
            calActual.set(Calendar.HOUR_OF_DAY, 0);
            calActual.set(Calendar.MINUTE, 0);
            calActual.set(Calendar.SECOND, 0);
            calActual.set(Calendar.MILLISECOND, 0);

            Calendar calVencimiento = Calendar.getInstance();
            calVencimiento.setTime(sdf.parse(fechaVencimiento));
            calVencimiento.set(Calendar.HOUR_OF_DAY, 0);
            calVencimiento.set(Calendar.MINUTE, 0);
            calVencimiento.set(Calendar.SECOND, 0);
            calVencimiento.set(Calendar.MILLISECOND, 0);

            long diffMillis = calVencimiento.getTimeInMillis() - calActual.getTimeInMillis();
            long diasRest = Math.round(diffMillis / (1000.0 * 60 * 60 * 24));

            EstadoLicencia estado;
            if (diasRest > 30) {
                estado = EstadoLicencia.VIGENTE;
            } else if (diasRest >= 0) {
                estado = EstadoLicencia.PROXIMA_A_VENCER;
            } else if (diasRest >= -90) {
                estado = EstadoLicencia.VENCIDA;
            } else {
                estado = EstadoLicencia.BLOQUEADA;
            }

            if (estado == EstadoLicencia.BLOQUEADA) {
                return String.format(Locale.US, "%s %d 0.00 NO_DISPONIBLE", estado.name(), diasRest);
            }

            if (tipo == null) {
                return "ERROR";
            }

            double costo = tipo.getCostoBase();

            if (estado == EstadoLicencia.VIGENTE) {
                costo *= 0.90;
            } else if (estado == EstadoLicencia.VENCIDA) {
                costo *= 1.20;
            }

            if (renovacionesPrevias > 3) {
                costo *= 0.95;
            }

            Calendar calNuevaFecha = (Calendar) (estado == EstadoLicencia.VENCIDA ? calActual.clone() : calVencimiento.clone());

            if (tipo.getAniosVigencia() > 0) {
                calNuevaFecha.add(Calendar.YEAR, tipo.getAniosVigencia());
            }
            if (tipo.getMesesVigencia() > 0) {
                calNuevaFecha.add(Calendar.MONTH, tipo.getMesesVigencia());
            }

            String nuevaFechaStr = sdf.format(calNuevaFecha.getTime());

            return String.format(Locale.US, "%s %d %.2f %s", estado.name(), diasRest, costo, nuevaFechaStr);

        } catch (java.text.ParseException | IllegalArgumentException e) {
            return "ERROR";
        }
    }

}

public class SistemaRenovacionLicencias {

    public static void main(String[] args) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Ingresa los datos (fechaActual, fechaVencimiento, tipoLicencia, renovacionesPrevias):");
            String fechaActual = bufferedReader.readLine();
            String fechaVencimiento = bufferedReader.readLine();
            String tipoLicencia = bufferedReader.readLine();
            int renovacionesPrevias = Integer.parseInt(bufferedReader.readLine().trim());

            String result = Result.evaluarLicencia(fechaActual, fechaVencimiento, tipoLicencia, renovacionesPrevias);

            System.out.println("\nResultado de la evaluación:");
            System.out.println(result);
        }
    }
}