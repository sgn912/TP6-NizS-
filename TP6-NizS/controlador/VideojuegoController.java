package resol.NizS.controlador;

import resol.NizS.excepcion.ReglaNegocioException;
import resol.NizS.excepcion.VideojuegoNoEncontradoException;
import resol.NizS.modelo.Videojuego;
import resol.NizS.modelo.VideojuegoDAO;

import java.sql.SQLException;
import java.util.List;

public class VideojuegoController {
    private final VideojuegoDAO videojuegoDAO;

    public VideojuegoController(VideojuegoDAO videojuegoDAO) {
        this.videojuegoDAO = videojuegoDAO;
    }

    public List<Videojuego> listarVideojuegos() throws SQLException {
        return videojuegoDAO.listar();
    }

    public Videojuego buscarPorId(long id)
            throws SQLException, VideojuegoNoEncontradoException {
        Videojuego v = videojuegoDAO.buscarPorId(id);
        if (v == null) {
            throw new VideojuegoNoEncontradoException("No existe el videojuego con ID " + id);
        }
        return v;
    }

    public long agregarVideojuego(Videojuego v)
            throws SQLException, ReglaNegocioException {
        validar(v);
        return videojuegoDAO.agregar(v);
    }

    public boolean actualizarVideojuego(Videojuego v)
            throws SQLException, ReglaNegocioException, VideojuegoNoEncontradoException {
        if (v.getId() == null || videojuegoDAO.buscarPorId(v.getId()) == null) {
            throw new VideojuegoNoEncontradoException("El videojuego no existe.");
        }
        validar(v);
        return videojuegoDAO.actualizar(v);
    }

    public boolean eliminarVideojuego(long id)
            throws SQLException, VideojuegoNoEncontradoException {
        if (videojuegoDAO.buscarPorId(id) == null) {
            throw new VideojuegoNoEncontradoException("El videojuego no existe.");
        }
        return videojuegoDAO.eliminar(id);
    }

    public List<Videojuego> listarDisponibles() throws SQLException {
        return videojuegoDAO.listarDisponibles();
    }

    public List<Videojuego> listarNecesitanReposicion() throws SQLException {
        return videojuegoDAO.listarNecesitanReposicion();
    }

    private void validar(Videojuego v) throws ReglaNegocioException {
        if (v == null) throw new ReglaNegocioException("El videojuego no puede ser nulo.");
        if (v.getNombre() == null || v.getNombre().isBlank())
            throw new ReglaNegocioException("El nombre es obligatorio.");
        if (v.getPrecio() <= 0)
            throw new ReglaNegocioException("El precio debe ser mayor a 0.");
        if (v.getUnidadesDisponibles() < 0)
            throw new ReglaNegocioException("Las unidadesDisponibles no pueden ser negativas.");
        if (v.getNivelReposicion() < 0)
            throw new ReglaNegocioException("El nivelReposicion no puede ser negativo.");
        if (v.getSuspendido() != 0 && v.getSuspendido() != 1)
            throw new ReglaNegocioException("Suspendido debe ser 1 (disponible) o 0 (no disponible).");
    }
}
