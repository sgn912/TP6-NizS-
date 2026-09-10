package resol.NizS.vista;

import java.util.Scanner;

public class PrincipalView {
    private final Scanner scanner;

    public PrincipalView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Gestión de Videojuegos");
        System.out.println("2. Gestión de Ventas");
        System.out.println("3. Salir");
        return leerEntero("Seleccione una opción: ");
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese una opción válida.");
            }
        }
    }
}
