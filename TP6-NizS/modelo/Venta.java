package resol.NizS.modelo;

import java.time.LocalDate;

public class Venta {
    private Long id;
    private LocalDate fecha;
    private Videojuego videojuego;
    private int cantidad;
    private double descuento;
    private double total;

    public Venta() {}

    public Venta(LocalDate fecha, Videojuego videojuego, int cantidad) {
        this(null, fecha, videojuego, cantidad, 0, 0);
    }

    public Venta(Long id, LocalDate fecha, Videojuego videojuego, int cantidad,
                 double descuento, double total) {
        this.id = id;
        this.fecha = fecha;
        this.videojuego = videojuego;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.total = total;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Videojuego getVideojuego() { return videojuego; }
    public void setVideojuego(Videojuego videojuego) { this.videojuego = videojuego; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public void calcularDescuentoYTotal() {
        if (cantidad <= 1) descuento = 0;
        else if (cantidad <= 4) descuento = 5;
        else if (cantidad <= 9) descuento = 10;
        else descuento = 15;

        double subtotal = videojuego.getPrecio() * cantidad;
        total = subtotal * (1 - descuento / 100.0);
    }

    @Override
    public String toString() {
        return "Venta{id=" + id +
                ", fecha=" + fecha +
                ", videojuego=" + (videojuego == null ? null : videojuego.getNombre()) +
                ", cantidad=" + cantidad +
                ", descuento=" + descuento + "%" +
                ", total=" + total + '}';
    }
}
