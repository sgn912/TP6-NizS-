package resol.NizS.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBD {
    private static final String URL =
            System.getenv().getOrDefault("VIDEOJUEGOS_DB_URL",
                    "jdbc:mysql://localhost:3306/videojuegos_db?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires");
    private static final String USER =
            System.getenv().getOrDefault("VIDEOJUEGOS_DB_USER", "root");
    private static final String PASSWORD =
            System.getenv().getOrDefault("VIDEOJUEGOS_DB_PASSWORD", "");

    private ConexionBD() {}

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
