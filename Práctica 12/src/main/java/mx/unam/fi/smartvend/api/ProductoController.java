package mx.unam.fi.smartvend.api;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.unam.fi.smartvend.api.dto.CompraDTO;
import mx.unam.fi.smartvend.api.dto.FondosResponseDTO;
import mx.unam.fi.smartvend.api.dto.ProductoResponseDTO;
import mx.unam.fi.smartvend.api.dto.ReabastecimientoDTO;
import mx.unam.fi.smartvend.api.dto.RegistroProductoDTO;
import mx.unam.fi.smartvend.application.MaquinaService;
import mx.unam.fi.smartvend.domain.EstadoCompra;
import mx.unam.fi.smartvend.domain.ProductoInventario;
import mx.unam.fi.smartvend.domain.ResultadoCompra;
import mx.unam.fi.smartvend.domain.ResultadoReabastecimiento;
import mx.unam.fi.smartvend.domain.ResultadoRegistro;

@RestController
@RequestMapping("/api")
public class ProductoController {

    private final MaquinaService maquinaService;

    public ProductoController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    @PostMapping("/productos")
    public ResponseEntity<ResultadoRegistro> registrarProducto(@RequestBody RegistroProductoDTO dto) {
        ResultadoRegistro resultado = maquinaService.registrarProducto(dto);
        if (resultado == ResultadoRegistro.PRODUCTO_REGISTRADO) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        }
        return ResponseEntity.badRequest().body(resultado);
    }

    @GetMapping("/productos/{codigo}")
    public ResponseEntity<ProductoResponseDTO> buscarProducto(@PathVariable String codigo) {
        Optional<ProductoInventario> opt = maquinaService.buscarProducto(codigo);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ProductoInventario pi = opt.get();
        ProductoResponseDTO dto = new ProductoResponseDTO(
                pi.getProducto().getCodigo(),
                pi.getProducto().getNombre(),
                pi.getProducto().getFechaCaducidad(),
                pi.getProducto().getCategoria(),
                pi.getPrecio(),
                pi.getExistencias()
        );
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/productos/{codigo}/existencias")
    public ResponseEntity<ResultadoReabastecimiento> reabastecer(
            @PathVariable String codigo,
            @RequestBody ReabastecimientoDTO dto
    ) {
        ResultadoReabastecimiento resultado = maquinaService.reabastecer(codigo, dto.getCantidad());
        if (resultado == ResultadoReabastecimiento.REABASTECIMIENTO_REALIZADO) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.badRequest().body(resultado);
    }

    @PostMapping("/producto/{codigo}/comprar")
    public ResponseEntity<ResultadoCompra> comprar(
            @PathVariable String codigo,
            @RequestBody CompraDTO dto
    ) {
        dto.setCodigoProducto(codigo);
        ResultadoCompra resultado = maquinaService.comprar(dto);
        if (resultado.getEstado() == EstadoCompra.VENTA_REALIZADA) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.badRequest().body(resultado);
    }

    @GetMapping("/fondos")
    public ResponseEntity<FondosResponseDTO> consultarFondos() {
        return ResponseEntity.ok(maquinaService.consultarFondos());
    }
}