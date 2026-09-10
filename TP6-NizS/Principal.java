package resol.NizS;

import resol.NizS.controlador.VentaController;
import resol.NizS.controlador.VideojuegoController;
import resol.NizS.excepcion.ReglaNegocioException;
import resol.NizS.excepcion.VentaInvalidaException;
import resol.NizS.excepcion.VideojuegoNoEncontradoException;
import resol.NizS.modelo.*;
import resol.NizS.vista.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        VideojuegoDAO videojuegoDAO = new VideojuegoDAOImpl();
        VentaDAO ventaDAO = new VentaDAOImpl();

        VideojuegoController videojuegoController =
                new VideojuegoController(videojuegoDAO);
        VentaController ventaController =
                new VentaController(ventaDAO, videojuegoDAO);

        PrincipalView principalView = new PrincipalView(scanner);
        VideojuegoView videojuegoView = new VideojuegoView(scanner);
        VentaView ventaView = new VentaView(scanner);

        boolean salir = false;

        while (!salir) {
            try {
                switch (principalView.mostrarMenuPrincipal()) {
                    case 1 -> menuVideojuegos(videojuegoController, videojuegoView);
                    case 2 -> menuVentas(ventaController, ventaView);
                    case 3 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Programa finalizado.");
    }

    private static void menuVideojuegos(VideojuegoController c, VideojuegoView v)
            throws SQLException, VideojuegoNoEncontradoException, ReglaNegocioException {
        boolean volver = false;

        while (!volver) {
            switch (v.mostrarMenuVideojuegos()) {
                case 1 -> v.mostrarVideojuegos(c.listarVideojuegos());
                case 2 -> {
                    long id = v.pedirId();
                    v.mostrarVideojuegos(java.util.List.of(c.buscarPorId(id)));
                }
                case 3 -> {
                    long id = c.agregarVideojuego(v.pedirNuevoVideojuego());
                    v.mostrarMensaje("Videojuego agregado correctamente. ID: " + id);
                }
                case 4 -> {
                    long id = v.pedirId();
                    Videojuego actual = c.buscarPorId(id);
                    Videojuego actualizado = v.pedirVideojuegoParaActualizar(id, actual);
                    c.actualizarVideojuego(actualizado);
                    v.mostrarMensaje("Videojuego actualizado correctamente.");
                }
                case 5 -> {
                    long id = v.pedirId();
                    if (v.confirmar("¿Eliminar el videojuego " + id + "?")) {
                        c.eliminarVideojuego(id);
                        v.mostrarMensaje("Videojuego eliminado correctamente.");
                    }
                }
                case 6 -> v.mostrarVideojuegos(c.listarNecesitanReposicion());
                case 7 -> v.mostrarVideojuegos(c.listarDisponibles());
                case 8 -> volver = true;
                default -> v.mostrarMensaje("Opción inválida.");
            }
        }
    }

    private static void menuVentas(VentaController c, VentaView v)
            throws SQLException, VentaInvalidaException, ReglaNegocioException {
        boolean volver = false;

        while (!volver) {
            switch (v.mostrarMenuVentas()) {
                case 1 -> v.mostrarVentas(c.listarVentas());
                case 2 -> v.mostrarVentas(java.util.List.of(c.buscarPorId(v.pedirId())));
                case 3 -> {
                    long videojuegoId = v.pedirVideojuegoId();
                    int cantidad = v.pedirCantidad();
                    long id = c.registrarVenta(LocalDate.now(), videojuegoId, cantidad);
                    v.mostrarMensaje("Venta registrada correctamente. ID: " + id);
                }
                case 4 -> {
                    long videojuegoId = v.pedirVideojuegoId();
                    v.mostrarVentas(c.listarPorVideojuego(videojuegoId));
                }
                case 5 -> v.mostrarVentas(c.reporteMesActual());
                case 6 -> volver = true;
                default -> v.mostrarMensaje("Opción inválida.");
            }
        }
    }
}
