package resol.NizS.vista;

import resol.NizS.modelo.Venta;
import java.util.List;
import java.util.Scanner;

public class VentaView {
    private final Scanner scanner;

    public VentaView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int mostrarMenuVentas() {
        System.out.println("\n=== GESTIÓN DE VENTAS ===");
        System.out.println("1. Listar ventas");
        System.out.println("2. Buscar venta por ID");
        System.out.println("3. Registrar venta");
        System.out.println("4. Buscar ventas de un videojuego");
        System.out.println("5. Reporte de ventas del mes actual");
        System.out.println("6. Volver al menú principal");
        return leerEntero("Seleccione una opción: ");
    }

    public void mostrarVentas(List<Venta> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay ventas para mostrar.");
            return;
        }
        for (Venta v : lista) {
            System.out.printf(
                    "ID: %d | Fecha: %s | Videojuego: %s | Cantidad: %d | Descuento: %.0f%% | Total: $%.2f%n",
                    v.getId(), v.getFecha(), v.getVideojuego().getNombre(),
                    v.getCantidad(), v.getDescuento(), v.getTotal()
            );
        }
    }

    public long pedirId() {
        return leerLong("ID: ");
    }

    public long pedirVideojuegoId() {
        return leerLong("ID del videojuego: ");
    }

    public int pedirCantidad() {
        return leerEntero("Cantidad: ");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    private long leerLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un ID válido.");
            }
        }
    }
}
