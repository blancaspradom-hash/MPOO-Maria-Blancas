import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

interface ServicioEnvio {
    double calcularCosto();
    String getDescripcion();
}

enum TipoEnvio {
    TERRESTRE,
    AEREO
}

enum ServicioAdicional {
    SEGURO,
    EXPRESS
}

class EntradaEnvioDTO {
    private final TipoEnvio tipo;
    private final String destinatario;
    private final double pesoKg;
    private final double distanciaKm;
    private final List<ServicioAdicional> servicios;

    public EntradaEnvioDTO(
            TipoEnvio tipo,
            String destinatario,
            double pesoKg,
            double distanciaKm,
            List<ServicioAdicional> servicios
    ) {
        this.tipo = tipo;
        this.destinatario = destinatario;
        this.pesoKg = pesoKg;
        this.distanciaKm = distanciaKm;
        this.servicios =
                Collections.unmodifiableList(
                        new ArrayList<>(servicios)
                );
    }

    public TipoEnvio getTipo() {
        return tipo;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public List<ServicioAdicional> getServicios() {
        return servicios;
    }
}

/* =========================================================
   DESARROLLA TU SOLUCION AQUI
   ========================================================= */

abstract class Envio implements ServicioEnvio {
    protected final double pesoKg;
    protected final double distanciaKm;

    protected Envio(double pesoKg, double distanciaKm) {
        this.pesoKg = pesoKg;
        this.distanciaKm = distanciaKm;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }
}

class EnvioTerrestre extends Envio {
    private static final double FACTOR_DISTANCIA = 0.50;
    private static final double FACTOR_PESO = 2.0;

    public EnvioTerrestre(double pesoKg, double distanciaKm) {
        super(pesoKg, distanciaKm);
    }

    @Override
    public double calcularCosto() {
        return (distanciaKm * FACTOR_DISTANCIA) + (pesoKg * FACTOR_PESO);
    }

    @Override
    public String getDescripcion() {
        return "Envio terrestre";
    }
}

class EnvioAereo extends Envio {
    private static final double FACTOR_DISTANCIA = 1.20;
    private static final double FACTOR_PESO = 5.0;

    public EnvioAereo(double pesoKg, double distanciaKm) {
        super(pesoKg, distanciaKm);
    }

    @Override
    public double calcularCosto() {
        return (distanciaKm * FACTOR_DISTANCIA) + (pesoKg * FACTOR_PESO);
    }

    @Override
    public String getDescripcion() {
        return "Envio aereo";
    }
}

abstract class ServicioAdicionalDecorator implements ServicioEnvio {
    protected final ServicioEnvio servicioEnvio;

    protected ServicioAdicionalDecorator(ServicioEnvio servicioEnvio) {
        this.servicioEnvio = servicioEnvio;
    }

    @Override
    public double calcularCosto() {
        return servicioEnvio.calcularCosto();
    }

    @Override
    public String getDescripcion() {
        return servicioEnvio.getDescripcion();
    }
}

class SeguroEnvio extends ServicioAdicionalDecorator {
    private static final double MULTIPLICADOR_SEGURO = 1.10;

    public SeguroEnvio(ServicioEnvio servicioEnvio) {
        super(servicioEnvio);
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() * MULTIPLICADOR_SEGURO;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Seguro";
    }
}

class EntregaExpress extends ServicioAdicionalDecorator {
    private static final double RECARGO_EXPRESS = 100.0;

    public EntregaExpress(ServicioEnvio servicioEnvio) {
        super(servicioEnvio);
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + RECARGO_EXPRESS;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Express";
    }
}

class CentroEnvios {
    public String procesar(ServicioEnvio envio) {
        String costoFormateado = String.format(Locale.US, "%.2f", envio.calcularCosto());
        return envio.getDescripcion() + "|" + costoFormateado;
    }

    public List<String> procesar(List<ServicioEnvio> envios) {
        List<String> resultados = new ArrayList<>();
        for (ServicioEnvio envio : envios) {
            resultados.add(procesar(envio));
        }
        return resultados;
    }
}

class Result {
    public static List<String> procesar(List<EntradaEnvioDTO> entradas) {
        List<ServicioEnvio> envios = new ArrayList<>();

        for (EntradaEnvioDTO entrada : entradas) {
            ServicioEnvio envio;
            if (entrada.getTipo() == TipoEnvio.TERRESTRE) {
                envio = new EnvioTerrestre(entrada.getPesoKg(), entrada.getDistanciaKm());
            } else {
                envio = new EnvioAereo(entrada.getPesoKg(), entrada.getDistanciaKm());
            }

            for (ServicioAdicional servicio : entrada.getServicios()) {
                if (servicio == ServicioAdicional.SEGURO) {
                    envio = new SeguroEnvio(envio);
                } else if (servicio == ServicioAdicional.EXPRESS) {
                    envio = new EntregaExpress(envio);
                }
            }
            envios.add(envio);
        }

        return new CentroEnvios().procesar(envios);
    }
}

public class Polimorfismo {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String primeraLinea = reader.readLine();

        if (primeraLinea == null || primeraLinea.isBlank()) {
            return;
        }

        int numeroEnvios = Integer.parseInt(primeraLinea.trim());
        List<EntradaEnvioDTO> entradas = new ArrayList<>();

        for (int i = 0; i < numeroEnvios; i++) {
            String linea = reader.readLine();
            if (linea == null) {
                throw new IllegalArgumentException("Faltan datos de entrada");
            }
            EntradaEnvioDTO entrada = convertirEntrada(linea);
            entradas.add(entrada);
        }

        List<String> resultados = Result.procesar(entradas);

        if (resultados == null) {
            throw new IllegalStateException("Result.procesar() regreso null");
        }

        for (String resultado : resultados) {
            System.out.println(resultado);
        }
    }

    private static EntradaEnvioDTO convertirEntrada(String linea) {
        if (linea == null || linea.isBlank()) {
            throw new IllegalArgumentException("La linea de entrada no puede estar vacia");
        }

        String[] partes = linea.split("\\|", -1);

        if (partes.length != 5) {
            throw new IllegalArgumentException("Formato de entrada invalido: " + linea);
        }

        TipoEnvio tipo = TipoEnvio.valueOf(
                partes[0].trim().toUpperCase(Locale.ROOT)
        );

        String destinatario = partes[1].trim();

        if (destinatario.isBlank()) {
            throw new IllegalArgumentException("El destinatario no puede estar vacio");
        }

        double pesoKg = Double.parseDouble(partes[2].trim());
        double distanciaKm = Double.parseDouble(partes[3].trim());

        List<ServicioAdicional> servicios = convertirServicios(partes[4]);

        return new EntradaEnvioDTO(
                tipo,
                destinatario,
                pesoKg,
                distanciaKm,
                servicios
        );
    }

    private static List<ServicioAdicional> convertirServicios(String valor) {
        List<ServicioAdicional> servicios = new ArrayList<>();

        if (valor == null) {
            return servicios;
        }

        String texto = valor.trim();

        if (texto.isEmpty() || texto.equalsIgnoreCase("NINGUNO")) {
            return servicios;
        }

        String[] elementos = texto.split("\\+");

        for (String elemento : elementos) {
            String nombreServicio = elemento.trim().toUpperCase(Locale.ROOT);
            ServicioAdicional servicio = ServicioAdicional.valueOf(nombreServicio);
            servicios.add(servicio);
        }

        return servicios;
    }
}
