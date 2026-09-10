package resol.NizS.vista;

import resol.NizS.modelo.Videojuego;
import java.util.List;
import java.util.Scanner;

public class VideojuegoView {
    private final Scanner scanner;

    public VideojuegoView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int mostrarMenuVideojuegos() {
        System.out.println("\n=== GESTIÓN DE VIDEOJUEGOS ===");
        System.out.println("1. Listar videojuegos");
        System.out.println("2. Buscar videojuego por ID");
        System.out.println("3. Agregar videojuego");
        System.out.println("4. Actualizar videojuego");
        System.out.println("5. Eliminar videojuego");
        System.out.println("6. Videojuegos que necesitan reposición");
        System.out.println("7. Videojuegos disponibles para la venta");
        System.out.println("8. Volver al menú principal");
        return leerEntero("Seleccione una opción: ");
    }

    public void mostrarVideojuegos(List<Videojuego> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay videojuegos para mostrar.");
            return;
        }
        for (Videojuego v : lista) System.out.println(v);
    }

    public long pedirId() {
        return leerLong("ID: ");
    }

    public Videojuego pedirNuevoVideojuego() {
        System.out.println("\n--- NUEVO VIDEOJUEGO ---");
        String nombre = leerTexto("Nombre: ");
        double precio = leerDouble("Precio: ");
        int stock = leerEntero("Unidades disponibles: ");
        int reposicion = leerEntero("Nivel de reposición: ");
        int suspendido = leerEntero("Suspendido (1=disponible, 0=no disponible): ");
        return new Videojuego(nombre, precio, stock, reposicion, suspendido);
    }

    public Videojuego pedirVideojuegoParaActualizar(long id, Videojuego actual) {
        System.out.println("\n--- ACTUALIZAR VIDEOJUEGO ---");
        String nombre = leerTexto("Nombre [" + actual.getNombre() + "]: ");
        double precio = leerDouble("Precio [" + actual.getPrecio() + "]: ");
        int stock = leerEntero("Unidades disponibles [" + actual.getUnidadesDisponibles() + "]: ");
        int reposicion = leerEntero("Nivel de reposición [" + actual.getNivelReposicion() + "]: ");
        int suspendido = leerEntero("Suspendido [" + actual.getSuspendido() + "]: ");
        return new Videojuego(id, nombre, precio, stock, reposicion, suspendido);
    }

    public boolean confirmar(String mensaje) {
        System.out.print(mensaje + " (S/N): ");
        return scanner.nextLine().trim().equalsIgnoreCase("S");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
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

    private double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un precio válido.");
            }
        }
    }
}
