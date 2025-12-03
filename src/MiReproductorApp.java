
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MiReproductorApp extends JFrame {


    private static final Color COLOR_FONDO = new Color(18, 18, 18);
    private static final Color COLOR_LATERAL = new Color(0, 0, 0); 
    private static final Color COLOR_REPRODUCTOR = new Color(28, 28, 28); 
    private static final Color COLOR_TEXTO = new Color(255, 255, 255);
    private static final Color COLOR_ENFASIS = new Color(30, 215, 96);
    private static final Font FUENTE_ICONOS = new Font("Segoe UI Symbol",Font.PLAIN,30);
    private static final Font FUENTE_TEXTO = new Font("Segoe UI Symbol", Font.PLAIN, 14);

    private JPanel panelContenidoCentral; 
    private JPanel panelMisPlaylists;
    private java.util.List<Cancion> todasLasCanciones; 
    private PlaylistDAO playlistDAO; 
    private Controlador controlador; 
    private CancionDAO cancionDAO;
    private VisualizadorAudio visualizador;

    public MiReproductorApp() {
        setTitle("VWAMP MP3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null); 
        getContentPane().setBackground(COLOR_FONDO);

        setLayout(new BorderLayout());

        try {
            controlador = new Controlador();
            cancionDAO = new CancionDAO();
            playlistDAO = new PlaylistDAO();

            todasLasCanciones = cancionDAO.obtenerCanciones();
            
            if (todasLasCanciones.isEmpty()) {
                System.out.println("La base de datos devolvió 0 canciones.");
            } else {
                System.out.println("✅ Se cargaron " + todasLasCanciones.size() + " canciones de la BD.");
                controlador.cargarLista(todasLasCanciones);
            }

        } catch (Exception e) {
            System.err.println("Error al conectar con la Base de Datos o el Controlador:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de BD: " + e.getMessage());
        }

        JPanel panelLateral = crearPanelLateral();
        add(panelLateral, BorderLayout.WEST);

        JPanel contenedorCentral = new JPanel(new BorderLayout());
        contenedorCentral.setBackground(COLOR_FONDO);
        panelContenidoCentral = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panelContenidoCentral.setBackground(COLOR_FONDO);
        JScrollPane scrollPane = new JScrollPane(panelContenidoCentral);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(COLOR_FONDO);

        contenedorCentral.add(scrollPane, BorderLayout.CENTER);
        add(contenedorCentral, BorderLayout.CENTER);

        JPanel panelReproductor = crearPanelReproductor();
        add(panelReproductor, BorderLayout.SOUTH);

        mostrarVistaInicio();
    }


    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_LATERAL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
        panel.setPreferredSize(new Dimension(250, getHeight()));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15)); 

        Font fuenteMenu = new Font("Arial", Font.BOLD, 14);
        


        JLabel lblTitulo = new JLabel("VWAMP PLAYER");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_ENFASIS);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); 
        
        JButton btnInicio = crearBotonMenu("Inicio", fuenteMenu, false);
        btnInicio.addActionListener(e -> mostrarVistaInicio()); 
        panel.add(btnInicio);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnBuscar = crearBotonMenu("Buscar", fuenteMenu, false);
        btnBuscar.addActionListener(e -> mostrarVistaBuscar()); 
        panel.add(btnBuscar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnBiblioteca = crearBotonMenu("Tu Biblioteca", fuenteMenu, false);
        btnBiblioteca.addActionListener(e -> mostrarVistaBiblioteca()); 
        panel.add(btnBiblioteca);
        
        panel.add(Box.createRigidArea(new Dimension(0, 30)));


        JLabel lblPlaylists = new JLabel("PLAYLISTS");
        lblPlaylists.setFont(new Font("Arial", Font.BOLD, 12));
        lblPlaylists.setForeground(Color.LIGHT_GRAY);
        lblPlaylists.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblPlaylists);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnNuevaPlaylist = new JButton("+ Crear Playlist");
        btnNuevaPlaylist.setFont(fuenteMenu);
        btnNuevaPlaylist.setBackground(COLOR_LATERAL);
        btnNuevaPlaylist.setForeground(COLOR_TEXTO);
        btnNuevaPlaylist.setBorderPainted(false);
        btnNuevaPlaylist.setFocusPainted(false);
        btnNuevaPlaylist.setContentAreaFilled(false); 
        btnNuevaPlaylist.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevaPlaylist.setHorizontalAlignment(SwingConstants.LEFT);
        btnNuevaPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
        

        btnNuevaPlaylist.addActionListener(e -> crearNuevaPlaylist());
        
        panel.add(btnNuevaPlaylist);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));


        panelMisPlaylists = new JPanel();
        panelMisPlaylists.setLayout(new BoxLayout(panelMisPlaylists, BoxLayout.Y_AXIS));
        panelMisPlaylists.setBackground(COLOR_LATERAL); 

        panel.add(panelMisPlaylists);

        actualizarListaPlaylists();

        return panel;
    }


    private JButton crearBotonMenu(String texto, Font fuente, boolean isPlaylist) {
        JButton btn = new JButton(texto);
        btn.setFont(fuente);
        btn.setForeground(isPlaylist ? Color.GRAY : COLOR_TEXTO);
        btn.setBackground(COLOR_LATERAL);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(COLOR_ENFASIS);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(isPlaylist ? Color.GRAY : COLOR_TEXTO);
            }
        });

        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); 
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }



    private JPanel crearPanelReproductor() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_REPRODUCTOR);
        panel.setPreferredSize(new Dimension(getWidth(), 90));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));


        JPanel infoCancion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoCancion.setBackground(COLOR_REPRODUCTOR);
        infoCancion.setPreferredSize(new Dimension(250, 70));

        
        
        JLabel lblPlaceholder = new JLabel("🎵");
        lblPlaceholder.setFont(new Font("Arial", Font.PLAIN, 30));
        
        JPanel textoInfo = new JPanel(new GridLayout(2, 1));
        textoInfo.setBackground(COLOR_REPRODUCTOR);
        

        JLabel lblTitulo = new JLabel("Seleccione canción");
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel lblArtista = new JLabel("--");
        lblArtista.setForeground(Color.GRAY);
        lblArtista.setFont(new Font("Arial", Font.PLAIN, 12));
        
        textoInfo.add(lblTitulo);
        textoInfo.add(lblArtista);
        infoCancion.add(lblPlaceholder);
        infoCancion.add(textoInfo);
        panel.add(infoCancion, BorderLayout.WEST);

        JPanel controles = new JPanel();
        controles.setLayout(new BoxLayout(controles, BoxLayout.Y_AXIS));
        controles.setBackground(COLOR_REPRODUCTOR);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botones.setBackground(COLOR_REPRODUCTOR);
        
        JButton btnPrev = crearBotonControl("\u23EE");
        JButton btnPlay = crearBotonControl("\u25B6");
        JButton btnNext = crearBotonControl("\u23ED");
        
        btnPrev.addActionListener(e -> controlador.anterior());
        btnNext.addActionListener(e -> controlador.siguiente());
        btnPlay.addActionListener(e -> {
            controlador.playPause();

        });

        botones.add(crearBotonControl("↩️")); 
        botones.add(btnPrev);
        botones.add(btnPlay);
        botones.add(btnNext);
        botones.add(crearBotonControl("🔀")); 

        JPanel progreso = new JPanel(new GridBagLayout());
        progreso.setBackground(COLOR_REPRODUCTOR);
        
        JLabel lblTiempoActual = new JLabel("00:00");
        lblTiempoActual.setForeground(Color.GRAY);
        JLabel lblTiempoTotal = new JLabel("00:00");
        lblTiempoTotal.setForeground(Color.GRAY);
        
        JSlider sliderProgreso = new JSlider(0, 100, 0);
        sliderProgreso.setBackground(COLOR_REPRODUCTOR);
        sliderProgreso.setPreferredSize(new Dimension(400, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.gridx = 0; progreso.add(lblTiempoActual, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; progreso.add(sliderProgreso, gbc);
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; progreso.add(lblTiempoTotal, gbc);

        controles.add(botones);
        controles.add(progreso);
        panel.add(controles, BorderLayout.CENTER);


        JPanel controlesDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        controlesDerecha.setBackground(COLOR_REPRODUCTOR);
        controlesDerecha.setPreferredSize(new Dimension(200, 70));

        JLabel lblVol = new JLabel("\uD83D\uDD0A");
        lblVol.setFont(FUENTE_ICONOS.deriveFont(Font.PLAIN, 20f));
        JSlider sliderVolumen = new JSlider(0, 100, 100); 
        sliderVolumen.setBackground(COLOR_REPRODUCTOR);
        sliderVolumen.setPreferredSize(new Dimension(100, 15));
        
        
        sliderVolumen.addChangeListener(e -> controlador.cambiarVolumen(sliderVolumen.getValue()));

        controlesDerecha.add(lblVol);
        controlesDerecha.add(sliderVolumen);
        panel.add(controlesDerecha, BorderLayout.EAST);

        controlador.conectarComponentesVisuales(sliderProgreso, lblTiempoActual, lblTiempoTotal, lblTitulo, lblArtista);

        return panel;
        
    }

    private JButton crearBotonControl(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_ICONOS);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(COLOR_TEXTO);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void mostrarVistaInicio() {
        panelContenidoCentral.removeAll();

        panelContenidoCentral.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("¡Bienvenido a VWAMP!", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 40));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(new EmptyBorder(40, 0, 40, 0)); 

        JPanel panelVis = new JPanel(new GridBagLayout()); 
        panelVis.setBackground(COLOR_FONDO);

        visualizador = new VisualizadorAudio(); 
        visualizador.setPreferredSize(new Dimension(800, 350)); 

        
        Reproductor.getInstancia().setAudioSpectrumListener(visualizador);

        panelVis.add(visualizador);

        panelContenidoCentral.add(titulo, BorderLayout.NORTH);
        panelContenidoCentral.add(panelVis, BorderLayout.CENTER);

        refrescarPanel();
    }
    

    private void mostrarVistaBiblioteca() {
        panelContenidoCentral.removeAll();
        panelContenidoCentral.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Tu Biblioteca (" + todasLasCanciones.size() + " canciones)");
        titulo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        titulo.setForeground(COLOR_TEXTO);
        
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panelTitulo.setBackground(COLOR_FONDO);
        panelTitulo.add(titulo);
        
        panelContenidoCentral.add(panelTitulo, BorderLayout.NORTH);

        JPanel gridCanciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        gridCanciones.setBackground(COLOR_FONDO);

        if (todasLasCanciones.isEmpty()) {
            JLabel lblVacio = new JLabel("No se encontraron canciones.");
            lblVacio.setForeground(Color.GRAY);
            gridCanciones.add(lblVacio);
        } else {
            for (int i = 0; i < todasLasCanciones.size(); i++) {
                Cancion c = todasLasCanciones.get(i);
                JPanel tarjeta = crearTarjetaCancion(c, i, todasLasCanciones);
                gridCanciones.add(tarjeta);
            }

            int totalCanciones = todasLasCanciones.size();
            int columnasEstimadas = 4; 
            int filas = (int) Math.ceil((double) totalCanciones / columnasEstimadas);
            int alturaTotal = (filas * 260) + 50; 
            
            gridCanciones.setPreferredSize(new Dimension(800, alturaTotal));
        }

        panelContenidoCentral.add(gridCanciones, BorderLayout.CENTER);
        
        refrescarPanel();
    }


    private void actualizarListaPlaylists() {
        if (panelMisPlaylists == null) return;

        panelMisPlaylists.removeAll();
        
        java.util.List<Playlist> misListas = playlistDAO.obtenerPlaylists();
        

        for (Playlist p : misListas) {
            JButton btn = crearBotonMenu(p.getNombre(), FUENTE_TEXTO, true);
            
            btn.addActionListener(e -> mostrarVistaPlaylist(p));
            
            panelMisPlaylists.add(btn);
            panelMisPlaylists.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        panelMisPlaylists.revalidate();
        panelMisPlaylists.repaint();
    }

    private void mostrarVistaBuscar() {
        panelContenidoCentral.removeAll();
        panelContenidoCentral.setLayout(new BorderLayout());

        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBackground(COLOR_FONDO);
        panelBusqueda.setPreferredSize(new Dimension(800, 60));

        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        
        txtBuscar.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        btnBuscar.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        
        panelContenidoCentral.add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelResultados = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panelResultados.setBackground(COLOR_FONDO);

        panelContenidoCentral.add(panelResultados, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {
            String termino = txtBuscar.getText().toLowerCase();
            
            panelResultados.removeAll();

            java.util.List<Cancion> resultadosBusqueda = new java.util.ArrayList<>();
            
            if (todasLasCanciones != null) {
                for (Cancion c : todasLasCanciones) {
                    if (c.getTitulo().toLowerCase().contains(termino) || 
                        c.getArtista().toLowerCase().contains(termino)) {
                        resultadosBusqueda.add(c);
                    }
                }
            }

            if (resultadosBusqueda.isEmpty()) {
                JLabel lblNo = new JLabel("No se encontraron coincidencias para: " + termino);
                lblNo.setForeground(Color.GRAY);
                lblNo.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
                panelResultados.add(lblNo);
                
                panelResultados.setPreferredSize(null); 
            } else {
                for (int i = 0; i < resultadosBusqueda.size(); i++) {
                    Cancion c = resultadosBusqueda.get(i);
                    panelResultados.add(crearTarjetaCancion(c, i, resultadosBusqueda));
                }
                
                int filas = (int) Math.ceil((double) resultadosBusqueda.size() / 4);
                int altura = (filas * 260) + 50;
                panelResultados.setPreferredSize(new Dimension(800, altura));
            }

            panelResultados.revalidate();
            panelResultados.repaint();
        });
        
        refrescarPanel();
    }

    private void crearNuevaPlaylist() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la nueva playlist:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            playlistDAO.CrearPlaylist(nombre);
            JOptionPane.showMessageDialog(this, "Playlist '" + nombre + "' creada exitosamente.");
            actualizarListaPlaylists();
        }
    }

    private void agregarCancionAPlaylistUI(Playlist p) {
        if (todasLasCanciones == null || todasLasCanciones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tu biblioteca está vacía. No tienes canciones para agregar.");
            return;
        }

        String[] opciones = new String[todasLasCanciones.size()];
        for (int i = 0; i < todasLasCanciones.size(); i++) {
            Cancion c = todasLasCanciones.get(i);
            opciones[i] = c.getTitulo() + " - " + c.getArtista();
        }

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona la canción a agregar:",
                "Agregar a " + p.getNombre(),
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            for (Cancion c : todasLasCanciones) {
                String nombreCompleto = c.getTitulo() + " - " + c.getArtista();
                if (nombreCompleto.equals(seleccion)) {
                    
                    playlistDAO.agregarCancionAPlaylist(p.getId_playlist(), c.getid());
                    
                    JOptionPane.showMessageDialog(this, "¡Canción agregada correctamente!");
                    
                    mostrarVistaPlaylist(p);
                    break;
                }
            }
        }
    }


    private void mostrarVistaPlaylist(Playlist p) {
        panelContenidoCentral.removeAll();
        panelContenidoCentral.setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        header.setBackground(COLOR_FONDO);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY)); 

        JLabel titulo = new JLabel("Playlist: " + p.getNombre());
        titulo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        titulo.setForeground(COLOR_TEXTO);

        JButton btnAnadir = new JButton("Añadir Canción");
        btnAnadir.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        btnAnadir.setBackground(COLOR_ENFASIS); 
        btnAnadir.setForeground(Color.BLACK);
        btnAnadir.setFocusPainted(false);
        btnAnadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAnadir.addActionListener(e -> agregarCancionAPlaylistUI(p));

        header.add(titulo);
        header.add(btnAnadir);

        panelContenidoCentral.add(header, BorderLayout.NORTH);

        JPanel gridCanciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        gridCanciones.setBackground(COLOR_FONDO);

        java.util.List<Cancion> cancionesDeEstaPlaylist = controlador.obtenerCancionesDePlaylist(p.getId_playlist());

        if (cancionesDeEstaPlaylist.isEmpty()) {
            JLabel lblVacio = new JLabel("Esta playlist está vacía, presiona el botón verde de arriba para comenzar.", SwingConstants.CENTER);
            lblVacio.setForeground(Color.GRAY);
            lblVacio.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
            
            JPanel panelMensaje = new JPanel(new GridBagLayout());
            panelMensaje.setBackground(COLOR_FONDO);
            panelMensaje.add(lblVacio);
            
            panelContenidoCentral.add(panelMensaje, BorderLayout.CENTER);
            
        } else {
            for (int i = 0; i < cancionesDeEstaPlaylist.size(); i++) {
                gridCanciones.add(crearTarjetaCancion(cancionesDeEstaPlaylist.get(i), i, cancionesDeEstaPlaylist));
            }
            
            int filas = (int) Math.ceil((double) cancionesDeEstaPlaylist.size() / 4);
            int altura = (filas * 260) + 100;
            gridCanciones.setPreferredSize(new Dimension(800, altura));
            
            panelContenidoCentral.add(gridCanciones, BorderLayout.CENTER);
        }

        refrescarPanel();
        
        if (!cancionesDeEstaPlaylist.isEmpty()) {
            controlador.cargarLista(cancionesDeEstaPlaylist);
        }
    }

    private void refrescarPanel() {
        panelContenidoCentral.revalidate();
        panelContenidoCentral.repaint();
    }

    private JPanel crearTarjetaCancion(Cancion c, int indiceEnLista, List<Cancion> listaContexto) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(COLOR_REPRODUCTOR);
        tarjeta.setPreferredSize(new Dimension(160, 220));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    
        JLabel icono = new JLabel("🎵", SwingConstants.CENTER);
        icono.setFont(FUENTE_ICONOS.deriveFont(50f));
        icono.setForeground(COLOR_ENFASIS);
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(c.getTitulo());
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(FUENTE_ICONOS.deriveFont(Font.BOLD, 16f));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblArtista = new JLabel(c.getArtista());
        lblArtista.setForeground(Color.GRAY);
        lblArtista.setFont(FUENTE_ICONOS.deriveFont(Font.BOLD, 11f));
        lblArtista.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(icono);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(lblTitulo);
        tarjeta.add(lblArtista);

        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                controlador.cargarLista(listaContexto);
                
                controlador.reproducirIndice(indiceEnLista);
                
                System.out.println("Reproduciendo desde biblioteca: " + c.getTitulo());
            }
        });

        return tarjeta;
    }


    
}