import java.io.*;
import java.util.*;

public class SistemaDePedidosConDescuentos {

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String productos = br.readLine();
        String operaciones = br.readLine();

        ProductoRepository productoRepository = new ProductoRepositoryImpl();
        PedidoService pedidoService = new PedidoServiceImpl(productoRepository);
        Pedido pedido = Pedido.empty();

        if (productos != null && !productos.isBlank() && !productos.equals("-")) {
            String[] registros = productos.split(";");
            for (String registro : registros) {
                String[] datos = registro.split("\\|", 3);
                ProductoDTO productoDTO = new ProductoDTO(
                        datos[0].trim(),
                        datos[1].trim(),
                        datos[2].trim()
                );
                productoRepository.guardar(productoDTO);
            }
        }

        StringBuilder salida = new StringBuilder();

        if (operaciones != null && !operaciones.isBlank() && !operaciones.equals("-")) {
            String[] listaOperaciones = operaciones.split(";");
            for (String operacion : listaOperaciones) {
                String[] datos = operacion.split("\\|");
                AccionPedido accion = AccionPedido.valueOf(datos[0].trim());

                if (accion == AccionPedido.AGREGAR) {
                    int idProducto = Integer.parseInt(datos[1].trim());
                    int cantidad = Integer.parseInt(datos[2].trim());

                    EstadoAgregarProducto estado = pedidoService.agregarProducto(
                            pedido, idProducto, cantidad
                    );

                    salida.append("AGREGADO ")
                            .append(idProducto)
                            .append(" ")
                            .append(estado)
                            .append("\n");
                } else if (accion == AccionPedido.CONFIRMAR) {
                    TipoDescuento tipoDescuento = TipoDescuento.valueOf(datos[1].trim());
                    ResumenPedidoDTO resumen = pedidoService.confirmarPedido(pedido, tipoDescuento);

                    if (resumen == null) {
                        throw new IllegalStateException("confirmarPedido no debe regresar null");
                    }

                    imprimirResumen(salida, resumen);
                }
            }
        }

        System.out.print(salida.toString());
    }

    private static void imprimirResumen(StringBuilder salida, ResumenPedidoDTO resumen) {
        List<DetallePedidoDTO> detalles = resumen.getDetalles();

        if (detalles == null) {
            throw new IllegalStateException("La lista de detalles no debe ser null");
        }

        for (DetallePedidoDTO detalle : detalles) {
            salida.append("ITEM ")
                    .append(detalle.getIdProducto())
                    .append(" ")
                    .append(detalle.getNombre())
                    .append(" ")
                    .append(detalle.getCantidad())
                    .append("\n");
        }

        salida.append(String.format(
                Locale.US,
                "RESUMEN %.2f %.2f %.2f\n",
                resumen.getSubtotal(),
                resumen.getDescuento(),
                resumen.getTotal()
        ));
    }
}

enum AccionPedido {
    AGREGAR,
    CONFIRMAR
}

enum TipoDescuento {
    REGULAR,
    FRECUENTE,
    MAYOREO
}

enum EstadoAgregarProducto {
    AGREGADO,
    PRODUCTO_NO_EXISTE,
    CANTIDAD_INVALIDA
}

final class ProductoDTO {
    private final String id;
    private final String nombre;
    private final String precio;

    public ProductoDTO(String id, String nombre, String precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPrecio() { return precio; }
}

final class DetallePedidoDTO {
    private final int idProducto;
    private final String nombre;
    private final int cantidad;

    public DetallePedidoDTO(int idProducto, String nombre, int cantidad) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
}

final class ResumenPedidoDTO {
    private final List<DetallePedidoDTO> detalles;
    private final double subtotal;
    private final double descuento;
    private final double total;

    public ResumenPedidoDTO(List<DetallePedidoDTO> detalles, double subtotal, double descuento, double total) {
        this.detalles = detalles;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
    }

    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public double getSubtotal() { return subtotal; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
}

interface DescuentoStrategy {
    double calcularDescuento(double subtotal);
}

interface Discountable {
    void setDiscount(DescuentoStrategy descuentoStrategy);
}

interface ProductoRepository {
    boolean guardar(ProductoDTO producto);
    Producto buscarPorId(int idProducto);
    boolean existe(int idProducto);
}

interface PedidoService {
    EstadoAgregarProducto agregarProducto(Pedido pedido, int idProducto, int cantidad);
    DescuentoStrategy SelectorDescuento(TipoDescuento tipoDescuento);
    ResumenPedidoDTO confirmarPedido(Pedido pedido, TipoDescuento tipoDescuento);
}

class Producto {
    private final int id;
    private final String nombre;
    private final double precio;

    public Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}

class ElementoPedido {
    private final Producto producto;
    private int cantidad;

    public ElementoPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public void agregarCantidad(int cantidad) { this.cantidad += cantidad; }
}

class Pedido implements Discountable {
    private final List<ElementoPedido> elementos;
    private DescuentoStrategy descuentoStrategy;

    private Pedido() {
        this.elementos = new ArrayList<>();
        this.descuentoStrategy = new DescuentoRegularStrategy();
    }

    public static Pedido empty() {
        return new Pedido();
    }

    @Override
    public void setDiscount(DescuentoStrategy descuentoStrategy) {
        this.descuentoStrategy = descuentoStrategy;
    }

    public void agregarOActualizar(Producto producto, int cantidad) {
        for (ElementoPedido elemento : elementos) {
            if (elemento.getProducto().getId() == producto.getId()) {
                elemento.agregarCantidad(cantidad);
                return;
            }
        }
        elementos.add(new ElementoPedido(producto, cantidad));
    }

    public List<ElementoPedido> getElementos() { return elementos; }

    public double calcularSubtotal() {
        double subtotal = 0.0;
        for (ElementoPedido elemento : elementos) {
            subtotal += elemento.getProducto().getPrecio() * elemento.getCantidad();
        }
        return subtotal;
    }

    public double calcularDescuento(double subtotal) {
        return descuentoStrategy.calcularDescuento(subtotal);
    }
}

class DescuentoRegularStrategy implements DescuentoStrategy {
    @Override
    public double calcularDescuento(double subtotal) { return 0.0; }
}

class DescuentoFrecuenteStrategy implements DescuentoStrategy {
    @Override
    public double calcularDescuento(double subtotal) { return subtotal * 0.10; }
}

class DescuentoMayoreoStrategy implements DescuentoStrategy {
    @Override
    public double calcularDescuento(double subtotal) {
        if (subtotal >= 5000.00) {
            return subtotal * 0.15;
        }
        return 0.0;
    }
}

class ProductoRepositoryImpl implements ProductoRepository {
    private final Map<Integer, Producto> catalogo = new HashMap<>();

    @Override
    public boolean guardar(ProductoDTO producto) {
        try {
            int id = Integer.parseInt(producto.getId());
            String nombre = producto.getNombre();
            double precio = Double.parseDouble(producto.getPrecio());
            catalogo.put(id, new Producto(id, nombre, precio));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Producto buscarPorId(int idProducto) {
        return catalogo.get(idProducto);
    }

    @Override
    public boolean existe(int idProducto) {
        return catalogo.containsKey(idProducto);
    }
}

class PedidoServiceImpl implements PedidoService {
    private final ProductoRepository productoRepository;

    public PedidoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public EstadoAgregarProducto agregarProducto(Pedido pedido, int idProducto, int cantidad) {
        if (!productoRepository.existe(idProducto)) {
            return EstadoAgregarProducto.PRODUCTO_NO_EXISTE;
        }
        if (cantidad <= 0) {
            return EstadoAgregarProducto.CANTIDAD_INVALIDA;
        }

        Producto producto = productoRepository.buscarPorId(idProducto);
        pedido.agregarOActualizar(producto, cantidad);
        return EstadoAgregarProducto.AGREGADO;
    }

    @Override
    public DescuentoStrategy SelectorDescuento(TipoDescuento tipoDescuento) {
        switch (tipoDescuento) {
            case FRECUENTE:
                return new DescuentoFrecuenteStrategy();
            case MAYOREO:
                return new DescuentoMayoreoStrategy();
            case REGULAR:
            default:
                return new DescuentoRegularStrategy();
        }
    }

    @Override
    public ResumenPedidoDTO confirmarPedido(Pedido pedido, TipoDescuento tipoDescuento) {
        DescuentoStrategy estrategia = SelectorDescuento(tipoDescuento);
        pedido.setDiscount(estrategia);

        double subtotal = pedido.calcularSubtotal();
        double descuento = pedido.calcularDescuento(subtotal);
        double total = subtotal - descuento;

        List<DetallePedidoDTO> detallesDTO = new ArrayList<>();
        for (ElementoPedido elemento : pedido.getElementos()) {
            detallesDTO.add(new DetallePedidoDTO(
                elemento.getProducto().getId(),
                elemento.getProducto().getNombre(),
                elemento.getCantidad()
            ));
        }

        return new ResumenPedidoDTO(detallesDTO, subtotal, descuento, total);
    }
}

