import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

enum TipoReembolso {
    ALIMENTOS,
    TRANSPORTE,
    HOSPEDAJE
}

enum ResultadoValidacion {
    APROBADO,
    SIN_COMPROBANTE,
    FUERA_DE_PERIODO,
    EXCEDE_LIMITE,
    REQUIERE_AUTORIZACION
}

final class ReembolsoInputDTO {
    private final TipoReembolso tipo;
    private final String folio;
    private final String empleado;
    private final double monto;
    private final LocalDate fechaGasto;
    private final boolean tieneComprobante;
    private final boolean autorizadoGerente;
    private final LocalDate fechaEvaluacion;

    public ReembolsoInputDTO(
            TipoReembolso tipo,
            String folio,
            String empleado,
            double monto,
            LocalDate fechaGasto,
            boolean tieneComprobante,
            boolean autorizadoGerente,
            LocalDate fechaEvaluacion) {
        this.tipo = tipo;
        this.folio = folio;
        this.empleado = empleado;
        this.monto = monto;
        this.fechaGasto = fechaGasto;
        this.tieneComprobante = tieneComprobante;
        this.autorizadoGerente = autorizadoGerente;
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public TipoReembolso getTipo() { return tipo; }
    public String getFolio() { return folio; }
    public String getEmpleado() { return empleado; }
    public double getMonto() { return monto; }
    public LocalDate getFechaGasto() { return fechaGasto; }
    public boolean tieneComprobante() { return tieneComprobante; }
    public boolean estaAutorizadoGerente() { return autorizadoGerente; }
    public LocalDate getFechaEvaluacion() { return fechaEvaluacion; }
}

final class ReembolsoOutputDTO {
    private final String folio;
    private final TipoReembolso tipo;
    private final ResultadoValidacion resultado;

    public ReembolsoOutputDTO(
            String folio,
            TipoReembolso tipo,
            ResultadoValidacion resultado) {
        this.folio = folio;
        this.tipo = tipo;
        this.resultado = resultado;
    }

    public String getFolio() { return folio; }
    public TipoReembolso getTipo() { return tipo; }
    public ResultadoValidacion getResultado() { return resultado; }
}

abstract class Reembolso {
    protected final String folio;
    protected final String empleado;
    protected final double monto;
    protected final LocalDate fechaGasto;
    protected final boolean tieneComprobante;
    protected final boolean autorizadoGerente;
    protected final LocalDate fechaEvaluacion;

    public Reembolso(
            String folio,
            String empleado,
            double monto,
            LocalDate fechaGasto,
            boolean tieneComprobante,
            boolean autorizadoGerente,
            LocalDate fechaEvaluacion) {
        this.folio = folio;
        this.empleado = empleado;
        this.monto = monto;
        this.fechaGasto = fechaGasto;
        this.tieneComprobante = tieneComprobante;
        this.autorizadoGerente = autorizadoGerente;
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public Reembolso(
            String folio,
            double monto,
            LocalDate fechaGasto,
            LocalDate fechaEvaluacion) {
        this(folio, "Desconocido", monto, fechaGasto, true, false, fechaEvaluacion);
    }

    public abstract double getLimitePermitido();
    public abstract TipoReembolso getTipo();

    public String getFolio() { return folio; }
    public String getEmpleado() { return empleado; }
    public double getMonto() { return monto; }
    public LocalDate getFechaGasto() { return fechaGasto; }
    public boolean tieneComprobante() { return tieneComprobante; }
    public boolean estaAutorizadoGerente() { return autorizadoGerente; }
    public LocalDate getFechaEvaluacion() { return fechaEvaluacion; }
}

final class ReembolsoAlimentos extends Reembolso {
    public ReembolsoAlimentos(
            String folio,
            String empleado,
            double monto,
            LocalDate fechaGasto,
            boolean tieneComprobante,
            boolean autorizadoGerente,
            LocalDate fechaEvaluacion) {
        super(folio, empleado, monto, fechaGasto, tieneComprobante, autorizadoGerente, fechaEvaluacion);
    }

    public ReembolsoAlimentos(
            String folio,
            double monto,
            LocalDate fechaGasto,
            LocalDate fechaEvaluacion) {
        super(folio, monto, fechaGasto, fechaEvaluacion);
    }

    @Override
    public double getLimitePermitido() {
        return 1000.0;
    }

    @Override
    public TipoReembolso getTipo() {
        return TipoReembolso.ALIMENTOS;
    }
}

final class ReembolsoTransporte extends Reembolso {
    public ReembolsoTransporte(
            String folio,
            String empleado,
            double monto,
            LocalDate fechaGasto,
            boolean tieneComprobante,
            boolean autorizadoGerente,
            LocalDate fechaEvaluacion) {
        super(folio, empleado, monto, fechaGasto, tieneComprobante, autorizadoGerente, fechaEvaluacion);
    }

    public ReembolsoTransporte(
            String folio,
            double monto,
            LocalDate fechaGasto,
            LocalDate fechaEvaluacion) {
        super(folio, monto, fechaGasto, fechaEvaluacion);
    }

    @Override
    public double getLimitePermitido() {
        return 1500.0;
    }

    @Override
    public TipoReembolso getTipo() {
        return TipoReembolso.TRANSPORTE;
    }
}

final class ReembolsoHospedaje extends Reembolso {
    public ReembolsoHospedaje(
            String folio,
            String empleado,
            double monto,
            LocalDate fechaGasto,
            boolean tieneComprobante,
            boolean autorizadoGerente,
            LocalDate fechaEvaluacion) {
        super(folio, empleado, monto, fechaGasto, tieneComprobante, autorizadoGerente, fechaEvaluacion);
    }

    public ReembolsoHospedaje(
            String folio,
            double monto,
            LocalDate fechaGasto,
            LocalDate fechaEvaluacion) {
        super(folio, monto, fechaGasto, fechaEvaluacion);
    }

    @Override
    public double getLimitePermitido() {
        return 4000.0;
    }

    @Override
    public TipoReembolso getTipo() {
        return TipoReembolso.HOSPEDAJE;
    }
}

abstract class RegistroReembolso {
    public abstract Reembolso crearReembolso(ReembolsoInputDTO input);
}

final class RegistroAlimentos extends RegistroReembolso {
    @Override
    public Reembolso crearReembolso(ReembolsoInputDTO input) {
        return new ReembolsoAlimentos(
                input.getFolio(),
                input.getEmpleado(),
                input.getMonto(),
                input.getFechaGasto(),
                input.tieneComprobante(),
                input.estaAutorizadoGerente(),
                input.getFechaEvaluacion()
        );
    }
}

final class RegistroTransporte extends RegistroReembolso {
    @Override
    public Reembolso crearReembolso(ReembolsoInputDTO input) {
        return new ReembolsoTransporte(
                input.getFolio(),
                input.getEmpleado(),
                input.getMonto(),
                input.getFechaGasto(),
                input.tieneComprobante(),
                input.estaAutorizadoGerente(),
                input.getFechaEvaluacion()
        );
    }
}

final class RegistroHospedaje extends RegistroReembolso {
    @Override
    public Reembolso crearReembolso(ReembolsoInputDTO input) {
        return new ReembolsoHospedaje(
                input.getFolio(),
                input.getEmpleado(),
                input.getMonto(),
                input.getFechaGasto(),
                input.tieneComprobante(),
                input.estaAutorizadoGerente(),
                input.getFechaEvaluacion()
        );
    }
}

abstract class ValidadorReembolso {
    protected ValidadorReembolso siguiente;

    public void setSiguiente(ValidadorReembolso siguiente) {
        this.siguiente = siguiente;
    }

    public abstract ResultadoValidacion validar(Reembolso reembolso);
}

final class ValidadorComprobante extends ValidadorReembolso {
    @Override
    public ResultadoValidacion validar(Reembolso reembolso) {
        if (!reembolso.tieneComprobante()) {
            return ResultadoValidacion.SIN_COMPROBANTE;
        }
        if (siguiente != null) {
            return siguiente.validar(reembolso);
        }
        return ResultadoValidacion.APROBADO;
    }
}

final class ValidadorPeriodo extends ValidadorReembolso {
    @Override
    public ResultadoValidacion validar(Reembolso reembolso) {
        long diasTranscurridos = ChronoUnit.DAYS.between(reembolso.getFechaGasto(), reembolso.getFechaEvaluacion());
        if (diasTranscurridos > 30) {
            return ResultadoValidacion.FUERA_DE_PERIODO;
        }
        if (siguiente != null) {
            return siguiente.validar(reembolso);
        }
        return ResultadoValidacion.APROBADO;
    }
}

final class ValidadorLimite extends ValidadorReembolso {
    @Override
    public ResultadoValidacion validar(Reembolso reembolso) {
        if (reembolso.getMonto() > reembolso.getLimitePermitido()) {
            return ResultadoValidacion.EXCEDE_LIMITE;
        }
        if (siguiente != null) {
            return siguiente.validar(reembolso);
        }
        return ResultadoValidacion.APROBADO;
    }
}

final class ValidadorGerencia extends ValidadorReembolso {
    @Override
    public ResultadoValidacion validar(Reembolso reembolso) {
        if (reembolso.getMonto() > 2000.0 && !reembolso.estaAutorizadoGerente()) {
            return ResultadoValidacion.REQUIERE_AUTORIZACION;
        }
        if (siguiente != null) {
            return siguiente.validar(reembolso);
        }
        return ResultadoValidacion.APROBADO;
    }
}

class Result {
    public static ReembolsoOutputDTO procesarReembolso(ReembolsoInputDTO input) {
        RegistroReembolso registro = switch (input.getTipo()) {
            case ALIMENTOS -> new RegistroAlimentos();
            case TRANSPORTE -> new RegistroTransporte();
            case HOSPEDAJE -> new RegistroHospedaje();
            default -> throw new IllegalArgumentException("Tipo de reembolso no soportado");
        };

        Reembolso reembolso = registro.crearReembolso(input);

        ValidadorReembolso v1 = new ValidadorComprobante();
        ValidadorReembolso v2 = new ValidadorPeriodo();
        ValidadorReembolso v3 = new ValidadorLimite();
        ValidadorReembolso v4 = new ValidadorGerencia();

        v1.setSiguiente(v2);
        v2.setSiguiente(v3);
        v3.setSiguiente(v4);

        ResultadoValidacion resultado = v1.validar(reembolso);

        return new ReembolsoOutputDTO(reembolso.getFolio(), reembolso.getTipo(), resultado);
    }
}

public class SolicitudesDeReembolso {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Ingrese la fecha de evaluación (YYYY-MM-DD):");
        String lineaFecha = br.readLine();
        LocalDate fechaEvaluacion = LocalDate.parse(lineaFecha.trim());

        System.out.println("Ingrese los datos del reembolso (tipo|folio|empleado|monto|fechaGasto|tieneComprobante|autorizadoGerente):");
        String lineaSolicitud = br.readLine();
        String[] datos = lineaSolicitud.split("\\|");

        TipoReembolso tipo = TipoReembolso.valueOf(datos[0].trim());
        String folio = datos[1].trim();
        String empleado = datos[2].trim();
        double monto = Double.parseDouble(datos[3].trim());
        LocalDate fechaGasto = LocalDate.parse(datos[4].trim());
        boolean tieneComprobante = Boolean.parseBoolean(datos[5].trim());
        boolean autorizadoGerente = Boolean.parseBoolean(datos[6].trim());

        ReembolsoInputDTO input = new ReembolsoInputDTO(
                tipo,
                folio,
                empleado,
                monto,
                fechaGasto,
                tieneComprobante,
                autorizadoGerente,
                fechaEvaluacion
        );

        ReembolsoOutputDTO output = Result.procesarReembolso(input);

        System.out.println("\n--- RESULTADO DEL PROCESAMIENTO ---");
        System.out.println("REEMBOLSO " + output.getFolio());
        System.out.println("TIPO " + output.getTipo());
        System.out.println("RESULTADO " + output.getResultado());
    }
}