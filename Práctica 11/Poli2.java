import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;




interface Publicable {

    ResultadoPublicacion publicar();

    Long getId();

    String getAutor();
}


abstract class PublicacionDTO {

    private final Long id;
    private final String autor;
    private final String titulo;

    private final int meGusta;
    private final int comentarios;
    private final int compartidos;


    protected PublicacionDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos
    ) {

        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.meGusta = meGusta;
        this.comentarios = comentarios;
        this.compartidos = compartidos;
    }


    public Long getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getMeGusta() {
        return meGusta;
    }

    public int getComentarios() {
        return comentarios;
    }

    public int getCompartidos() {
        return compartidos;
    }
}




final class PublicacionTextoDTO
        extends PublicacionDTO {

    private final String texto;


    public PublicacionTextoDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            String texto
    ) {

        super(
                id,
                autor,
                titulo,
                meGusta,
                comentarios,
                compartidos
        );

        this.texto = texto;
    }


    public String getTexto() {
        return texto;
    }
}


final class PublicacionImagenDTO
        extends PublicacionDTO {

    private final String descripcionImagen;
    private final String formato;


    public PublicacionImagenDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            String descripcionImagen,
            String formato
    ) {

        super(
                id,
                autor,
                titulo,
                meGusta,
                comentarios,
                compartidos
        );

        this.descripcionImagen =
                descripcionImagen;

        this.formato =
                formato;
    }


    public String getDescripcionImagen() {
        return descripcionImagen;
    }

    public String getFormato() {
        return formato;
    }
}


final class PublicacionVideoDTO
        extends PublicacionDTO {

    private final int duracionSegundos;
    private final int visualizaciones;


    public PublicacionVideoDTO(
            Long id,
            String autor,
            String titulo,
            int meGusta,
            int comentarios,
            int compartidos,
            int duracionSegundos,
            int visualizaciones
    ) {

        super(
                id,
                autor,
                titulo,
                meGusta,
                comentarios,
                compartidos
        );

        this.duracionSegundos =
                duracionSegundos;

        this.visualizaciones =
                visualizaciones;
    }


    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public int getVisualizaciones() {
        return visualizaciones;
    }
}



enum EstadoPublicacion {
    RECHAZADA, PUBLICADA, DESTACADA, VIRAL
}

class ResultadoPublicacion {
    private final Long idPublicacion;
    private final String descripcion;
    private final double relevancia;
    private final EstadoPublicacion estado;

    public ResultadoPublicacion(Long idPublicacion, String descripcion, double relevancia, EstadoPublicacion estado) {
        this.idPublicacion = idPublicacion;
        this.descripcion = descripcion;
        this.relevancia = relevancia;
        this.estado = estado;
    }

    public Long getIdPublicacion() {
        return idPublicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getRelevancia() {
        return relevancia;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }
}

abstract class Publicacion implements Publicable {
    protected final Long id;
    protected final String autor;
    protected final String titulo;
    protected final int meGusta;
    protected final int comentarios;
    protected final int compartidos;

    protected Publicacion(Long id, String autor, String titulo, int meGusta, int comentarios, int compartidos) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.meGusta = meGusta;
        this.comentarios = comentarios;
        this.compartidos = compartidos;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAutor() {
        return autor;
    }

    @Override
    public final ResultadoPublicacion publicar() {
        if (!validarDatosGenerales() || !validarContenido()) {
            return new ResultadoPublicacion(id, getDescripcion(), 0.0, EstadoPublicacion.RECHAZADA);
        }
        double relevancia = calcularRelevancia();
        EstadoPublicacion estado = determinarEstado(relevancia);
        return new ResultadoPublicacion(id, getDescripcion(), relevancia, estado);
    }

    protected boolean validarDatosGenerales() {
        return id != null && id > 0 && autor != null && !autor.isBlank() &&
               titulo != null && !titulo.isBlank() &&
               meGusta >= 0 && comentarios >= 0 && compartidos >= 0;
    }

    protected abstract boolean validarContenido();

    protected abstract double calcularRelevancia();

    public abstract String getDescripcion();

    protected EstadoPublicacion determinarEstado(double relevancia) {
        if (relevancia < 50.0) {
            return EstadoPublicacion.PUBLICADA;
        } else if (relevancia < 300.0) {
            return EstadoPublicacion.DESTACADA;
        } else {
            return EstadoPublicacion.VIRAL;
        }
    }
}

final class PublicacionTexto extends Publicacion {
    private final String texto;

    public PublicacionTexto(Long id, String autor, String titulo, int meGusta, int comentarios, int compartidos, String texto) {
        super(id, autor, titulo, meGusta, comentarios, compartidos);
        this.texto = texto;
    }

    @Override
    protected boolean validarContenido() {
        return texto != null && !texto.isBlank();
    }

    @Override
    protected double calcularRelevancia() {
        return meGusta + (comentarios * 3.0) + (compartidos * 5.0);
    }

    @Override
    public String getDescripcion() {
        return "Texto | " + titulo + " | " + autor;
    }
}

final class PublicacionImagen extends Publicacion {
    private final String descripcionImagen;
    private final String formato;

    public PublicacionImagen(Long id, String autor, String titulo, int meGusta, int comentarios, int compartidos, String descripcionImagen, String formato) {
        super(id, autor, titulo, meGusta, comentarios, compartidos);
        this.descripcionImagen = descripcionImagen;
        this.formato = formato;
    }

    @Override
    protected boolean validarContenido() {
        return descripcionImagen != null && !descripcionImagen.isBlank() &&
               formato != null && (formato.equals("JPG") || formato.equals("PNG") || formato.equals("WEBP"));
    }

    @Override
    protected double calcularRelevancia() {
        return (meGusta * 2.0) + (comentarios * 3.0) + (compartidos * 5.0);
    }

    @Override
    public String getDescripcion() {
        return "Imagen | " + titulo + " | " + descripcionImagen + " | " + formato + " | " + autor;
    }
}

final class PublicacionVideo extends Publicacion {
    private final int duracionSegundos;
    private final int visualizaciones;

    public PublicacionVideo(Long id, String autor, String titulo, int meGusta, int comentarios, int compartidos, int duracionSegundos, int visualizaciones) {
        super(id, autor, titulo, meGusta, comentarios, compartidos);
        this.duracionSegundos = duracionSegundos;
        this.visualizaciones = visualizaciones;
    }

    @Override
    protected boolean validarContenido() {
        return duracionSegundos > 0 && visualizaciones >= 0;
    }

    @Override
    protected double calcularRelevancia() {
        double base = (meGusta * 2.0) + (comentarios * 3.0) + (compartidos * 5.0);
        if (visualizaciones > 0) {
            base += (visualizaciones / 100.0) + 15.0;
        }
        return base;
    }

    @Override
    public String getDescripcion() {
        return "Video | " + titulo + " | " + duracionSegundos + " s | " + autor;
    }
}

class Feed {
    private final List<ResultadoPublicacion> resultados = new ArrayList<>();

    public ResultadoPublicacion procesar(Publicable publicacion) {
        ResultadoPublicacion resultado = publicacion.publicar();
        resultados.add(resultado);
        return resultado;
    }

    public void procesar(List<Publicable> publicaciones) {
        for (Publicable p : publicaciones) {
            procesar(p);
        }
    }

    public List<ResultadoPublicacion> generarRanking() {
        List<ResultadoPublicacion> copia = new ArrayList<>(resultados);
        copia.sort((r1, r2) -> Double.compare(r2.getRelevancia(), r1.getRelevancia()));
        return copia;
    }

    public List<ResultadoPublicacion> obtenerVirales() {
        List<ResultadoPublicacion> virales = new ArrayList<>();
        for (ResultadoPublicacion r : resultados) {
            if (r.getEstado() == EstadoPublicacion.VIRAL) {
                virales.add(r);
            }
        }
        return virales;
    }
}

class Result {

    public static Publicable convertir(
            PublicacionTextoDTO entrada
    ) {
        return new PublicacionTexto(
                entrada.getId(),
                entrada.getAutor(),
                entrada.getTitulo(),
                entrada.getMeGusta(),
                entrada.getComentarios(),
                entrada.getCompartidos(),
                entrada.getTexto()
        );
    }


    public static Publicable convertir(
            PublicacionImagenDTO entrada
    ) {
        return new PublicacionImagen(
                entrada.getId(),
                entrada.getAutor(),
                entrada.getTitulo(),
                entrada.getMeGusta(),
                entrada.getComentarios(),
                entrada.getCompartidos(),
                entrada.getDescripcionImagen(),
                entrada.getFormato()
        );
    }


    public static Publicable convertir(
            PublicacionVideoDTO entrada
    ) {
        return new PublicacionVideo(
                entrada.getId(),
                entrada.getAutor(),
                entrada.getTitulo(),
                entrada.getMeGusta(),
                entrada.getComentarios(),
                entrada.getCompartidos(),
                entrada.getDuracionSegundos(),
                entrada.getVisualizaciones()
        );
    }
}

final class HackerRankVibe {

    private HackerRankVibe() {
    }


    public static void ejecutar()
            throws Exception {

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                System.in
                        )
                );


        String primeraLinea =
                reader.readLine();


        if (primeraLinea == null
                || primeraLinea.isBlank()) {

            return;
        }


        int numeroPublicaciones =
                Integer.parseInt(
                        primeraLinea.trim()
                );


        List<Publicable> publicaciones =
                new ArrayList<>();




        for (int i = 0;
             i < numeroPublicaciones;
             i++) {

            String linea =
                    reader.readLine();


            if (linea == null) {

                throw new IllegalArgumentException(
                        "Faltan publicaciones"
                );
            }


            Publicable publicacion =
                    convertirEntrada(
                            linea
                    );


            if (publicacion == null) {

                throw new IllegalStateException(
                        "La conversion regreso null"
                );
            }


            publicaciones.add(
                    publicacion
            );
        }


        Feed feedIndividual =
                new Feed();


        for (Publicable publicacion
                : publicaciones) {

            ResultadoPublicacion resultado =
                    feedIndividual.procesar(
                            publicacion
                    );


            imprimirResultado(
                    resultado
            );
        }


        Feed feed =
                new Feed();


        feed.procesar(
                publicaciones
        );

        System.out.println(
                "RANKING"
        );


        List<ResultadoPublicacion> ranking =
                feed.generarRanking();


        for (ResultadoPublicacion resultado
                : ranking) {

            imprimirRanking(
                    resultado
            );
        }
        
        System.out.println(
                "VIRALES"
        );


        List<ResultadoPublicacion> virales =
                feed.obtenerVirales();


        for (ResultadoPublicacion resultado
                : virales) {

            imprimirViral(
                    resultado
            );
        }
    }


    private static Publicable convertirEntrada(
            String linea
    ) {

        if (linea == null
                || linea.isBlank()) {

            throw new IllegalArgumentException(
                    "La linea no puede estar vacia"
            );
        }


        String[] partes =
                linea.split(
                        "\\|",
                        -1
                );


        String tipo =
                partes[0]
                        .trim()
                        .toUpperCase();


        return switch (tipo) {
            case "TEXTO" -> convertirTexto(partes);
            case "IMAGEN" -> convertirImagen(partes);
            case "VIDEO" -> convertirVideo(partes);
            default -> throw new IllegalArgumentException(
                    "Tipo de publicacion invalido"
            );
        };
    }

    private static Publicable convertirTexto(
            String[] partes
    ) {

        if (partes.length != 8) {

            throw new IllegalArgumentException(
                    "Formato TEXTO invalido"
            );
        }


        PublicacionTextoDTO entrada =
                new PublicacionTextoDTO(

                        Long.valueOf(
                                partes[1].trim()
                        ),

                        partes[2].trim(),

                        partes[3].trim(),

                        Integer.parseInt(
                                partes[4].trim()
                        ),

                        Integer.parseInt(
                                partes[5].trim()
                        ),

                        Integer.parseInt(
                                partes[6].trim()
                        ),

                        partes[7].trim()
                );


        return Result.convertir(
                entrada
        );
    }

    private static Publicable convertirImagen(
            String[] partes
    ) {

        if (partes.length != 9) {

            throw new IllegalArgumentException(
                    "Formato IMAGEN invalido"
            );
        }


        PublicacionImagenDTO entrada =
                new PublicacionImagenDTO(

                        Long.valueOf(
                                partes[1].trim()
                        ),

                        partes[2].trim(),

                        partes[3].trim(),

                        Integer.parseInt(
                                partes[4].trim()
                        ),

                        Integer.parseInt(
                                partes[5].trim()
                        ),

                        Integer.parseInt(
                                partes[6].trim()
                        ),

                        partes[7].trim(),

                        partes[8]
                                .trim()
                                .toUpperCase()
                );


        return Result.convertir(
                entrada
        );
    }


    private static Publicable convertirVideo(
            String[] partes
    ) {

        if (partes.length != 9) {

            throw new IllegalArgumentException(
                    "Formato VIDEO invalido"
            );
        }


        PublicacionVideoDTO entrada =
                new PublicacionVideoDTO(

                        Long.valueOf(
                                partes[1].trim()
                        ),

                        partes[2].trim(),

                        partes[3].trim(),

                        Integer.parseInt(
                                partes[4].trim()
                        ),

                        Integer.parseInt(
                                partes[5].trim()
                        ),

                        Integer.parseInt(
                                partes[6].trim()
                        ),

                        Integer.parseInt(
                                partes[7].trim()
                        ),

                        Integer.parseInt(
                                partes[8].trim()
                        )
                );


        return Result.convertir(
                entrada
        );
    }


    private static void imprimirResultado(
            ResultadoPublicacion resultado
    ) {

        System.out.printf(
                "%d|%s|%.2f|%s%n",

                resultado.getIdPublicacion(),

                resultado.getDescripcion(),

                resultado.getRelevancia(),

                resultado.getEstado()
        );
    }


    private static void imprimirRanking(
            ResultadoPublicacion resultado
    ) {

        System.out.printf(
                "%d|%.2f|%s%n",

                resultado.getIdPublicacion(),

                resultado.getRelevancia(),

                resultado.getEstado()
        );
    }


    private static void imprimirViral(
            ResultadoPublicacion resultado
    ) {

        System.out.printf(
                "%d|%s|%.2f%n",

                resultado.getIdPublicacion(),

                resultado.getDescripcion(),

                resultado.getRelevancia()
        );
    }
}


public class Poli2 {

    public static void main(String[] args)
            throws Exception {

        HackerRankVibe.ejecutar();
    }
}