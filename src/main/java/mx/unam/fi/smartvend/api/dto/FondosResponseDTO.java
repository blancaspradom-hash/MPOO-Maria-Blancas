package mx.unam.fi.smartvend.api.dto;

public class FondosResponseDTO {
    private final double efectivo;
    private final double tarjeta;
    private final double total;

    public FondosResponseDTO(double efectivo, double tarjeta, double total) {
        this.efectivo = efectivo;
        this.tarjeta = tarjeta;
        this.total = total;
    }

    public double getEfectivo() { return efectivo; }
    public double getTarjeta() { return tarjeta; }
    public double getTotal() { return total; }
}
