import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PlaylistDAO {
    public List<Playlist> obtenerPlaylists(){
        List<Playlist> playlists = new ArrayList<>();
        String sqlq = "SELECT * FROM PLAYLISTS";
        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlq);
            ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Playlist playlist = new Playlist(
                    rs.getInt("id_playlist"),
                    rs.getString("nombre"),
                    rs.getTimestamp("fecha_creacion")
                );
                playlists.add(playlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists;
    }

    public void CrearPlaylist(String nombre){
        String sqlq = "INSERT INTO PLAYLISTS (nombre, fecha_creacion)" + "VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlq)) {
            pstmt.setString(1, nombre);
            pstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarCancionAPlaylist(int id_playlist, int id_cancion){
        String sqlq = "INSERT INTO PLAYLIST_CANCIONES (id_playlist, id_cancion)" + "VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlq)) {
            pstmt.setInt(1, id_playlist);
            pstmt.setInt(2, id_cancion);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void eliminarCancionDePlaylist(int id_playlist, int id_cancion){
        String sqlq = "DELETE FROM PLAYLIST_CANCIONES WHERE id_playlist = ? AND id_cancion = ?";
        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlq)) {
            pstmt.setInt(1, id_playlist);
            pstmt.setInt(2, id_cancion);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void eliminarPlaylist(int id_playlist){
        String sqlq = "DELETE FROM PLAYLISTS WHERE id_playlist = ?";
        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlq)) {
            pstmt.setInt(1, id_playlist);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cancion> obtenerCancionesDePlaylist(int id_playlist) {
        List<Cancion> canciones = new ArrayList<>();
        String sqlq = "SELECT c.* FROM CANCIONES c "+"JOIN PLAYLIST_CANCIONES pc ON c.id_cancion = pc.id_cancion " +"WHERE pc.id_playlist = ?";

        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlq)) {
            
            pstmt.setInt(1, id_playlist);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cancion cancion = new Cancion(
                        rs.getInt("id_cancion"),
                        rs.getString("titulo"),
                        rs.getString("artista"),
                        rs.getString("album"),
                        rs.getString("genero"),
                        rs.getInt("duracion_segundos"), 
                        rs.getString("ruta_archivo")   
                    );
                    canciones.add(cancion);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return canciones;
    }



}
