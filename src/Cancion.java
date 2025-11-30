public class Cancion {
    private int id_cancion; 
    private String titulo;
    private String artista;
    private String album;
    private String genero;
    private int duracion;
    private String ruta;

    public Cancion(int id_cancion, String titulo, String artista, String album, String genero, int duracion, String ruta) {
        this.id_cancion = id_cancion;
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.genero = genero;
        this.duracion = duracion;
        this.ruta = ruta;
    }

    public int getid(){
        return id_cancion;
    }
    public void setid(int id_cancion){
        this.id_cancion = id_cancion;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getRuta() {
        return ruta;
    }
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public String toString() {
        return this.titulo + " = " + this.artista;
    }
}