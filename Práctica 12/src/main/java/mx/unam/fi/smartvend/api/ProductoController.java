package mx.unam.fi.smartvend.api;

import java.util.Optional;

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

public class ProductoController {

    private final MaquinaService maquinaService;

    public ProductoController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    public ResponseEntity<ResultadoRegistro> registrarProducto(RegistroProductoDTO dto) {
        ResultadoRegistro resultado = maquinaService.registrarProducto(dto);
        if (resultado == ResultadoRegistro.PRODUCTO_REGISTRADO) {
            return ResponseEntity.<ResultadoRegistro>status(HttpStatus.CREATED).body(resultado).build();
        }
        return ResponseEntity.<ResultadoRegistro>badRequest().body(resultado).build();
    }

    public ResponseEntity<ProductoResponseDTO> buscarProducto(String codigo) {
        Optional<ProductoInventario> opt = maquinaService.buscarProducto(codigo);
        if (opt.isEmpty()) {
            return ResponseEntity.<ProductoResponseDTO>notFound().build();
        }
        ProductoInventario pi = opt.get();
        ProductoResponseDTO dto = new ProductoResponseDTO(
                pi.getProducto().getCodigo(),
                pi.getProducto().getNombre(),
                pi.getProducto().getFechaCaducidad(),
                String.valueOf(pi.getProducto().getCategoria()),
                pi.getPrecio(),
                pi.getExistencias()
        );
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<ResultadoReabastecimiento> reabastecer(String codigo, ReabastecimientoDTO dto) {
        ResultadoReabastecimiento resultado = maquinaService.reabastecer(codigo, dto.getCantidad());
        if (resultado == ResultadoReabastecimiento.REABASTECIMIENTO_REALIZADO) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.<ResultadoReabastecimiento>badRequest().body(resultado).build();
    }

    public ResponseEntity<ResultadoCompra> comprar(String codigo, CompraDTO dto) {
        dto.setCodigoProducto(codigo);
        ResultadoCompra resultado = maquinaService.comprar(dto);
        if (resultado.getEstado() == EstadoCompra.VENTA_REALIZADA) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.<ResultadoCompra>badRequest().body(resultado).build();
    }

    public ResponseEntity<FondosResponseDTO> consultarFondos() {
        return ResponseEntity.ok(maquinaService.consultarFondos());
    }

    public static final class ResponseEntity<T> {
    private final T body;
    private final int statusCode;

    private ResponseEntity(T body, int statusCode) {
        this.body = body;
        this.statusCode = statusCode;
    }

    public T getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static <T> ResponseEntityBuilder<T> ok() {
        return status(HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return status(HttpStatus.OK, body);
    }

    public static <T> ResponseEntityBuilder<T> status(HttpStatus status) {
        return new ResponseEntityBuilder<>(status);
    }

    public static <T> ResponseEntity<T> status(HttpStatus status, T body) {
        return new ResponseEntity<>(body, status.getCode());
    }

    public static <T> ResponseEntityBuilder<T> badRequest() {
        return status(HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntityBuilder<T> notFound() {
        return status(HttpStatus.NOT_FOUND);
    }

    public static class ResponseEntityBuilder<T> {
        private final HttpStatus status;
        private T body;

        private ResponseEntityBuilder(HttpStatus status) {
            this.status = status;
        }

        public ResponseEntityBuilder<T> body(T body) {
            this.body = body;
            return this;
        }

        public ResponseEntity<T> build() {
            return new ResponseEntity<>(body, status.getCode());
        }
    }
}

    public enum HttpStatus {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    NOT_FOUND(404);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
}