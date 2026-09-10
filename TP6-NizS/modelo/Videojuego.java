package resol.NizS.modelo;

public class Videojuego {
    private Long id;
    private String nombre;
    private double precio;
    private int unidadesDisponibles;
    private int nivelReposicion;
    private int suspendido;

    public Videojuego() {}

    public Videojuego(String nombre, double precio, int unidadesDisponibles,
                      int nivelReposicion, int suspendido) {
        this(null, nombre, precio, unidadesDisponibles, nivelReposicion, suspendido);
    }

    public Videojuego(Long id, String nombre, double precio, int unidadesDisponibles,
                      int nivelReposicion, int suspendido) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.unidadesDisponibles = unidadesDisponibles;
        this.nivelReposicion = nivelReposicion;
        this.suspendido = suspendido;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getUnidadesDisponibles() { return unidadesDisponibles; }
    public void setUnidadesDisponibles(int unidadesDisponibles) { this.unidadesDisponibles = unidadesDisponibles; }

    public int getNivelReposicion() { return nivelReposicion; }
    public void setNivelReposicion(int nivelReposicion) { this.nivelReposicion = nivelReposicion; }

    public int getSuspendido() { return suspendido; }
    public void setSuspendido(int suspendido) { this.suspendido = suspendido; }

    public boolean necesitaReposicion() {
        return unidadesDisponibles < nivelReposicion;
    }

    public boolean estaDisponible() {
        return suspendido == 1 && unidadesDisponibles > 0;
    }

    @Override
    public String toString() {
        return "Videojuego{id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", unidadesDisponibles=" + unidadesDisponibles +
                ", nivelReposicion=" + nivelReposicion +
                ", suspendido=" + suspendido + '}';
    }
}
