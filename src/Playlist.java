import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id_playlist;
    private String nombre;
    private Timestamp fecha;
    private List<Cancion> canciones;

    public Playlist() {
        this.canciones = new ArrayList<>();
    }

    public Playlist(int id_playlist, String nombre, Timestamp fecha) {
        this.id_playlist = id_playlist;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    
    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    public void agregarCancion(Cancion cancion) {
        this.canciones.add(cancion);
    }

    public int getId_playlist() {
        return id_playlist;
    }

    public void setId_playlist(int id_playlist) {
        this.id_playlist = id_playlist;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void Timestamp(Timestamp fecha) {
        this.fecha = fecha;
    }
    
    @Override
    public String toString(){
        return this.nombre;
    }
}
