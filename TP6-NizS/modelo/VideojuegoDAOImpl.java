package resol.NizS.modelo;

import resol.NizS.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoDAOImpl implements VideojuegoDAO {

    private Videojuego map(ResultSet rs) throws SQLException {
        return new Videojuego(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getInt("unidadesDisponibles"),
                rs.getInt("nivelReposicion"),
                rs.getInt("suspendido")
        );
    }

    @Override
    public List<Videojuego> listar() throws SQLException {
        String sql = "SELECT * FROM videojuegos ORDER BY id";
        List<Videojuego> lista = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    @Override
    public Videojuego buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM videojuegos WHERE id = ?";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public long agregar(Videojuego v) throws SQLException {
        String sql = "INSERT INTO videojuegos(nombre, precio, unidadesDisponibles, nivelReposicion, suspendido) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getNombre());
            ps.setDouble(2, v.getPrecio());
            ps.setInt(3, v.getUnidadesDisponibles());
            ps.setInt(4, v.getNivelReposicion());
            ps.setInt(5, v.getSuspendido());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    v.setId(keys.getLong(1));
                    return v.getId();
                }
            }
        }
        return -1;
    }

    @Override
    public boolean actualizar(Videojuego v) throws SQLException {
        String sql = "UPDATE videojuegos SET nombre=?, precio=?, unidadesDisponibles=?, nivelReposicion=?, suspendido=? WHERE id=?";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, v.getNombre());
            ps.setDouble(2, v.getPrecio());
            ps.setInt(3, v.getUnidadesDisponibles());
            ps.setInt(4, v.getNivelReposicion());
            ps.setInt(5, v.getSuspendido());
            ps.setLong(6, v.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(long id) throws SQLException {
        String sql = "DELETE FROM videojuegos WHERE id=?";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Videojuego> listarDisponibles() throws SQLException {
        String sql = "SELECT * FROM videojuegos WHERE suspendido = 1 AND unidadesDisponibles > 0 ORDER BY id";
        List<Videojuego> lista = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    @Override
    public List<Videojuego> listarNecesitanReposicion() throws SQLException {
        String sql = "SELECT * FROM videojuegos WHERE unidadesDisponibles < nivelReposicion ORDER BY id";
        List<Videojuego> lista = new ArrayList<>();
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }
}
