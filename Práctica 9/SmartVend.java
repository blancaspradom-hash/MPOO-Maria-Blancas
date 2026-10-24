import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

enum CategoriaProducto {
    BEBIDA,
    SNACK
}

enum ResultadoRegistro {
    PRODUCTO_REGISTRADO,
    CODIGO_DUPLICADO,
    PRODUCTO_INVALIDO,
    PRECIO_INVALIDO
}

enum ResultadoReabastecimiento {
    REABASTECIMIENTO_REALIZADO,
    PRODUCTO_NO_ENCONTRADO,
    CANTIDAD_INVALIDA
}

enum ResultadoEntrega {
    PRODUCTO_PREPARADO,
    PRODUCTO_NO_ENCONTRADO,
    SIN_EXISTENCIAS
}

enum EstadoCobro {
    COBRO_REALIZADO,
    MONTO_INVALIDO,
    SIN_FONDOS_SUFICIENTES
}

enum EstadoCompra {
    VENTA_REALIZADA,
    PRODUCTO_NO_ENCONTRADO,
    SIN_EXISTENCIAS,
    PAGO_INSUFICIENTE,
    SIN_FONDOS_SUFICIENTES
}

class ProductoInventario {

    private final Producto producto;
    private final double precio;
    private final int existencias;

    public ProductoInventario(
            Producto producto,
            double precio,
            int existencias
    ) {
        this.producto = producto;
        this.precio = precio;
        this.existencias = existencias;
    }

    public Producto getProducto() {
        return producto;
    }

    public double getPrecio() {
        return precio;
    }

    public int getExistencias() {
        return existencias;
    }
}

class ResultadoCobro {

    private final EstadoCobro estado;
    private final double cambio;

    public ResultadoCobro(
            EstadoCobro estado,
            double cambio
    ) {
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

class ResultadoCompra {

    private final EstadoCompra estado;
    private final Producto producto;
    private final double cambio;

    public ResultadoCompra(
            EstadoCompra estado,
            Producto producto,
            double cambio
    ) {
        this.estado = estado;
        this.producto = producto;
        this.cambio = cambio;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public Producto getProducto() {
        return producto;
    }

    public double getCambio() {
        return cambio;
    }
}

class RegistroProductoDTO {

    private final String codigo;
    private final String nombre;
    private final LocalDate fechaCaducidad;
    private final CategoriaProducto categoria;
    private final double precio;

    public RegistroProductoDTO(
            String codigo,
            String nombre,
            LocalDate fechaCaducidad,
            CategoriaProducto categoria,
            double precio
    ) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaCaducidad = fechaCaducidad;
        this.categoria = categoria;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }
}

class ReabastecimientoDTO {

    private final String codigo;
    private final int cantidad;

    public ReabastecimientoDTO(
            String codigo,
            int cantidad
    ) {
        this.codigo = codigo;
        this.cantidad = cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getCantidad() {
        return cantidad;
    }
}

class CompraDTO {

    private final String codigoProducto;
    private final double cantidadRecibida;

    public CompraDTO(
            String codigoProducto,
            double cantidadRecibida
    ) {
        this.codigoProducto = codigoProducto;
        this.cantidadRecibida = cantidadRecibida;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public double getCantidadRecibida() {
        return cantidadRecibida;
    }
}

interface Inventario {

    ResultadoRegistro registrarProducto(
            Producto producto,
            double precio
    );

    Optional<ProductoInventario> buscarProducto(
            String codigo
    );

    ResultadoReabastecimiento reabastecer(
            String codigo,
            int cantidad
    );

    ResultadoEntrega despachar(
            String codigo
    );
}

interface Monedero {

    ResultadoCobro cobrar(
            double precio,
            double cantidadRecibida
    );

    double consultarFondos();
}

interface MaquinaDespachadora {

    ResultadoRegistro registrarProducto(
            RegistroProductoDTO producto
    );

    Optional<ProductoInventario> buscarProducto(
            String codigo
    );

    ResultadoReabastecimiento reabastecer(
            ReabastecimientoDTO solicitud
    );

    ResultadoCompra comprar(
            CompraDTO solicitud
    );

    double consultarFondos();
}

class Producto {
    private final String codigo;
    private final String nombre;
    private final LocalDate fechaCaducidad;
    private final CategoriaProducto categoria;

    public Producto(Builder builder) {
        this.codigo = builder.codigo;
        this.nombre = builder.nombre;
        this.fechaCaducidad = builder.fechaCaducidad;
        this.categoria = builder.categoria;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public CategoriaProducto getCategoria() { return categoria; }

    public static class Builder {
        private String codigo;
        private String nombre;
        private LocalDate fechaCaducidad;
        private CategoriaProducto categoria;

        public Builder codigo(String codigo) { this.codigo = codigo; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder fechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; return this; }
        public Builder categoria(CategoriaProducto categoria) { this.categoria = categoria; return this; }

        public Producto build() {
            return new Producto(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

class InventarioMemoria implements Inventario {
    private final Map<String, ProductoInventario> map = new HashMap<>();

    @Override
    public ResultadoRegistro registrarProducto(Producto producto, double precio) {
        if (producto == null || producto.getCodigo() == null) return ResultadoRegistro.PRODUCTO_INVALIDO;
        if (precio <= 0) return ResultadoRegistro.PRECIO_INVALIDO;
        if (map.containsKey(producto.getCodigo())) return ResultadoRegistro.CODIGO_DUPLICADO;
        map.put(producto.getCodigo(), new ProductoInventario(producto, precio, 0));
        return ResultadoRegistro.PRODUCTO_REGISTRADO;
    }

    @Override
    public Optional<ProductoInventario> buscarProducto(String codigo) {
        return Optional.ofNullable(map.get(codigo));
    }

    @Override
    public ResultadoReabastecimiento reabastecer(String codigo, int cantidad) {
        if (cantidad <= 0) return ResultadoReabastecimiento.CANTIDAD_INVALIDA;
        ProductoInventario pi = map.get(codigo);
        if (pi == null) return ResultadoReabastecimiento.PRODUCTO_NO_ENCONTRADO;
        map.put(codigo, new ProductoInventario(pi.getProducto(), pi.getPrecio(), pi.getExistencias() + cantidad));
        return ResultadoReabastecimiento.REABASTECIMIENTO_REALIZADO;
    }

    @Override
    public ResultadoEntrega despachar(String codigo) {
        ProductoInventario pi = map.get(codigo);
        if (pi == null) return ResultadoEntrega.PRODUCTO_NO_ENCONTRADO;
        if (pi.getExistencias() <= 0) return ResultadoEntrega.SIN_EXISTENCIAS;
        map.put(codigo, new ProductoInventario(pi.getProducto(), pi.getPrecio(), pi.getExistencias() - 1));
        return ResultadoEntrega.PRODUCTO_PREPARADO;
    }
}

class MonederoSimple implements Monedero {
    private double fondos;

    public MonederoSimple(double fondosIniciales) {
        this.fondos = fondosIniciales;
    }

    @Override
    public double consultarFondos() {
        return fondos;
    }

    @Override
    public ResultadoCobro cobrar(double precio, double cantidadRecibida) {
        if (cantidadRecibida < precio) return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, 0.0);
        double cambio = cantidadRecibida - precio;
        if (fondos < cambio) return new ResultadoCobro(EstadoCobro.SIN_FONDOS_SUFICIENTES, 0.0);
        fondos += precio;
        return new ResultadoCobro(EstadoCobro.COBRO_REALIZADO, cambio);
    }
}

class MaquinaExpendedora implements MaquinaDespachadora {
    private final Inventario inventario;
    private final Monedero monedero;

    private MaquinaExpendedora(Builder builder) {
        this.inventario = builder.inventario;
        this.monedero = builder.monedero;
    }

    @Override
    public ResultadoRegistro registrarProducto(RegistroProductoDTO dto) {
        Producto p = Producto.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .fechaCaducidad(dto.getFechaCaducidad())
                .categoria(dto.getCategoria())
                .build();
        return inventario.registrarProducto(p, dto.getPrecio());
    }

    @Override
    public Optional<ProductoInventario> buscarProducto(String codigo) {
        return inventario.buscarProducto(codigo);
    }

    @Override
    public ResultadoReabastecimiento reabastecer(ReabastecimientoDTO solicitud) {
        return inventario.reabastecer(solicitud.getCodigo(), solicitud.getCantidad());
    }

    @Override
    public double consultarFondos() {
        return monedero.consultarFondos();
    }

    @Override
    public ResultadoCompra comprar(CompraDTO solicitud) {
        Optional<ProductoInventario> opt = inventario.buscarProducto(solicitud.getCodigoProducto());
        if (opt.isEmpty()) return new ResultadoCompra(EstadoCompra.PRODUCTO_NO_ENCONTRADO, null, 0.0);
        
        ProductoInventario pi = opt.get();
        if (pi.getExistencias() <= 0) return new ResultadoCompra(EstadoCompra.SIN_EXISTENCIAS, pi.getProducto(), 0.0);
        if (solicitud.getCantidadRecibida() < pi.getPrecio()) return new ResultadoCompra(EstadoCompra.PAGO_INSUFICIENTE, pi.getProducto(), 0.0);

        if (inventario.despachar(solicitud.getCodigoProducto()) != ResultadoEntrega.PRODUCTO_PREPARADO) {
            return new ResultadoCompra(EstadoCompra.SIN_EXISTENCIAS, pi.getProducto(), 0.0);
        }

        ResultadoCobro cobro = monedero.cobrar(pi.getPrecio(), solicitud.getCantidadRecibida());
        if (cobro.getEstado() != EstadoCobro.COBRO_REALIZADO) {
            inventario.reabastecer(solicitud.getCodigoProducto(), 1);
            EstadoCompra estado = (cobro.getEstado() == EstadoCobro.SIN_FONDOS_SUFICIENTES) ? EstadoCompra.SIN_FONDOS_SUFICIENTES : EstadoCompra.PAGO_INSUFICIENTE;
            return new ResultadoCompra(estado, pi.getProducto(), 0.0);
        }

        return new ResultadoCompra(EstadoCompra.VENTA_REALIZADA, pi.getProducto(), cobro.getCambio());
    }

    static class Builder {
        private Inventario inventario;
        private Monedero monedero;

        public Builder inventario(Inventario inventario) {
            this.inventario = inventario;
            return this;
        }

        public Builder monedero(Monedero monedero) {
            this.monedero = monedero;
            return this;
        }

        public MaquinaExpendedora build() {
            return new MaquinaExpendedora(this);
        }
    }
}

class Result {

    public static MaquinaDespachadora crearMaquina(
            double fondosIniciales
    ) {
        Inventario inventario = new InventarioMemoria();
        Monedero monedero = new MonederoSimple(fondosIniciales);
        return new MaquinaExpendedora.Builder()
                .inventario(inventario)
                .monedero(monedero)
                .build();
    }
}

public class SmartVend {

    public static void main(String[] args)
            throws Exception {

        java.util.Locale.setDefault(
                java.util.Locale.US
        );

        java.io.BufferedReader reader =
                new java.io.BufferedReader(
                        new java.io.InputStreamReader(
                                System.in
                        )
                );

        double fondosIniciales =
                Double.parseDouble(
                        reader.readLine().trim()
                );

        MaquinaDespachadora maquina =
                Result.crearMaquina(
                        fondosIniciales
                );

        if (maquina == null) {

            throw new IllegalStateException(
                    "Result.crearMaquina() regreso null"
            );
        }

        int numeroOperaciones =
                Integer.parseInt(
                        reader.readLine().trim()
                );

        for (int i = 0; i < numeroOperaciones; i++) {

            String linea =
                    reader.readLine();

            if (linea == null) {
                break;
            }

            ejecutarOperacion(
                    maquina,
                    linea
            );
        }
    }

    private static void ejecutarOperacion(
            MaquinaDespachadora maquina,
            String linea
    ) {

        String[] partes =
                linea.split(
                        "\\|",
                        -1
                );

        String operacion =
                partes[0]
                        .trim()
                        .toUpperCase();

        switch (operacion) {
            case "REGISTRAR" -> registrar(
                    maquina,
                    partes
            );
            case "BUSCAR" -> buscar(
                    maquina,
                    partes
            );
            case "REABASTECER" -> reabastecer(
                    maquina,
                    partes
            );
            case "COMPRAR" -> comprar(
                    maquina,
                    partes
            );
            case "FONDOS" -> consultarFondos(
                    maquina
            );
            default -> throw new IllegalArgumentException(
                    "Operacion desconocida: "
                            + operacion
            );
        }
    }

    private static void registrar(
            MaquinaDespachadora maquina,
            String[] partes
    ) {

        RegistroProductoDTO producto =
                new RegistroProductoDTO(
                        partes[1].trim(),
                        partes[2].trim(),
                        LocalDate.parse(
                                partes[3].trim()
                        ),
                        CategoriaProducto.valueOf(
                                partes[4]
                                        .trim()
                                        .toUpperCase()
                        ),
                        Double.parseDouble(
                                partes[5].trim()
                        )
                );

        ResultadoRegistro resultado =
                maquina.registrarProducto(
                        producto
                );

        System.out.println(
                "REGISTRAR|"
                        + resultado
        );
    }

    private static void buscar(
            MaquinaDespachadora maquina,
            String[] partes
    ) {

        String codigo =
                partes[1].trim();

        Optional<ProductoInventario> resultado =
                maquina.buscarProducto(
                        codigo
                );

        if (resultado.isEmpty()) {

            System.out.println(
                    "BUSCAR|NO_ENCONTRADO"
            );

            return;
        }

        ProductoInventario producto =
                resultado.get();

        System.out.printf(
                java.util.Locale.US,
                "BUSCAR|ENCONTRADO|%.2f|%d%n",
                producto.getPrecio(),
                producto.getExistencias()
        );
    }

    private static void reabastecer(
            MaquinaDespachadora maquina,
            String[] partes
    ) {

        ReabastecimientoDTO solicitud =
                new ReabastecimientoDTO(
                        partes[1].trim(),
                        Integer.parseInt(
                                partes[2].trim()
                        )
                );

        ResultadoReabastecimiento resultado =
                maquina.reabastecer(
                        solicitud
                );

        System.out.println(
                "REABASTECER|"
                        + resultado
        );
    }

    private static void comprar(
            MaquinaDespachadora maquina,
            String[] partes
    ) {

        CompraDTO solicitud =
                new CompraDTO(
                        partes[1].trim(),
                        Double.parseDouble(
                                partes[2].trim()
                        )
                );

        ResultadoCompra resultado =
                maquina.comprar(
                        solicitud
                );

        System.out.printf(
                java.util.Locale.US,
                "COMPRAR|%s|%.2f%n",
                resultado.getEstado(),
                resultado.getCambio()
        );
    }

    private static void consultarFondos(
            MaquinaDespachadora maquina
    ) {

        System.out.printf(
                java.util.Locale.US,
                "FONDOS|%.2f%n",
                maquina.consultarFondos()
        );
    }
}