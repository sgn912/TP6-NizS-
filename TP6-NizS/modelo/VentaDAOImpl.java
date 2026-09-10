package resol.NizS.modelo;

import resol.NizS.util.ConexionBD;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOImpl implements VentaDAO {

    private Venta map(ResultSet rs) throws SQLException {
        Videojuego v = new Videojuego(
                rs.getLong("videojuego_id"),
                rs.getString("videojuego_nombre"),
                rs.getDouble("precio"),
                rs.getInt("unidadesDisponibles"),
                rs.getInt("nivelReposicion"),
                rs.getInt("suspendido")
        );
        return new Venta(
                rs.getLong("id"),
                rs.getDate("fecha").toLocalDate(),
                v,
                rs.getInt("cantidad"),
                rs.getDouble("descuento"),
                rs.getDouble("total")
        );
    }

    private String baseSelect() {
        return """
                SELECT ve.id, ve.fecha, ve.videojuego_id, ve.cantidad, ve.descuento, ve.total,
                       v.nombre AS videojuego_nombre, v.precio, v.unidadesDisponibles,
                       v.nivelReposicion, v.suspendido
                FROM ventas ve
                JOIN videojuegos v ON v.id = ve.videojuego_id
                """;
    }

    @Override
    public List<Venta> listar() throws SQLException {
        List<Venta> lista = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(baseSelect() + " ORDER BY ve.id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    @Override
    public Venta buscarPorId(long id) throws SQLException {
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(baseSelect() + " WHERE ve.id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public long registrar(Venta venta) throws SQLException {
        String sql = "INSERT INTO ventas(fecha, videojuego_id, cantidad, descuento, total) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(venta.getFecha()));
            ps.setLong(2, venta.getVideojuego().getId());
            ps.setInt(3, venta.getCantidad());
            ps.setDouble(4, venta.getDescuento());
            ps.setDouble(5, venta.getTotal());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    venta.setId(keys.getLong(1));
                    return venta.getId();
                }
            }
        }
        return -1;
    }

    @Override
    public boolean actualizar(Venta venta) throws SQLException {
        String sql = "UPDATE ventas SET fecha=?, videojuego_id=?, cantidad=?, descuento=?, total=? WHERE id=?";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(venta.getFecha()));
            ps.setLong(2, venta.getVideojuego().getId());
            ps.setInt(3, venta.getCantidad());
            ps.setDouble(4, venta.getDescuento());
            ps.setDouble(5, venta.getTotal());
            ps.setLong(6, venta.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(long id) throws SQLException {
        String sql = "DELETE FROM ventas WHERE id=?";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate() > 0;
        }
    }

    private List<Venta> listarConFiltro(String condicion, int anio, int mes) throws SQLException {
        List<Venta> lista = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(baseSelect() + condicion + " ORDER BY ve.fecha, ve.id")) {
            ps.setInt(1, anio);
            ps.setInt(2, mes);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Venta> listarDelMes(int anio, int mes) throws SQLException {
        return listarConFiltro(" WHERE YEAR(ve.fecha)=? AND MONTH(ve.fecha)=?", anio, mes);
    }

    @Override
    public List<Venta> listarPorVideojuego(long videojuegoId) throws SQLException {
        List<Venta> lista = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(baseSelect() + " WHERE ve.videojuego_id=? ORDER BY ve.fecha, ve.id")) {
            ps.setLong(1, videojuegoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        }
        return lista;
    }
}
