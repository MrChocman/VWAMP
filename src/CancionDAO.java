import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CancionDAO {
    
    public List<Cancion> obtenerCanciones(){
        List<Cancion> canc = new ArrayList<>();
        String sqlq = "SELECT * FROM CANCIONES";
        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlq);
            ResultSet rs = pstmt.executeQuery()) {
            
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
                canc.add(cancion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return canc;
    }

    public void insertarCancion(Cancion cancion){ 
        String sqlq = "INSERT INTO CANCIONES (titulo, artista, album, genero, duracion_segundos, ruta_archivo)" + "VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlq)) {
            pstmt.setString(1, cancion.getTitulo());
            pstmt.setString(2, cancion.getArtista());
            pstmt.setString(3, cancion.getAlbum());
            pstmt.setString(4, cancion.getGenero());
            pstmt.setInt(5, cancion.getDuracion());
            pstmt.setString(6, cancion.getRuta());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarCancion(int id_cancion){ 
        String sqlq = "DELETE FROM CANCIONES WHERE id_cancion = ?";
        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlq)) {
            pstmt.setInt(1, id_cancion);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cancion buscarCancionPortit (String titulo){
        String sqlq = "SELECT * FROM CANCIONES WHERE titulo = ?";

        try (Connection conn = ConexionDB.getConnection();
            PreparedStatement psmt  = conn.prepareStatement(sqlq)){
            psmt.setString(1, titulo);
            try (ResultSet rs = psmt.executeQuery()){
                if (rs.next()){
                    Cancion cn = new Cancion(
                        rs.getInt("id_cancion"),
                        rs.getString("titulo"),
                        rs.getString("artista"),
                        rs.getString("album"),
                        rs.getString("genero"),
                        rs.getInt("duracion_segundos"),
                        rs.getString("ruta_archivo")
                    );
                    return cn;
                } else {
                    return null;
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
    }

}