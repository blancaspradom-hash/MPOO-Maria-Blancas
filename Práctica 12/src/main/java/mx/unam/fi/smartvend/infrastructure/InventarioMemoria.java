package mx.unam.fi.smartvend.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mx.unam.fi.smartvend.domain.Inventario;
import mx.unam.fi.smartvend.domain.Producto;
import mx.unam.fi.smartvend.domain.ProductoInventario;
import mx.unam.fi.smartvend.domain.ResultadoEntrega;
import mx.unam.fi.smartvend.domain.ResultadoReabastecimiento;
import mx.unam.fi.smartvend.domain.ResultadoRegistro;

public class InventarioMemoria implements Inventario {
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
