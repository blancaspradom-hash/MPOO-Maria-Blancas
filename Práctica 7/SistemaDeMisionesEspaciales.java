import java.io.*;
import java.util.*;

enum TipoDestino {
    PLANETA, 
    SATELITE_NATURAL
}

enum TipoRecurso {
    DRON,
    SATELITE_ARTIFICIAL,
    CARGA,
    TRIPULANTE
}

enum TipoMision {
    EXPLORACION,
    RESCATE,
    TRANSPORTE
}

enum Capacidad {
    COMUNICABLE,
    RASTREABLE,
    OPERABLE,
    ESCANEABLE
}

enum EstadoValidacion {
    OK,
    FALLIDA
}

enum EstadoPaso {
    OK,
    NO_EJECUTADO
}

enum ResultadoObjetivo {
    ESCANEO_COMPLETADO,
    RESCATE_COMPLETADO,
    CARGA_ENTREGADA,
    NO_EJECUTADO
}

final class DestinoDTO {
    private final TipoDestino tipo;
    private final String codigo;
    private final String nombre;
    private final double gravedad;
    private final String datoEspecifico;

    public DestinoDTO(TipoDestino tipo, String codigo, String nombre, double gravedad, String datoEspecifico) {
        this.tipo = tipo;
        this.codigo = codigo;
        this.nombre = nombre;
        this.gravedad = gravedad;
        this.datoEspecifico = datoEspecifico;
    }

    public TipoDestino getTipo() { return tipo; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getGravedad() { return gravedad; }
    public String getDatoEspecifico() { return datoEspecifico; }
}

final class NaveDTO {
    private final String id;
    private final String nombre;
    private final String posicion;
    private final boolean operativa;

    public NaveDTO(String id, String nombre, String posicion, boolean operativa) {
        this.id = id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.operativa = operativa;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPosicion() { return posicion; }
    public boolean isOperativa() { return operativa; }
}

abstract class RecursoDTO {
    private final TipoRecurso tipo;
    private final String id;

    protected RecursoDTO(TipoRecurso tipo, String id) {
        this.tipo = tipo;
        this.id = id;
    }

    public TipoRecurso getTipo() { return tipo; }
    public String getId() { return id; }
}

final class DronDTO extends RecursoDTO {
    private final String modelo;
    private final String posicion;
    private final boolean operativo;

    public DronDTO(String id, String modelo, String posicion, boolean operativo) {
        super(TipoRecurso.DRON, id);
        this.modelo = modelo;
        this.posicion = posicion;
        this.operativo = operativo;
    }

    public String getModelo() { return modelo; }
    public String getPosicion() { return posicion; }
    public boolean isOperativo() { return operativo; }
}

final class SateliteArtificialDTO extends RecursoDTO {
    private final String nombre;
    private final String posicion;
    private final boolean operativo;

    public SateliteArtificialDTO(String id, String nombre, String posicion, boolean operativo) {
        super(TipoRecurso.SATELITE_ARTIFICIAL, id);
        this.nombre = nombre;
        this.posicion = posicion;
        this.operativo = operativo;
    }

    public String getNombre() { return nombre; }
    public String getPosicion() { return posicion; }
    public boolean isOperativo() { return operativo; }
}

final class CargaDTO extends RecursoDTO {
    private final String descripcion;
    private final double peso;
    private final String posicion;

    public CargaDTO(String id, String descripcion, double peso, String posicion) {
        super(TipoRecurso.CARGA, id);
        this.descripcion = descripcion;
        this.peso = peso;
        this.posicion = posicion;
    }

    public String getDescripcion() { return descripcion; }
    public double getPeso() { return peso; }
    public String getPosicion() { return posicion; }
}

final class TripulanteDTO extends RecursoDTO {
    private final String nombre;
    private final String posicion;

    public TripulanteDTO(String id, String nombre, String posicion) {
        super(TipoRecurso.TRIPULANTE, id);
        this.nombre = nombre;
        this.posicion = posicion;
    }

    public String getNombre() { return nombre; }
    public String getPosicion() { return posicion; }
}

final class MisionDTO {
    private final String id;
    private final String nombre;
    private final TipoMision tipo;
    private final List<String> idsRecursos;

    public MisionDTO(String id, String nombre, TipoMision tipo, List<String> idsRecursos) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.idsRecursos = new ArrayList<>(idsRecursos);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoMision getTipo() { return tipo; }
    public List<String> getIdsRecursos() { return new ArrayList<>(idsRecursos); }
}

final class EjecucionMisionDTO {
    private final EstadoValidacion validacion;
    private final EstadoPaso preparacion;
    private final EstadoPaso viaje;
    private final ResultadoObjetivo objetivo;
    private final EstadoPaso finalizacion;
    private final EstadoPaso reporte;

    public EjecucionMisionDTO(EstadoValidacion validacion, EstadoPaso preparacion, EstadoPaso viaje,
                              ResultadoObjetivo objetivo, EstadoPaso finalizacion, EstadoPaso reporte) {
        this.validacion = validacion;
        this.preparacion = preparacion;
        this.viaje = viaje;
        this.objetivo = objetivo;
        this.finalizacion = finalizacion;
        this.reporte = reporte;
    }

    public EstadoValidacion getValidacion() { return validacion; }
    public EstadoPaso getPreparacion() { return preparacion; }
    public EstadoPaso getViaje() { return viaje; }
    public ResultadoObjetivo getObjetivo() { return objetivo; }
    public EstadoPaso getFinalizacion() { return finalizacion; }
    public EstadoPaso getReporte() { return reporte; }
}

interface DestinoMapper {
    DestinoEspacial toEntity(DestinoDTO dto);
}

interface NaveMapper {
    Nave toEntity(NaveDTO dto);
}

interface RecursoMapper {
    Recurso toEntity(RecursoDTO dto);
}

interface MisionFactoryService {
    Mision crearMision(MisionDTO dto, Nave nave, DestinoEspacial destino, Map<String, Recurso> recursos);
}

interface CapacidadService {
    List<Capacidad> obtenerCapacidades(Object entidad);
}

interface Comunicable {
    String comunicar();
}

interface Rastreable {
    String obtenerPosicion();
}

interface Operable {
    boolean estaOperativo();
}

interface Escaneable {
    String escanear();
}

abstract class DestinoEspacial {
    protected String codigo;
    protected String nombre;
    protected double gravedad;

    public DestinoEspacial(String codigo, String nombre, double gravedad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.gravedad = gravedad;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getGravedad() { return gravedad; }
}

class Planeta extends DestinoEspacial {
    private final boolean tieneAtmosfera;

    public Planeta(String codigo, String nombre, double gravedad, boolean tieneAtmosfera) {
        super(codigo, nombre, gravedad);
        this.tieneAtmosfera = tieneAtmosfera;
    }

    public boolean tieneAtmosfera() { return tieneAtmosfera; }
}

class SateliteNatural extends DestinoEspacial {
    private final String orbitaA;

    public SateliteNatural(String codigo, String nombre, double gravedad, String orbitaA) {
        super(codigo, nombre, gravedad);
        this.orbitaA = orbitaA;
    }

    public String getOrbitaA() { return orbitaA; }
}

class Nave implements Comunicable, Rastreable, Operable {
    private final String id;
    private final String nombre;
    private final String posicion;
    private final boolean operativa;

    public Nave(String id, String nombre, String posicion, boolean operativa) {
        this.id = id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.operativa = operativa;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String comunicar() { return "Comunicando con nave " + nombre; }

    @Override
    public String obtenerPosicion() { return posicion; }

    @Override
    public boolean estaOperativo() { return operativa; }
}

abstract class Recurso {
    protected String id;

    protected Recurso(String id) {
        this.id = id;
    }

    public String getId() { return id; }
}

class Dron extends Recurso implements Comunicable, Rastreable, Operable, Escaneable {
    private final String modelo;
    private final String posicion;
    private final boolean operativo;

    public Dron(String id, String modelo, String posicion, boolean operativo) {
        super(id);
        this.modelo = modelo;
        this.posicion = posicion;
        this.operativo = operativo;
    }

    public String getModelo() { return modelo; }

    @Override
    public String comunicar() { return "Dron " + modelo + " " + id; }
    @Override
    public String obtenerPosicion() { return posicion; }
    @Override
    public boolean estaOperativo() { return operativo; }
    @Override
    public String escanear() { return "Escaneo completado por dron " + modelo; }
}

class SateliteArtificial extends Recurso implements Comunicable, Rastreable, Operable, Escaneable {
    private final String nombre;
    private final String posicion;
    private final boolean operativo;

    public SateliteArtificial(String id, String nombre, String posicion, boolean operativo) {
        super(id);
        this.nombre = nombre;
        this.posicion = posicion;
        this.operativo = operativo;
    }

    @Override
    public String comunicar() { return "Satelite " + nombre; }
    @Override
    public String obtenerPosicion() { return posicion; }
    @Override
    public boolean estaOperativo() { return operativo; }
    @Override
    public String escanear() { return "Escaneo completado por satelite"; }
}

class Carga extends Recurso implements Rastreable {
    private final String descripcion;
    private final double peso;
    private final String posicion;

    public Carga(String id, String descripcion, double peso, String posicion) {
        super(id);
        this.descripcion = descripcion;
        this.peso = peso;
        this.posicion = posicion;
    }

    public String getDescripcion() { return descripcion; }
    public double getPeso() { return peso; }

    @Override
    public String obtenerPosicion() { return posicion; }
}

class Tripulante extends Recurso implements Comunicable, Rastreable {
    private final String nombre;
    private final String posicion;

    public Tripulante(String id, String nombre, String posicion) {
        super(id);
        this.nombre = nombre;
        this.posicion = posicion;
    }

    @Override
    public String comunicar() { return "Tripulante " + nombre; }
    @Override
    public String obtenerPosicion() { return posicion; }
}

abstract class Mision {
    protected String id;
    protected String nombre;
    protected Nave nave;
    protected DestinoEspacial destino;
    protected List<Recurso> recursos;

    public Mision(String id, String nombre, Nave nave, DestinoEspacial destino, List<Recurso> recursos) {
        this.id = id;
        this.nombre = nombre;
        this.nave = nave;
        this.destino = destino;
        this.recursos = recursos;
    }

    public final EjecucionMisionDTO ejecutarMision() {
        if (!nave.estaOperativo()) {
            return new EjecucionMisionDTO(
                EstadoValidacion.FALLIDA,
                EstadoPaso.NO_EJECUTADO,
                EstadoPaso.NO_EJECUTADO,
                ResultadoObjetivo.NO_EJECUTADO,
                EstadoPaso.NO_EJECUTADO,
                EstadoPaso.NO_EJECUTADO
            );
        }

        prepararRecursos();
        iniciarViaje();
        ResultadoObjetivo resObj = ejecutarObjetivo();
        finalizarOperacion();
        generarReporte();

        return new EjecucionMisionDTO(
            EstadoValidacion.OK,
            EstadoPaso.OK,
            EstadoPaso.OK,
            resObj,
            EstadoPaso.OK,
            EstadoPaso.OK
        );
    }

    protected void prepararRecursos() {}
    protected void iniciarViaje() {}
    protected abstract ResultadoObjetivo ejecutarObjetivo();
    protected void finalizarOperacion() {}
    protected void generarReporte() {}
}

class MisionExploracion extends Mision {
    public MisionExploracion(String id, String nombre, Nave nave, DestinoEspacial destino, List<Recurso> recursos) {
        super(id, nombre, nave, destino, recursos);
    }

    @Override
    protected ResultadoObjetivo ejecutarObjetivo() {
        return ResultadoObjetivo.ESCANEO_COMPLETADO;
    }
}

class MisionRescate extends Mision {
    public MisionRescate(String id, String nombre, Nave nave, DestinoEspacial destino, List<Recurso> recursos) {
        super(id, nombre, nave, destino, recursos);
    }

    @Override
    protected ResultadoObjetivo ejecutarObjetivo() {
        return ResultadoObjetivo.RESCATE_COMPLETADO;
    }
}

class MisionTransporte extends Mision {
    public MisionTransporte(String id, String nombre, Nave nave, DestinoEspacial destino, List<Recurso> recursos) {
        super(id, nombre, nave, destino, recursos);
    }

    @Override
    protected ResultadoObjetivo ejecutarObjetivo() {
        return ResultadoObjetivo.CARGA_ENTREGADA;
    }
}

class DestinoMapperImpl implements DestinoMapper {
    @Override
    public DestinoEspacial toEntity(DestinoDTO dto) {
        if (dto.getTipo() == TipoDestino.PLANETA) {
            boolean atmos = Boolean.parseBoolean(dto.getDatoEspecifico());
            return new Planeta(dto.getCodigo(), dto.getNombre(), dto.getGravedad(), atmos);
        } else {
            return new SateliteNatural(dto.getCodigo(), dto.getNombre(), dto.getGravedad(), dto.getDatoEspecifico());
        }
    }
}

class NaveMapperImpl implements NaveMapper {
    @Override
    public Nave toEntity(NaveDTO dto) {
        return new Nave(dto.getId(), dto.getNombre(), dto.getPosicion(), dto.isOperativa());
    }
}

class RecursoMapperImpl implements RecursoMapper {
    @Override
    public Recurso toEntity(RecursoDTO dto) {
        return switch (dto) {
            case DronDTO d -> new Dron(d.getId(), d.getModelo(), d.getPosicion(), d.isOperativo());
            case SateliteArtificialDTO s ->
                    new SateliteArtificial(s.getId(), s.getNombre(), s.getPosicion(), s.isOperativo());
            case CargaDTO c -> new Carga(c.getId(), c.getDescripcion(), c.getPeso(), c.getPosicion());
            case TripulanteDTO t -> new Tripulante(t.getId(), t.getNombre(), t.getPosicion());
            default -> throw new IllegalArgumentException("Tipo de recurso desconocido");
        };
    }
}

class MisionFactoryServiceImpl implements MisionFactoryService {
    @Override
    public Mision crearMision(MisionDTO dto, Nave nave, DestinoEspacial destino, Map<String, Recurso> recursosMap) {
        List<Recurso> recursosMision = new ArrayList<>();
        for (String idRes : dto.getIdsRecursos()) {
            if (recursosMap.containsKey(idRes)) {
                recursosMision.add(recursosMap.get(idRes));
            }
        }

        return switch (dto.getTipo()) {
            case EXPLORACION -> new MisionExploracion(dto.getId(), dto.getNombre(), nave, destino, recursosMision);
            case RESCATE -> new MisionRescate(dto.getId(), dto.getNombre(), nave, destino, recursosMision);
            case TRANSPORTE -> new MisionTransporte(dto.getId(), dto.getNombre(), nave, destino, recursosMision);
            default -> throw new IllegalArgumentException("Tipo de mision no soportado");
        };
    }
}

class CapacidadServiceImpl implements CapacidadService {
    @Override
    public List<Capacidad> obtenerCapacidades(Object entidad) {
        List<Capacidad> caps = new ArrayList<>();
        if (entidad instanceof Comunicable) {
            caps.add(Capacidad.COMUNICABLE);
        }
        if (entidad instanceof Rastreable) {
            caps.add(Capacidad.RASTREABLE);
        }
        if (entidad instanceof Operable) {
            caps.add(Capacidad.OPERABLE);
        }
        if (entidad instanceof Escaneable) {
            caps.add(Capacidad.ESCANEABLE);
        }
        return caps;
    }
}

public class SistemaDeMisionesEspaciales {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String destinoInput = br.readLine();
        String naveInput = br.readLine();
        String recursosInput = br.readLine();
        String misionInput = br.readLine();

        String resultado = procesarMision(
                destinoInput,
                naveInput,
                recursosInput,
                misionInput
        );

        System.out.print(resultado);
    }

    public static String procesarMision(
            String destinoInput,
            String naveInput,
            String recursosInput,
            String misionInput) {

        DestinoDTO destinoDTO = parseDestino(destinoInput);
        NaveDTO naveDTO = parseNave(naveInput);
        List<RecursoDTO> recursosDTO = parseRecursos(recursosInput);
        MisionDTO misionDTO = parseMision(misionInput);

        DestinoMapper destinoMapper = new DestinoMapperImpl();
        NaveMapper naveMapper = new NaveMapperImpl();
        RecursoMapper recursoMapper = new RecursoMapperImpl();

        MisionFactoryService factory = new MisionFactoryServiceImpl();
        CapacidadService capacidadService = new CapacidadServiceImpl();

        DestinoEspacial destino = destinoMapper.toEntity(destinoDTO);
        Nave nave = naveMapper.toEntity(naveDTO);
        Map<String, Recurso> recursos = mapearRecursos(recursosDTO, recursoMapper);

        Mision mision = factory.crearMision(misionDTO, nave, destino, recursos);
        EjecucionMisionDTO ejecucion = mision.ejecutarMision();

        return construirSalida(
                destino,
                nave,
                recursos,
                misionDTO,
                ejecucion,
                capacidadService
        );
    }

    private static Map<String, Recurso> mapearRecursos(
            List<RecursoDTO> recursosDTO,
            RecursoMapper mapper) {

        Map<String, Recurso> recursos = new LinkedHashMap<>();

        for (RecursoDTO dto : recursosDTO) {
            Recurso recurso = mapper.toEntity(dto);
            recursos.put(recurso.getId(), recurso);
        }

        return recursos;
    }

    private static String construirSalida(
            DestinoEspacial destino,
            Nave nave,
            Map<String, Recurso> recursos,
            MisionDTO misionDTO,
            EjecucionMisionDTO ejecucion,
            CapacidadService capacidadService) {

        List<String> salida = new ArrayList<>();

        salida.add("MISION " + misionDTO.getId());
        salida.add("TIPO " + misionDTO.getTipo());
        salida.add(construirDestino(destino));
        salida.add(construirCapacidades(nave.getId(), nave, capacidadService));

        for (String id : misionDTO.getIdsRecursos()) {
            Recurso recurso = recursos.get(id);
            if (recurso != null) {
                salida.add(construirCapacidades(recurso.getId(), recurso, capacidadService));
            }
        }

        if (ejecucion.getValidacion() == EstadoValidacion.FALLIDA) {
            salida.add("VALIDACION FALLIDA");
            salida.add("MISION ABORTADA");
            return String.join("\n", salida);
        }

        salida.add("VALIDACION OK");

        if (ejecucion.getPreparacion() == EstadoPaso.OK) {
            salida.add("PREPARACION OK");
        }

        if (ejecucion.getViaje() == EstadoPaso.OK) {
            salida.add("VIAJE " + destino.getNombre());
        }

        if (ejecucion.getObjetivo() != ResultadoObjetivo.NO_EJECUTADO) {
            salida.add("OBJETIVO " + ejecucion.getObjetivo().name());
        }

        if (ejecucion.getFinalizacion() == EstadoPaso.OK) {
            salida.add("FINALIZACION OK");
        }

        if (ejecucion.getReporte() == EstadoPaso.OK) {
            salida.add("REPORTE COMPLETADO");
        }

        return String.join("\n", salida);
    }

    private static String construirDestino(DestinoEspacial destino) {
        if (destino instanceof Planeta planeta) {
            return "DESTINO PLANETA " + planeta.getNombre() + " ATMOSFERA " + (planeta.tieneAtmosfera() ? "SI" : "NO");
        }
        if (destino instanceof SateliteNatural satelite) {
            return "DESTINO SATELITE_NATURAL " + satelite.getNombre() + " ORBITA " + satelite.getOrbitaA();
        }
        throw new IllegalArgumentException("Destino no valido");
    }

    private static String construirCapacidades(String id, Object entidad, CapacidadService servicio) {
        List<Capacidad> capacidades = servicio.obtenerCapacidades(entidad);
        List<String> nombres = new ArrayList<>();

        for (Capacidad capacidad : capacidades) {
            nombres.add(capacidad.name());
        }

        return "CAPACIDADES " + id + " " + String.join(",", nombres);
    }

    private static DestinoDTO parseDestino(String input) {
        String[] d = input.split("\\|", 5);
        return new DestinoDTO(
                TipoDestino.valueOf(d[0].trim()),
                d[1].trim(),
                d[2].trim(),
                Double.parseDouble(d[3].trim()),
                d[4].trim()
        );
    }

    private static NaveDTO parseNave(String input) {
        String[] d = input.split("\\|", 4);
        return new NaveDTO(
                d[0].trim(),
                d[1].trim(),
                d[2].trim(),
                Boolean.parseBoolean(d[3].trim())
        );
    }

    private static List<RecursoDTO> parseRecursos(String input) {
        List<RecursoDTO> recursos = new ArrayList<>();

        if (input == null || input.trim().isEmpty() || input.equals("-")) {
            return recursos;
        }

        String[] registros = input.split(";");

        for (String registro : registros) {
            String[] d = registro.split("\\|");
            TipoRecurso tipo = TipoRecurso.valueOf(d[0].trim());

            switch (tipo) {
                case DRON -> recursos.add(new DronDTO(d[1].trim(), d[2].trim(), d[3].trim(), Boolean.parseBoolean(d[4].trim())));
                case SATELITE_ARTIFICIAL -> recursos.add(new SateliteArtificialDTO(d[1].trim(), d[2].trim(), d[3].trim(), Boolean.parseBoolean(d[4].trim())));
                case CARGA -> recursos.add(new CargaDTO(d[1].trim(), d[2].trim(), Double.parseDouble(d[3].trim()), d[4].trim()));
                case TRIPULANTE -> recursos.add(new TripulanteDTO(d[1].trim(), d[2].trim(), d[3].trim()));
            }
        }

        return recursos;
    }

    private static MisionDTO parseMision(String input) {
        String[] d = input.split("\\|", 4);
        List<String> ids = new ArrayList<>();

        if (d.length == 4 && !d[3].trim().isEmpty() && !d[3].equals("-")) {
            String[] recursos = d[3].split(",");
            for (String id : recursos) {
                ids.add(id.trim());
            }
        }

        return new MisionDTO(
                d[0].trim(),
                d[1].trim(),
                TipoMision.valueOf(d[2].trim()),
                ids
        );
    }
}