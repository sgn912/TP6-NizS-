package resol.NizS.modelo;

import java.sql.SQLException;
import java.util.List;

public interface VideojuegoDAO {
    List<Videojuego> listar() throws SQLException;
    Videojuego buscarPorId(long id) throws SQLException;
    long agregar(Videojuego videojuego) throws SQLException;
    boolean actualizar(Videojuego videojuego) throws SQLException;
    boolean eliminar(long id) throws SQLException;
    List<Videojuego> listarDisponibles() throws SQLException;
    List<Videojuego> listarNecesitanReposicion() throws SQLException;
}
