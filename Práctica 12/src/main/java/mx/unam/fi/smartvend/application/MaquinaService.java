package mx.unam.fi.smartvend.application;

import mx.unam.fi.smartvend.api.dto.*;
import mx.unam.fi.smartvend.domain.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MaquinaService {

    private final Inventario inventario;
    private final Monedero monederoEfectivo;
    private final Monedero monederoTarjeta;

    public MaquinaService(
            Inventario inventario,
            @Qualifier("monederoEfectivo") Monedero monederoEfectivo,
            @Qualifier("monederoTarjeta") Monedero monederoTarjeta
    ) {
        this.inventario = inventario;
        this.monederoEfectivo = monederoEfectivo;
        this.monederoTarjeta = monederoTarjeta;
    }

    public ResultadoRegistro registrarProducto(RegistroProductoDTO dto) {
        Producto p = Producto.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .fechaCaducidad(dto.getFechaCaducidad())
                .categoria(dto.getCategoria())
                .build();
        return inventario.registrarProducto(p, dto.getPrecio());
    }

    public Optional<ProductoInventario> buscarProducto(String codigo) {
        return inventario.buscarProducto(codigo);
    }

    public ResultadoReabastecimiento reabastecer(String codigo, int cantidad) {
        return inventario.reabastecer(codigo, cantidad);
    }

    public ResultadoCompra comprar(CompraDTO solicitud) {
        Optional<ProductoInventario> opt = inventario.buscarProducto(solicitud.getCodigoProducto());
        if (opt.isEmpty()) return new ResultadoCompra(EstadoCompra.PRODUCTO_NO_ENCONTRADO, null, 0.0);

        ProductoInventario pi = opt.get();
        if (pi.getExistencias() <= 0) return new ResultadoCompra(EstadoCompra.SIN_EXISTENCIAS, pi.getProducto(), 0.0);
        if (solicitud.getCantidadRecibida() < pi.getPrecio()) return new ResultadoCompra(EstadoCompra.PAGO_INSUFICIENTE, pi.getProducto(), 0.0);

        Monedero monederoSeleccionado;
        monederoSeleccionado = (solicitud.getMetodoPago() == MetodoPago.TARJETA) 
                ? monederoTarjeta 
                : monederoEfectivo;

        if (inventario.despachar(solicitud.getCodigoProducto()) != ResultadoEntrega.PRODUCTO_PREPARADO) {
            return new ResultadoCompra(EstadoCompra.SIN_EXISTENCIAS, pi.getProducto(), 0.0);
        }

        ResultadoCobro cobro = monederoSeleccionado.cobrar(pi.getPrecio(), solicitud.getCantidadRecibida());
        if (cobro.getEstado() != EstadoCobro.COBRO_REALIZADO) {
            inventario.reabastecer(solicitud.getCodigoProducto(), 1);
            EstadoCompra estado = (cobro.getEstado() == EstadoCobro.SIN_FONDOS_SUFICIENTES) 
                    ? EstadoCompra.SIN_FONDOS_SUFICIENTES 
                    : EstadoCompra.PAGO_INSUFICIENTE;
            return new ResultadoCompra(estado, pi.getProducto(), 0.0);
        }

        return new ResultadoCompra(EstadoCompra.VENTA_REALIZADA, pi.getProducto(), cobro.getCambio());
    }

    public FondosResponseDTO consultarFondos() {
        double efectivo = monederoEfectivo.consultarFondos();
        double tarjeta = monederoTarjeta.consultarFondos();
        return new FondosResponseDTO(efectivo, tarjeta, efectivo + tarjeta);
    }
}