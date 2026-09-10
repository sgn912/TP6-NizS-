package resol.NizS.controlador;

import resol.NizS.excepcion.ReglaNegocioException;
import resol.NizS.excepcion.VentaInvalidaException;
import resol.NizS.modelo.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VentaController {
    private final VentaDAO ventaDAO;
    private final VideojuegoDAO videojuegoDAO;

    public VentaController(VentaDAO ventaDAO, VideojuegoDAO videojuegoDAO) {
        this.ventaDAO = ventaDAO;
        this.videojuegoDAO = videojuegoDAO;
    }

    public List<Venta> listarVentas() throws SQLException {
        return ventaDAO.listar();
    }

    public Venta buscarPorId(long id) throws SQLException, VentaInvalidaException {
        Venta venta = ventaDAO.buscarPorId(id);
        if (venta == null) throw new VentaInvalidaException("No existe la venta con ID " + id);
        return venta;
    }

    public long registrarVenta(LocalDate fecha, long videojuegoId, int cantidad)
            throws SQLException, VentaInvalidaException, ReglaNegocioException {

        if (fecha == null) throw new VentaInvalidaException("La fecha es obligatoria.");
        if (fecha.isAfter(LocalDate.now()))
            throw new VentaInvalidaException("La fecha de venta no puede ser futura.");
        if (cantidad <= 0)
            throw new VentaInvalidaException("La cantidad debe ser mayor que cero.");

        Videojuego v = videojuegoDAO.buscarPorId(videojuegoId);
        if (v == null)
            throw new ReglaNegocioException("No existe el videojuego indicado.");

        if (v.getSuspendido() != 1)
            throw new ReglaNegocioException("El videojuego no está disponible para la venta.");

        if (v.getUnidadesDisponibles() < cantidad)
            throw new ReglaNegocioException("Stock insuficiente. Disponible: " + v.getUnidadesDisponibles());

        Venta venta = new Venta(fecha, v, cantidad);
        venta.calcularDescuentoYTotal();

        v.setUnidadesDisponibles(v.getUnidadesDisponibles() - cantidad);

        // Se usa la misma conexión para garantizar que venta y descuento de stock
        // queden sincronizados.
        long id;
        try (var cn = resol.NizS.util.ConexionBD.obtenerConexion()) {
            cn.setAutoCommit(false);
            try {
                String update = "UPDATE videojuegos SET unidadesDisponibles=? WHERE id=?";
                try (var ps = cn.prepareStatement(update)) {
                    ps.setInt(1, v.getUnidadesDisponibles());
                    ps.setLong(2, v.getId());
                    if (ps.executeUpdate() == 0)
                        throw new SQLException("No se pudo actualizar el stock.");
                }

                String insert = "INSERT INTO ventas(fecha, videojuego_id, cantidad, descuento, total) VALUES (?, ?, ?, ?, ?)";
                try (var ps = cn.prepareStatement(insert, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, java.sql.Date.valueOf(venta.getFecha()));
                    ps.setLong(2, venta.getVideojuego().getId());
                    ps.setInt(3, venta.getCantidad());
                    ps.setDouble(4, venta.getDescuento());
                    ps.setDouble(5, venta.getTotal());
                    ps.executeUpdate();
                    try (var keys = ps.getGeneratedKeys()) {
                        if (keys.next()) id = keys.getLong(1);
                        else throw new SQLException("No se obtuvo el ID de la venta.");
                    }
                }
                cn.commit();
            } catch (Exception e) {
                cn.rollback();
                if (e instanceof SQLException se) throw se;
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        }

        return id;
    }

    public List<Venta> listarPorVideojuego(long videojuegoId) throws SQLException {
        return ventaDAO.listarPorVideojuego(videojuegoId);
    }

    public List<Venta> reporteMesActual() throws SQLException {
        LocalDate hoy = LocalDate.now();
        return ventaDAO.listarDelMes(hoy.getYear(), hoy.getMonthValue());
    }
}
