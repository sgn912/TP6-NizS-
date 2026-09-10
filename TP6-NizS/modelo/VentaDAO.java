package resol.NizS.modelo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface VentaDAO {
    List<Venta> listar() throws SQLException;
    Venta buscarPorId(long id) throws SQLException;
    long registrar(Venta venta) throws SQLException;
    boolean actualizar(Venta venta) throws SQLException;
    boolean eliminar(long id) throws SQLException;
    List<Venta> listarDelMes(int anio, int mes) throws SQLException;
    List<Venta> listarPorVideojuego(long videojuegoId) throws SQLException;
}
