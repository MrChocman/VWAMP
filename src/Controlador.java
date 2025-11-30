import javax.swing.*;
import java.util.List;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel; 
import javafx.util.Duration;

public class Controlador {
    static {
        try {
            new JFXPanel(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PlaylistDAO playlistDAO = new PlaylistDAO();
    private List<Cancion> colaReproduccion;
    private int indiceActual = -1;
    private Reproductor reproductor;


    private JSlider sliderProgreso;
    private JLabel lblTiempoActual;
    private JLabel lblTiempoTotal;
    private JLabel lblTituloCancion;
    private JLabel lblArtistaCancion;
    

    private boolean usuarioArrastrandoSlider = false;

    public Controlador() {
        this.reproductor = Reproductor.getInstancia();
    }


    public void cargarLista(List<Cancion> canciones) {
        this.colaReproduccion = canciones;
        this.indiceActual = 0;
        if (!canciones.isEmpty()) {
            actualizarInfoCancion(canciones.get(0)); 
        }
    }

    public List<Cancion> obtenerCancionesDePlaylist(int idPlaylist) {
        return playlistDAO.obtenerCancionesDePlaylist(idPlaylist);
    }


    public void reproducirIndice(int indice) {
        if (colaReproduccion != null && indice >= 0 && indice < colaReproduccion.size()) {
            this.indiceActual = indice;
            Cancion cancion = colaReproduccion.get(indice);

            System.out.println("Reproduciendo: " + cancion.getTitulo());
            actualizarInfoCancion(cancion);


            Platform.runLater(() -> {
                reproductor.cargarCancion(cancion.getRuta());
                reproductor.play();
                configurarEventosPlayer();
            });
        }
    }

    public void playPause() {
        Platform.runLater(() -> {
            if (reproductor.getMediaPlayer() != null) {
                if (reproductor.getMediaPlayer().getStatus() == javafx.scene.media.MediaPlayer.Status.PLAYING) {
                    reproductor.pause();
                } else {
                    reproductor.play();
                }
            } else if (colaReproduccion != null && !colaReproduccion.isEmpty()) {

                reproducirIndice(0);
            }
        });
    }

    public void siguiente() {
        if (colaReproduccion == null || colaReproduccion.isEmpty()) return;
        indiceActual++;
        if (indiceActual >= colaReproduccion.size()) indiceActual = 0;
        reproducirIndice(indiceActual);
    }

    public void anterior() {
        if (colaReproduccion == null || colaReproduccion.isEmpty()) return;
        indiceActual--;
        if (indiceActual < 0) indiceActual = colaReproduccion.size() - 1;
        reproducirIndice(indiceActual);
    }
    
    public void cambiarVolumen(int valorSwing) {

        double volumen = valorSwing / 100.0;
        Platform.runLater(() -> reproductor.setVolumen(volumen));
    }


    public void conectarComponentesVisuales(JSlider slider, JLabel lblActual, JLabel lblTotal, JLabel lblTit, JLabel lblArt) {
        this.sliderProgreso = slider;
        this.lblTiempoActual = lblActual;
        this.lblTiempoTotal = lblTotal;
        this.lblTituloCancion = lblTit;
        this.lblArtistaCancion = lblArt;


        slider.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                usuarioArrastrandoSlider = true;
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                usuarioArrastrandoSlider = false;
                int valor = slider.getValue();
                Platform.runLater(() -> reproductor.saltarA(valor));
            }
        });
    }

    private void actualizarInfoCancion(Cancion c) {

        SwingUtilities.invokeLater(() -> {
            if (lblTituloCancion != null) lblTituloCancion.setText(c.getTitulo());
            if (lblArtistaCancion != null) lblArtistaCancion.setText(c.getArtista());
        });
    }

    private String formatearTiempo(double segundos) {
        int min = (int) segundos / 60;
        int sec = (int) segundos % 60;
        return String.format("%02d:%02d", min, sec);
    }


    private void configurarEventosPlayer() {
        var player = reproductor.getMediaPlayer();


        player.setOnEndOfMedia(() -> siguiente());


        player.setOnReady(() -> {
            double duracion = player.getTotalDuration().toSeconds();
            SwingUtilities.invokeLater(() -> {
                if (sliderProgreso != null) sliderProgreso.setMaximum((int) duracion);
                if (lblTiempoTotal != null) lblTiempoTotal.setText(formatearTiempo(duracion));
            });
        });

        player.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!usuarioArrastrandoSlider) {
                double segundos = newTime.toSeconds();
                SwingUtilities.invokeLater(() -> {
                    if (sliderProgreso != null) sliderProgreso.setValue((int) segundos);
                    if (lblTiempoActual != null) lblTiempoActual.setText(formatearTiempo(segundos));
                });
            }
        });
    }
}